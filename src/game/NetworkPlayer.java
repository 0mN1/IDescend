package game;

import java.awt.image.BufferedImage;
import network.ClientSocket;

/**
 *
 * @author Herman Hallstedt
 * @version 2015-04-11
 */
public class NetworkPlayer extends Character
{
    private String name, id;
    private ClientSocket client;
            
    public NetworkPlayer(ClientSocket client, BufferedImage image)
    {
        super(client.position.x, client.position.y, image);
        name = client.name;
        id = client.id;
        
        this.client = client;
    }
    
    public void update(double delta)
    {
        if(client.hasNewData())
        {
            xPos = client.position.x;
            yPos = client.position.y;
            velocity.x = client.velocity.x;
            velocity.y = client.velocity.y;
        }
        
        move(delta);
    }
    
    public String getId()
    {
        return id;
    }
}
