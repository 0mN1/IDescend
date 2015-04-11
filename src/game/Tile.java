package game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by Lampkraft on 2015-04-08.
 */
public class Tile extends Entity {

    public Tile(float x, float y, int width, int height, float depth, BufferedImage image)
    {
        this.image = image;
        this.x = x;
        this.y = y;
        this.xscale = this.yscale = 1;
        this.maxwidth = width;
        this.maxheight = height;
        this.width = Math.round(xscale * maxwidth);
        this.height = Math.round(yscale * maxheight);
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
