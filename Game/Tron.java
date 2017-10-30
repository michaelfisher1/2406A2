import java.awt.EventQueue;
import javax.swing.JFrame;

public class Tron extends JFrame
{
    public Tron()
    {
        add(new Board());
        setResizable(false);
        pack();

        setTitle("Tron 1.0");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run() {
                JFrame ex = new Tron();
                ex.setVisible(true);
            }
        });
    }
}
