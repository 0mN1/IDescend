package network;

import graphics.Vector2f;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 
 * @author Herman Hallstedt
 */
public class ClientSocket
{
    public boolean newData = true;
    public String name, id;
    public Vector2f position = new Vector2f();
    public Vector2f velocity = new Vector2f();
    
    // host
    private Socket socket;
    private DataOutputStream writer;
    private InputStream reader;
    private String currentRequest = "";
    public List<String> updates = new ArrayList();
    
    public ClientSocket(String id, String name, float x, float y)
    {
        this.id = id;
        this.name = name;
        position.set(x, y);
    }
    
    public ClientSocket(Socket s, String id)
    {
        this.id = id;
        try {
            socket = s;
            writer = new DataOutputStream(socket.getOutputStream());
            reader = socket.getInputStream();
            
            writer.writeBytes("What's your name?\r\n");
            
            name = read();
            
            writer.writeBytes("Hi " + name + "!\r\n");
            
            String msg = read();
            position.x = Float.parseFloat(msg.split(" ")[0]);
            position.y = Float.parseFloat(msg.split(" ")[1]);
            
        } catch (Exception ex) {}
    }
    
    public void write(String msg)
    {
        try {
            writer.writeBytes(msg);
        } catch (IOException ex) {}
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
    
    public void update(List<String> gUpdates, List<String> nUpdates)
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
        
        applyUpdates(gUpdates, nUpdates);
    }
    
    public void applyUpdates(List<String> gUpdates, List<String> nUpdates)
    {
        for(int i = 0; i < updates.size(); i++)
        {
            String[] words = updates.get(i).split(" ");
            System.out.println(Arrays.toString(words));
            
            switch (words[0])
            {
                case "pos":
                {
                    position.x = Float.parseFloat(words[1]);
                    position.y = Float.parseFloat(words[2]);
                    newData = true;
                    
                    if(nUpdates != null)
                        nUpdates.add("pos " + id + " " + words[1] + " " + words[2]);
                    
                    break;
                }
                case "vel":
                {
                    velocity.x = Float.parseFloat(words[1]);
                    velocity.y = Float.parseFloat(words[2]);
                    newData = true;
                    
                    if(nUpdates != null)
                        nUpdates.add("vel " + id + " " + words[1] + " " + words[2]);
                    
                    break;
                }
                case "rm":
                {
                    if(gUpdates != null)
                        gUpdates.add("rm " + id);
                    if(nUpdates != null)
                        nUpdates.add("rm " + id);
                    break;
                }
            }
            updates.remove(0);
            i--;
        }
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
    
    public String getId()
    {
        return id;
    }
    
    public boolean hasNewData()
    {
        if(newData)
        {
            newData = false;
            return true;
        }
        else
            return false;    
    }
}
