package network;

import game.Player;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private String name;
    
    private int nPlayers;
    private List<ClientSocket> clientSockets;
    
    private Player player;
    
    private double timer = 0.d, timerLimit = 2.1d;
    
    private List<String> updates = new ArrayList();
    private List<String> gUpdates = new ArrayList();
    
    // Host
    private ServerSocket serverSocket;
    private boolean searchForClients;
    private Thread thread;
    private int nextId;
    
    // Client
    private String hostAddress;
    private Socket hostSocket;
    private DataOutputStream writer;
    private InputStream reader;
    private String currentRequest = "";
    private boolean isConnected = false;
    
    public MultiplayerHandler(Player player)
    {
        hostAddress = "81.225.136.216";
        this.player = player;
    }
    
    public MultiplayerHandler(boolean host, Player player)
    {
        hostAddress = "192.168.0.100";
        this.host = !host;
        setAsHost(host);
        this.player = player;
    }
    
    public boolean isHost()
    {
        return host;
    }
    
    public boolean isConnected()
    {
        return isConnected;
    }
    
    /**
     * Tells if a new player has been connected.
     * 
     * @return true if there is a new player.
     */
    public boolean hasNewPlayer()
    {
        return (nPlayers < clientSockets.size());
    }
    
    /**
     * Gives the socket for the next connected player.
     * 
     * @return Socket of the next of the latest connected players.
     */
    public ClientSocket getNewPlayer()
    {
        nPlayers++;
        return clientSockets.get(nPlayers-1);
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
                clientSockets = new ArrayList();
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
                    gUpdates.add("rm " + clientSockets.get(i).id);
                    clientSockets.get(i).close();
                    clientSockets.remove(i);
                    i--;
                } catch (Exception e){}
            }
        
        nPlayers = 0;
    }
    
    public void removeClient(String id)
    {
        for(int i = 0; i < clientSockets.size(); i++)
        {
            if(clientSockets.get(i).getId().equals(id))
            {
                clientSockets.remove(i);
                nPlayers--;
                break;
            }
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
            nextId++;
            clientSocket = new ClientSocket(serverSocket.accept(), "" + nextId);
            clientSocket.write("add 0 " + name + " " + player.getX() + " " + player.getY() + "\r\n");
            
            for(ClientSocket cs : clientSockets)
            {
                clientSocket.write("add " + cs.id + " " + cs.name + " " + cs.position.x + " " + cs.position.y + "\r\n");
                cs.write("add " + clientSocket.id + " " + clientSocket.name + " " + clientSocket.position.x + " " + clientSocket.position.y + "\r\n");
            }
            
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
            isConnected = true;
            
            String input = readFromHost();
            writer.writeBytes("Omni\r\n");
            input = readFromHost();
            writer.writeBytes(player.getX() + " " + player.getY() + "\r\n");
            
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
                writer.writeBytes("rm this\r\n");
                hostSocket.close();
                isConnected = false;
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
    
    public void update(double delta)
    {
        if(host)
        {
            timer += delta;
            if(timer >= timerLimit || player.hasNewData())
            {
                try {
                    updates.add("pos 0 " + player.getX() + " " + player.getY());
                    updates.add("vel 0 " + player.getVelocity().x + " " + player.getVelocity().y);
                } catch (Exception e) {}
                timer = 0;
            }
            
            for(int i = 0; i < Math.min(nPlayers, clientSockets.size()); i++)
            {
                clientSockets.get(i).update(gUpdates, updates);
            }
            
            for(int i = 0; i < updates.size(); i++)
            {
                for(int j = 0; j < Math.min(nPlayers, clientSockets.size()); j++)
                {
                    clientSockets.get(j).write(updates.get(i) + "\r\n");
                }
                updates.remove(0);
                i--;
            }
        }
        else if(hostSocket.isConnected() && !hostSocket.isClosed())
        {
            timer += delta;
            if(timer >= timerLimit || player.hasNewData())
            {
                try {
                    writer.writeBytes("pos " + player.getX() + " " + player.getY() + "\r\n" +
                                      "vel " + player.getVelocity().x + " " + player.getVelocity().y + "\r\n");
                } catch (Exception e) {}
                timer = 0;
            }
            
            updateFromHost();
            applyUpdates();
        }
    }
    
    /**
     * Client
     */
    private void updateFromHost()
    {
        try {
            while(reader.available() > 0)
            {
                int inputByte = reader.read();
                currentRequest += (char)inputByte;

                if(currentRequest.endsWith("\r\n"))
                {
                    updates.add(currentRequest.split("\r\n")[0]);
                    currentRequest = "";
                    break;
                }
            }
        } catch (IOException e) {
            currentRequest = "";
        }
    }
    
    private void applyUpdates()
    {
        for(int i = 0; i < updates.size(); i++)
        {
            String[] words = updates.get(i).split(" ");
            System.out.println(words);
            
            switch(words[0])
            {
                case "pos":
                {
                    ClientSocket cs = getClient(words[1]);
                    if(cs != null)
                    {
                        cs.position.x = Float.parseFloat(words[2]);
                        cs.position.y = Float.parseFloat(words[3]);
                        cs.newData = true;
                    }
                    break;
                }
                
                case "vel":
                {
                    ClientSocket cs = getClient(words[1]);
                    if(cs != null)
                    {
                        cs.velocity.x = Float.parseFloat(words[2]);
                        cs.velocity.y = Float.parseFloat(words[3]);
                        cs.newData = true;
                    }
                    break;
                }
                
                case "add":
                {
                    clientSockets.add(new ClientSocket(words[1], words[2], Float.parseFloat(words[3]), Float.parseFloat(words[4])));
                    break;
                }
                    
                case "rm":
                {
                    gUpdates.add(updates.get(i));
                    break;
                }
            }
            updates.remove(i);
            i--;
        }
    }
    
    public boolean hasNewUpdate()
    {
        return (gUpdates.size() > 0);
    }
    
    public String getNextUpdate()
    {
        String s = gUpdates.get(0);
        gUpdates.remove(0);
        return s;
    }
    
    private ClientSocket getClient(String id)
    {
        ClientSocket cs = null;
        
        for(ClientSocket c : clientSockets)
        {
            if(id.equals(c.getId()))
            {
                cs = c;
                break;
            }
        }
        
        return cs;
    }
}
