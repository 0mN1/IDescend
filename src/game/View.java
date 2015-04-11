package game;

/**
 * Created by Lampkraft on 2015-04-05.
 */
public class View {

    private int width, height;
    private float x, y, zoom;

    public View(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.zoom = 1;
    }

    public void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public void setPosition(float x, float y)
    {
        this.x = x + width/2;
        this.y = y + height/2;
    }

    public void zoom(float amount, double delta)
    {
        zoom += amount * delta;
    }

    public float getZoom()
    {
        return zoom;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }
}
