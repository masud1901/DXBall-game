import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 48;
    private Timer timer;
    private int delay = 8;

    private int playerX = 310;
    private int ballPositionX = 120;
    private int ballPositionY = 350;
    private int ballDirectionX = -1;
    private int ballDirectionY = -2;


    private MapGenerator map;


    //Constructor to pass the initial values
    public Gameplay() {
        map = new MapGenerator(4, 12);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();


    }


    public void paint(Graphics g) {
//        background
        g.setColor(Color.darkGray);
        g.fillRect(1, 1, 692, 592);

//        drawing map
        map.draw((Graphics2D) g);


//        borders
        g.setColor(Color.PINK);

        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(692, 0, 3, 592);

//        scores
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

//        paddle
        g.setColor(Color.MAGENTA);
        g.fillRect(playerX, 550, 100, 8);

//        the ball
        g.setColor(Color.yellow);
        g.fillOval(ballPositionX, ballPositionY, 20, 20);

//        when one wins the game
        if (totalBricks <= 0) {
            play = false;
            ballDirectionX = 0;
            ballDirectionY = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Mission Completed", 260, 300);
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press (Enter) to restart the game.", 230, 350);
        }

//        when one loose the game
        if (ballDirectionY > 570) {
            play = false;
            ballDirectionY = 0;
            ballDirectionX = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("--Game Over--,Score: " + score, 260, 300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press (Enter) to restart the game.", 230, 350);
        }
        g.dispose();
    }
}
