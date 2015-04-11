package game;


import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *
 * @author Herman Hallstedt
 * @version 2015-04-01
 */
public class Player extends Character
{
    public Player(BufferedImage image)
    {
        super(0, 0, 167, 144, image);
        this.image = image;
    }

    public void update(Keyboard key, double delta, View view)
    {
        if(key.up.isHeld())
            moveUp(delta);
        if(key.down.isHeld())
            moveDown(delta);
        if(key.left.isHeld())
            moveLeft(delta);
        if(key.right.isHeld())
            moveRight(delta);

        view.setPosition(x, y);
    }
}
