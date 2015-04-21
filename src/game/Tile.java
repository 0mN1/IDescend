package game;

import graphics.Vector2f;
import graphics.Entity;
import graphics.View;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Lampkraft on 2015-04-08.
 */
public class Tile extends Entity {

    Image image;

    public Tile(float x, float y, int width, int height, float depth, BufferedImage image)
    {
        this.image = image;
        xPos = x;
        yPos = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D g, View view)
    {
        Vector2f drawPosition = getDrawPosition(view);
        Vector2f drawScale = getDrawScale(view);

        g.drawImage(image, (int)drawPosition.x, (int)drawPosition.y, (int)drawScale.x, (int)drawScale.y, null);
    }
}
