package graphics;

import java.util.Random;

public class Screen 
{

	private int width, height;
	public int[] pixels;
	
	private final int MAP_SIZE = 16;
	private final int MAP_SIZE_MASK = MAP_SIZE - 1;
	
	public int[] tiles = new int[MAP_SIZE * MAP_SIZE];
	
	private Random random = new Random();
	
	public Screen(int width, int height)
	{
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		
	}
	
	public void clear()
	{
		for (int i = 0; i < pixels.length; i++)
		{
			pixels[i] = 0;
		}
	}
	
	public void render(int xOffset, int yOffset)
	{
		for (int y = 0; y < height; y++)
		{
			int yy = y + yOffset;
			for (int x = 0; x < width; x++)
			{
				int xx = x + xOffset;
				int tileIndex = ((xx / MAP_SIZE) & MAP_SIZE_MASK) + ((yy / MAP_SIZE) & MAP_SIZE_MASK) * MAP_SIZE ;
				pixels[x + y * width] = tiles[tileIndex];
			}
		}
	}
}
