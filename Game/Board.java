import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener
{
    private final int board_width = 500;
    private final int board_height = 500;
    private final int trailLocation = 5;
    private final int trailMax = board_height * board_width;
    private final int speed = 20;

    private int x[] = new int[trailMax];
    private int y[] = new int[trailMax];

    private int trail_length;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    public boolean trailState = false;

    private Timer timer;
    private Image trail;
    private Image bike;

    public Board()
    {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(board_width, board_height));
        loadImages();
        initGame();
    }

    private void loadImages()
    {
        ImageIcon trail_image = new ImageIcon("images/smalldot.png");
        trail = trail_image.getImage();

        ImageIcon bike_image = new ImageIcon("images/smalldot.png");
        bike = bike_image.getImage();
    }

    private void initGame()
    {
        trail_length = 1;
        for (int z = 0; z < trail_length; z++)
        {
            x[z] = 50 - z *10;
            y[z] = 50;
        }

        timer = new Timer(speed, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g)
    {
        if (inGame)
        {
            for (int z = 0; z < trail_length; z++)
            {
                if (z == 0)
                {
                    g.drawImage(bike, x[z], y[z], this);
                }
                else
                    {
                        g.drawImage(trail, x[z], y[z], this);
                    }
            }
            Toolkit.getDefaultToolkit().sync();
        }
        else
            {
                gameOver(g);
            }
    }
    private void gameOver(Graphics g)
    {
        String message = "Game Over!";
        Font small = new Font("Calibri", Font.BOLD, 14);
        FontMetrics meter = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (board_width - meter.stringWidth(message))/2, board_height/2);
    }

    private void addTrail()
        {
            trail_length++;
        }
    private void move()
    {
        for (int z = trail_length; z > 0; z--)
        {
            x[z] = x[(z-1)];
            y[z] = y[(z-1)];
        }
        if (leftDirection)
        {
            x[0] -= trailLocation;
        }
        if (rightDirection)
        {
            x[0] += trailLocation;
        }
        if (upDirection)
        {
            y[0] -= trailLocation;
        }
        if (downDirection)
        {
            y[0] += trailLocation;
        }
    }

    private void checkCollision()
    {
        for (int z = trail_length; z > 0; z --)
        {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z]))
            {
                inGame = false;
            }
        }
        if (y[0] >= board_height)
        {
            inGame = false;
        }
        if (y[0] < 0)
        {
            inGame = false;
        }
        if (x[0] >= board_width)
        {
            inGame = false;
        }
        if (x[0] < 0)
        {
            inGame = false;
        }
        if (!inGame)
        {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(inGame)
        {
            if(trailState)
            {
                addTrail();
            }
            checkCollision();
            move();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            int key = e.getKeyCode();
            if ((key == KeyEvent.VK_LEFT) && (!rightDirection))
            {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection))
            {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if ((key == KeyEvent.VK_UP) && (!downDirection))
            {
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if ((key == KeyEvent.VK_DOWN) && (!upDirection))
            {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            if ((key == KeyEvent.VK_SPACE))
            {
                if (!trailState)
                {
                    trailState = true;
                }
                else
                    {
                        trailState = false;
                    }
            }

        }
    }
}
