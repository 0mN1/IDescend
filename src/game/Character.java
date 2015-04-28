package game;

import graphics.Vector2f;
import graphics.Entity;
import graphics.View;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Character extends Entity{

    protected Vector2f velocity = new Vector2f();
    protected float acceleration;
    protected BufferedImage image;

    public Character(float x, float y, BufferedImage image)
    {
        this.image = image;
        this.xPos = x;
        this.yPos = y;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.acceleration = 200.f;
    }
    
    public Vector2f getVelocity()
    {
        return velocity;
    }

    public void update()
    {

    }

    public void draw(Graphics2D g, View view)
    {
        Vector2f drawPosition = getDrawPosition(view);
        Vector2f drawScale = getDrawScale(view);

        g.drawImage(image, (int)drawPosition.x, (int)drawPosition.y, (int)drawScale.x, (int)drawScale.y, null);
    }
    
    public void move(double delta)
    {
        xPos += velocity.x * delta;
        yPos += velocity.y * delta;
    }
    
    public void stop()
    {
        velocity.x = 0.f;
        velocity.y = 0.f;
    }

    public void moveUp()
    {
        velocity.y = -acceleration;
    }

    public void moveDown()
    {
        velocity.y = acceleration;
    }

    public void moveRight()
    {
        velocity.x = acceleration;
    }

    public void moveLeft()
    {
        velocity.x = -acceleration;
    }
}

