package game;

/**
 * Created by Lampkraft on 2015-04-05.
 */
public class Key {

    private boolean last, current;
    private int id;

    public Key(int id)
    {
        this.id = id;
    }

    public void update(boolean b)
    {
        last = current;
        current = b;
    }

    public boolean isHeld()
    {
        return last && current;
    }

    public boolean isPressed()
    {
        return !last && current;
    }

    public boolean isReleased()
    {
        return last && !current;
    }

    public boolean isUntouched()
    {
        return !last && !current;
    }

    public int getId()
    {
        return id;
    }
}
