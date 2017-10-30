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
    private final int dot_size = 10;
    private final int all_dots = 900;
    private final int random_position = 29;
    private final int delay = 140;

    private final int x[] = new int[all_dots];
    private final int y[] = new int[all_dots];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image trail;
    private Image apple;
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

        ImageIcon apple_image = new ImageIcon("images/smallapple.png");
        apple = apple_image.getImage();

        ImageIcon bike_image = new ImageIcon("images/smalldot.png");
        bike = bike_image.getImage();
    }

    private void initGame()
    {
        dots = 3;
        for (int z = 0; z < dots; z++)
        {
            x[z] = 50 - z *10;
            y[z] = 50;
        }

        locateApple();

        timer = new Timer(delay, this);
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
            g.drawImage(apple, apple_x, apple_y, this);
            for (int z = 0; z < dots; z++)
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

    private void checkApple()
    {
        if ((x[0] == apple_x) && (y[0] == apple_y))
        {
            dots++;
            locateApple();
        }
    }
    private void move()
    {
        for (int z = dots; z > 0; z--)
        {
            x[z] = x[(z-1)];
            y[z] = y[(z-1)];
        }
        if (leftDirection)
        {
            x[0] -= dot_size;
        }
        if (rightDirection)
        {
            x[0] += dot_size;
        }
        if (upDirection)
        {
            y[0] -= dot_size;
        }
        if (downDirection)
        {
            y[0] += dot_size;
        }
    }

    private void checkCollision()
    {
        for (int z = dots; z > 0; z --)
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
    private void locateApple()
    {
        int r = (int) (Math.random()*random_position);
        apple_x = ((r*dot_size));

        r = (int) (Math.random()* random_position);
        apple_y = ((r*dot_size));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(inGame)
        {
            checkApple();
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
        }
    }
}
