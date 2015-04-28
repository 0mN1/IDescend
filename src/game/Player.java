package game;


import graphics.Vector2f;
import input.Keyboard;
import graphics.View;
import java.awt.image.BufferedImage;

/**
 *
 * @author Herman Hallstedt
 * @version 2015-04-01
 */
public class Player extends Character
{
    private boolean newData;
    private Vector2f prevVel = new Vector2f();
    
    public Player(BufferedImage image)
    {
        super(0, 0, image);
        this.image = image;
    }

    public void update(Keyboard key, double delta, View view)
    {
        prevVel.copy(velocity);
        
        stop();
        
        if(key.up.isHeld() || key.wkey.isHeld())
            moveUp();
        if(key.down.isHeld() || key.skey.isHeld())
            moveDown();
        if(key.left.isHeld() || key.akey.isHeld())
            moveLeft();
        if(key.right.isHeld() || key.dkey.isHeld())
            moveRight();
        
        move(delta);
        
        if(!velocity.compare(prevVel))
            newData = true;

        view.setPosition(xPos, yPos);
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
