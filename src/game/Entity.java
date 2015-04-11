package game;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Lampkraft on 2015-04-05.
 */
public class Entity
{
    protected float x, y, xscale, yscale, offsetscale;
    protected int maxwidth, maxheight, width, height;
    protected BufferedImage image;
    protected Vector2f v1, v2;

    protected Vector2f setOffset(View view)
    {
        return new Vector2f(((x - view.getX() + view.getWidth()/2) * offsetscale) + view.getWidth()/2 - width/2, ((y - view.getY() + view.getHeight()/2) * offsetscale) + view.getHeight()/2 - height/2);
    }

    protected Vector2f setScaleOffset(View view)
    {
        offsetscale = view.getZoom();
        return new Vector2f(offsetscale * maxwidth * xscale, offsetscale * maxheight * yscale);
    }

    protected void setScale(float x, float y)
    {
        this.xscale = x;
        this.yscale = y;
    }

    public void setPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return x;
    }

    public float getOffsetScale() {
        return offsetscale;
    }

    public void setOffsetScale(float offsetScale) {
        this.offsetscale = offsetScale;
    }
}
