package graphics;

/**
 * Created by Lampkraft on 2015-04-05.
 */
public class Vector2f {

    public float x, y;

    public Vector2f(float a, float b)
    {
        x = a;
        y = b;
    }
    
    public Vector2f(Vector2f v)
    {
        x = v.x;
        y = v.y;
    }

    public Vector2f()
    {
        x = y = 0.f;
    }
    
    public boolean compare(Vector2f v)
    {
        return (x == v.x && y == v.y);
    }
    
    public Vector2f set(float a, float b)
    {
        x = a;
        y = b;
        return this;
    }
    
    public Vector2f copy(Vector2f v)
    {
        x = v.x;
        y = v.y;
        return this;
    }
}
