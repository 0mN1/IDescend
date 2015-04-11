package game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by Lampkraft on 2015-04-08.
 */
public class Tile extends Entity {

    Image image;

    public Tile(float x, float y, int width, int height, float depth, BufferedImage image)
    {
        this.image = image;
        this.x = x;
        this.y = y;
        this.xscale = 1;
        this.yscale = 1;
        this.maxwidth = width;
        this.maxheight = height;
        this.width = Math.round(xscale * maxwidth);
        this.height = Math.round(yscale * maxheight);
        this.xscale = this.yscale = 1.f;
        this.v1 = new Vector2f();
        this.v2 = new Vector2f();
    }

    public void draw(Graphics2D g, View view)
    {
        this.v1 = setOffset(view);
        this.v2 = setScaleOffset(view);

        width = (int)v2.a;
        height = (int)v2.b;

        g.drawImage(image, (int)(v1.a), (int)(v1.b), (int)v2.a, (int)v2.b, null);
    }
}
