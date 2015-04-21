package game;

import graphics.Vector2f;
import graphics.Entity;
import graphics.View;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Character extends Entity{

    protected float velocity;
    protected BufferedImage image;

    public Character(float x, float y, int width, int height, BufferedImage image)
    {
        this.image = image;
        this.xPos = x;
        this.yPos = y;
        this.width = width;
        this.height = height;
        this.velocity = 200.f;
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

    public void moveUp(double delta)
    {
        yPos -= velocity * delta;
    }

    public void moveDown(double delta)
    {
        yPos += velocity * delta;
    }

    public void moveRight(double delta)
    {
        xPos += velocity * delta;
    }

    public void moveLeft(double delta)
    {
        xPos -= velocity * delta;
    }
}

