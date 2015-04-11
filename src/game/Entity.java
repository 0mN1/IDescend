package game;

/**
 * Created by Lampkraft on 2015-04-05.
 */
public class Entity
{
    protected float xPos, yPos, xScale = 1.f, yScale = 1.f;
    protected int width, height;

    protected Vector2f getDrawPosition(View view)
    {
        return new Vector2f(((xPos - view.getX() + view.getWidth()/2 - width/2) * view.getZoom()) + view.getWidth()/2,
                            ((yPos - view.getY() + view.getHeight()/2 - height/2) * view.getZoom()) + view.getHeight()/2);
    }

    protected Vector2f getDrawScale(View view)
    {
        return new Vector2f(view.getZoom() * width * xScale, view.getZoom() * height * yScale);
    }

    protected void setScale(float x, float y)
    {
        xScale = x;
        yScale = y;
    }

    public void setPosition(float x, float y)
    {
        xPos = x;
        yPos = y;
    }

    public void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public void setX(float x)
    {
        xPos = x;
    }

    public void setY(float y)
    {
        yPos = y;
    }

    public float getX()
    {
        return xPos;
    }

    public float getY()
    {
        return yPos;
    }
}
