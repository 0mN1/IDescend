package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Character extends Entity{

    protected float velocity;

    public Character(float x, float y, int width, int height, BufferedImage image)
    {
        this.image = image;
        this.x = x;
        this.y = y;
        this.xscale = this.yscale = 1.f;
        this.maxwidth = width;
        this.maxheight = height;
        this.width = Math.round(xscale * maxwidth * offsetscale);
        this.height = Math.round(yscale * maxheight * offsetscale);
        this.velocity = 200.f;
        this.v1 = new Vector2f();
        this.v2 = new Vector2f();
    }

    public void update()
    {

    }

    public void draw(Graphics2D g, View view)
    {
        this.v1 = setOffset(view);
        this.v2 = setScaleOffset(view);

        width = (int)v2.a;
        height = (int)v2.b;

        g.drawImage(image, (int)(v1.a), (int)(v1.b), (int)v2.a, (int)v2.b, null);
    }

    public void moveUp(double delta)
    {
        y -= velocity * delta;
    }

    public void moveDown(double delta)
    {
        y += velocity * delta;
    }

    public void moveRight(double delta)
    {
        x += velocity * delta;
    }

    public void moveLeft(double delta)
    {
        x -= velocity * delta;
    }
}

