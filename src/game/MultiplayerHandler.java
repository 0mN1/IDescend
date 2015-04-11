package game;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.List;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Herman Hallstedt
 * @version 2015-04-04
 */
public class MultiplayerHandler implements Runnable
{
    private boolean host;
    
    // Host
    private ServerSocket serverSocket;
    private List<ClientSocket> clientSockets;
    private boolean searchForClients;
    private Thread thread;
    
    // Client
    private String hostAddress;
    private Socket hostSocket;
    private DataOutputStream writer;
    private InputStream reader;
    
    public MultiplayerHandler()
    {
        hostAddress = "127.0.0.1";
    }
    
    public MultiplayerHandler(boolean host)
    {
        hostAddress = "127.0.0.1";
        this.host = !host;
        setAsHost(host);
    }
    
    /**
     * Sets the handler to act as host or client
     * @param host
     */
    public final void setAsHost(boolean host)
    {
        if(this.host ^ host)
        {
            if(host == true)
            {
                System.out.println("Is now host");
                
                try
                {
                    serverSocket = new ServerSocket(8000);
                    serverSocket.setSoTimeout(1000);
                }
                catch (IOException ex)
                {}
                clientSockets = new ArrayList();
                
                startListening();
            }
            else
            {
                System.out.println("Is now client");
                
                close();
                hostSocket = new Socket();
                clientSockets = null;
            }
            
            this.host = host;
        }
    }
    
    /**
     * Closes the connection to the server or clients.
     */
    public void close()
    {
        if(host)
        {
            stopListening();
            disconnectAllClients();
        }
        else
        {
            disconnectFromHost();
        }
    }
    
    /**
     * Sets the ip-adress of the host.
     * @param ip Host address.
     */
    public void setHost(String ip)
    {
        hostAddress = ip;
    }
    
    /**
     * Server
     * Stars a listening thread.
     */
    public final synchronized void startListening()
    {
            searchForClients = true;
            thread = new Thread(this, "Server");
            thread.start();
            System.out.println("Is now listening");
    }

    /**
     * Server
     * Stops listening for new clients.
     */
    public synchronized void stopListening()
    {
        if(searchForClients)
        {
            searchForClients = false;
            try {
                thread.join();
            } catch (Exception e){}
            
            try {
                serverSocket.close();
            } catch (Exception e) {}
        }
    }
    
    /**
     * Server
     * Disconnects and removes all clients.
     */
    public void disconnectAllClients()
    {
        if(clientSockets != null)
            for(int i = 0; i < clientSockets.size(); i++)
            {
                try
                {
                    clientSockets.get(i).close();
                    clientSockets.remove(i);
                    i--;
                } catch (Exception e){}
            }
    }
    
    @Override // Server
    public void run()
    {
        while(searchForClients)
        {
            ClientSocket newClient = listen();
            
            if(newClient != null)
            {
                clientSockets.add(newClient);
            }
        }
    }
    
    /**
     * Server
     * Listens for a client. Timeouts after set amount of milliseconds.
     * 
     * @return The new client socket
     */
    private ClientSocket listen()
    {
        ClientSocket clientSocket;
        
        try {
            clientSocket = new ClientSocket(serverSocket.accept());
            System.out.println("Client " + clientSocket.getName() + ": " + clientSocket.getInetAddress() + " has connected.");
        } catch(Exception e) {
            clientSocket = null;
        }
        
        return clientSocket;
    }
    
    /**
     * Client
     * Tries to connect to server.
     */
    public void connectToHost()
    {
        try {
            hostSocket.connect(new InetSocketAddress(hostAddress, 8000), 10000);
            writer = new DataOutputStream(hostSocket.getOutputStream());
            reader = hostSocket.getInputStream();
            
            String input = readFromHost();
            writer.writeBytes("Omni\r\n");
            input = readFromHost();
            
        } catch(Exception e) {}
    }
    
    /**
     * Client
     * Disconnects from the server.
     */
    public void disconnectFromHost()
    {
        if(hostSocket.isConnected() && !hostSocket.isClosed())
        {
            try {
                writer.writeBytes("Bye!\r\n");
                hostSocket.close();
            } catch(Exception e) {}
        }
    }
    
    private String readFromHost()
    {
        String request = "";
        
        while(!request.endsWith("\r\n"))
        {
            int inputByte = -1;
            try {
                inputByte = reader.read();
            } catch (IOException ex) {}
            
            if(inputByte != -1)
            {
                request += (char)inputByte;
            }
        }
        
         return request.split("\r\n")[0];
    }
}

/**
 * 
 * @author Herman Hallstedt
 */
class ClientSocket
{
    private Socket socket;
    private DataOutputStream writer;
    private InputStream reader;
    
    private String name;
    
    public ClientSocket(Socket s)
    {
        try {
            socket = s;
            writer = new DataOutputStream(socket.getOutputStream());
            reader = socket.getInputStream();
            
            writer.writeBytes("What's your name?\r\n");
            
            name = read();
            
            writer.writeBytes("Hi " + name + "!\r\n");
        } catch (Exception ex) {}
    }
    
    private String read() throws IOException
    {
        String request = "";
        
        while(!request.endsWith("\r\n"))
        {
            int inputByte = reader.read();
            
            if(inputByte != -1)
            {
                request += (char)inputByte;
            }
        }
        
         return request.split("\r\n")[0];
    }
    
    public void close()
    {
        try {
            writer.writeBytes("See ya " + name + "!\r\n");
            socket.close();
        } catch (Exception ex) {}
    }
    
    public InetAddress getInetAddress()
    {
        return socket.getInetAddress();
    }
    
    public String getName()
    {
        return name;
    }
}
