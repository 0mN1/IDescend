package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Keyboard implements KeyListener
{
	
	private boolean[] keys = new boolean[120];
	public Key up, down, left, right, wkey, akey, skey, dkey, zkey, xkey, ckey, tkey, ykey;

	public Keyboard()
	{
		up = new Key(KeyEvent.VK_UP);
		down = new Key(KeyEvent.VK_DOWN);
		left = new Key(KeyEvent.VK_LEFT);
		right = new Key(KeyEvent.VK_RIGHT);
		wkey = new Key(KeyEvent.VK_W);
		skey = new Key(KeyEvent.VK_S);
		akey = new Key(KeyEvent.VK_A);
		dkey = new Key(KeyEvent.VK_D);
		zkey = new Key(KeyEvent.VK_Z);
		xkey = new Key(KeyEvent.VK_X);
		ckey = new Key(KeyEvent.VK_C);
		tkey = new Key(KeyEvent.VK_T);
		ykey = new Key(KeyEvent.VK_Y);
	}
	
	public void update()
	{
		up.update(keys[up.getId()]);
		down.update(keys[down.getId()]);
		right.update(keys[right.getId()]);
		left.update(keys[left.getId()]);
		wkey.update(keys[wkey.getId()]);
		skey.update(keys[skey.getId()]);
		akey.update(keys[akey.getId()]);
		dkey.update(keys[dkey.getId()]);
		zkey.update(keys[zkey.getId()]);
		xkey.update(keys[xkey.getId()]);
		ckey.update(keys[ckey.getId()]);
		tkey.update(keys[tkey.getId()]);
		ykey.update(keys[ykey.getId()]);
	}

	public void keyPressed(KeyEvent e) 
	{
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) 
	{
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) 
	{
		
	}

}
