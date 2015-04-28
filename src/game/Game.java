package game;

import input.Keyboard;
import graphics.View;
import network.MultiplayerHandler;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class Game extends Canvas implements Runnable
{
    private static final long serialVersionUID = 1L;

    // Resolution
    public static int width = 1280;
    public static int height = 720;
    public static int scale = 1;

    public static String title = "I Descend";

    private Thread thread;
    private JFrame frame;
    private Keyboard key;
    private boolean running = false, readyToQuit = false;

    private Player player;
    private List<NetworkPlayer> netPlayers = new ArrayList();
    private List<Character> characters = new ArrayList<>();
    private List<Tile> tiles = new ArrayList<>();
    private BufferedImage image0, image1, image2, image3;

    private MultiplayerHandler multiplayer;

    private View view;

    public Game()
    {
        Dimension size = new Dimension (width * scale, height * scale);
        setPreferredSize(size);

        frame = new JFrame();

        key = new Keyboard();

        view = new View(width, height);

        addKeyListener(key);

        // Clean up when program exits
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                running = false;
                while(!readyToQuit);
                    
                if (multiplayer != null)
                    multiplayer.close();
            }
        });

        loadResources();

        initiateWorld();
    }

    private void loadResources()
    {
        try {
            image0 = ImageIO.read(new File("res/textures/brick3.png"));
            image1 = ImageIO.read(new File("res/textures/skeleton.png"));
            image2 = ImageIO.read(new File("res/textures/warrior.png"));
            image3 = ImageIO.read(new File("res/textures/mage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initiateWorld()
    {

        player = new Player(image2);
        player.setPosition(width / 2, height / 2);
        
        multiplayer = new MultiplayerHandler(false, player);

        for(int hor = 0; hor < 25; hor++)
        {
            for(int ver = 0; ver < 25; ver++)
            {
                Tile t = new Tile(399 * hor, 399 * ver, 400, 400, 0f, image0);
                tiles.add(t);
            }
        }

        for(int i = 0; i < 100; i++)
        {
            Character c = new Character((float)Math.random() * 5000, (float)Math.random() * 5000,
                            image1);
            characters.add(c);
        }

    }

    public synchronized void start()
    {
        running = true;
        thread = new Thread(this, "Display");
        thread.start();
    }

    public synchronized void stop()
    {
        running = false;
        try {
                thread.join();
        } catch (InterruptedException e) {
                e.printStackTrace();
        }
    }

    public void run()
    {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0;
        double delta = 0;
        int frames = 0;

        requestFocus();

        while (running)
        {
            long now = System.nanoTime();
            delta = (now - lastTime) / ns;
            lastTime = now;

            update(delta);

            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                frame.setTitle(title + " | " + (int)(1/delta) + " " + frames + "fps");
                frames = 0;
            }
        }
        
        readyToQuit = true;
    }
    
    private void removeNetPlayer(String id)
    {
        for(int i = 0; i < netPlayers.size(); i++)
        {
            if(netPlayers.get(i).getId().equals(id))
            {
                netPlayers.remove(i);
                break;
            }
        }
    }

    public void updateMultiplayer(double delta)
    {
        if(multiplayer.hasNewPlayer())
            netPlayers.add(new NetworkPlayer(multiplayer.getNewPlayer(), image3));
        
        multiplayer.update(delta);
        while(multiplayer.hasNewUpdate())
        {
            String msg = multiplayer.getNextUpdate();
            String[] words = msg.split(" ");
            
            if(words[0].equals("rm"))
            {
                multiplayer.removeClient(words[1]);
                removeNetPlayer(words[1]);
            }
        }
        
        for(NetworkPlayer np : netPlayers)
        {
            np.update(delta);
        }
    }
    
    public void update(double delta)
    {
        key.update();
        player.update(key, delta, view);

        updateMultiplayer(delta);
        
        if(key.xkey.isPressed())
            multiplayer.setAsHost(true);
        if(key.zkey.isPressed())
            multiplayer.setAsHost(false);
        if(key.ckey.isPressed())
        {
            if(multiplayer.isHost())
                multiplayer.disconnectAllClients();
            else
            {
                if(!multiplayer.isConnected())
                    multiplayer.connectToHost();
                else
                    multiplayer.disconnectFromHost();
            }
        }
        if(key.tkey.isHeld())
            view.zoom(0.5f, delta);
        if(key.ykey.isHeld())
            view.zoom(-0.5f, delta);
    }

    public void render()
    {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null)
        {
            createBufferStrategy(3);
            return;
        }

        Graphics2D g = (Graphics2D)bs.getDrawGraphics();

        g.setColor(Color.darkGray);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw begin

        for(Tile t : tiles)
        {
            t.draw(g, view);
        }

        for(Character c : characters)
        {
            c.draw(g, view);
        }

        for(NetworkPlayer c : netPlayers)
        {
            c.draw(g, view);
        }

        player.draw(g, view);


        // Draw end

        g.dispose();
        bs.show();
    }

    public static void main(String[] args)
    {
        Game game = new Game();
        game.frame.setResizable(false);
        game.frame.add(game);
        game.frame.pack();
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLocationRelativeTo(null);
        game.frame.setVisible(true);

        game.start();
    }
}
