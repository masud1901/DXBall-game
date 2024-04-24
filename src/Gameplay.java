import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 48;
    private Timer timer;

    private int playerX = 310;
    private int ballPositionX = 120;
    private int ballPositionY = 350;
    private int ballDirectionX = -1;
    private int ballDirectionY = -2;
    private final String font = "serif";


    private MapGenerator map;

    @Override
    public void paint(Graphics g) {
        renderBackground(g);
        renderMap(g);
        renderBorders(g);
        renderScore(g);
        renderPaddle(g);
        renderBall(g);
        renderGameOverMessage(g);
        renderRestartMessage(g);
        g.dispose();
    }

    private void renderBackground(Graphics g) {
        g.setColor(Color.darkGray);
        g.fillRect(1, 1, 692, 592);
    }

    private void renderMap(Graphics g) {
        map.draw((Graphics2D) g);
    }

    private void renderBorders(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(692, 0, 3, 592);
    }

    private void renderScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font(font, Font.BOLD, 25));
        g.drawString("" + score, 590, 30);
    }

    private void renderPaddle(Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(playerX, 550, 100, 8);
    }

    private void renderBall(Graphics g) {
        g.setColor(Color.yellow);
        g.fillOval(ballPositionX, ballPositionY, 20, 20);
    }

    private void renderGameOverMessage(Graphics g) {
        if (totalBricks <= 0) {
            g.setColor(Color.RED);
            g.setFont(new Font(font, Font.BOLD, 30));
            g.drawString("Mission Completed", 260, 300);
        }
    }

    private void renderRestartMessage(Graphics g) {
        if (totalBricks <= 0 || ballDirectionY > 570) {
            g.setColor(Color.RED);
            g.setFont(new Font(font, Font.BOLD, 20));
            g.drawString("Press (Enter) to restart the game.", 230, 350);
        }
    }

    //Constructor to pass the initial values
    public Gameplay() {
        map = new MapGenerator(4, 10);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        initializeGame();

    }

    private void initializeGame() {
        ballPositionX = 120;
        ballPositionY = 350;
        ballDirectionX = -1;
        ballDirectionY = -2;
        playerX = 310;
        score = 0;
        totalBricks = 48;
        map = new MapGenerator(4, 10);
        int delay = 20; // Adjust the delay as needed
        timer = new Timer(delay, this);
        timer.start();
    }


    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE && (!play)) {
            play = true;
            initializeGame();
            repaint();
        }
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }
    public void actionPerformed(ActionEvent e) {
        if (play) {
            handlePaddleCollision();
            checkBrickCollisions();
            updateBallPosition();
            checkWallCollisions();
            checkGameOver();
            repaintGameArea();
        }
    }

    private void handlePaddleCollision() {
        Rectangle ballRect = new Rectangle(ballPositionX, ballPositionY, 20, 20);
        Rectangle paddleRect = new Rectangle(playerX, 550, 100, 8);

        if (ballRect.intersects(paddleRect)) {
            ballDirectionY = -ballDirectionY;
            adjustBallDirectionX(ballRect, paddleRect);
        }
    }

    private void adjustBallDirectionX(Rectangle ballRect, Rectangle paddleRect) {
        double relativeIntersectX = (ballRect.getCenterX()) - (paddleRect.getCenterX());
        ballDirectionX = (int) (relativeIntersectX / 25);
        if (ballDirectionX == 0) ballDirectionX = 2;
        if (ballDirectionY == 0) ballDirectionY = 2; // Ensure ballDirectionY is never zero
    }

    private void checkBrickCollisions() {
        Rectangle ballRect = new Rectangle(ballPositionX, ballPositionY, 20, 20);

        for (int i = 0; i < map.map.length; i++) {
            for (int j = 0; j < map.map[0].length; j++) {
                if (map.map[i][j] > 0) {
                    Rectangle brickRect = getBrickRectangle(i, j);

                    if (ballRect.intersects(brickRect)) {
                        handleBrickCollision(brickRect);
                        return;
                    }
                }
            }
        }
    }

    private Rectangle getBrickRectangle(int i, int j) {
        int brickX = j * map.brickWidth + 80;
        int brickY = i * map.brickHeight + 50;
        return new Rectangle(brickX, brickY, map.brickWidth, map.brickHeight);
    }

    private void handleBrickCollision(Rectangle brickRect) {
        int i = (brickRect.y - 50) / map.brickHeight; // Calculate the row of the brick
        int j = (brickRect.x - 80) / map.brickWidth; // Calculate the column of the brick
        map.setBrickValue(0, i, j);
        score += 5;
        totalBricks--;

        if (ballDirectionX == 0) {
            ballDirectionY = -ballDirectionY; // Invert the y-direction
        } else {
            double relativeIntersectX = (ballPositionX + 10) - (brickRect.getCenterX());
            double relativeIntersectY = (ballPositionY + 10) - (brickRect.getCenterY());
            double angle = Math.atan2(relativeIntersectY, relativeIntersectX);
            ballDirectionX = (int) (Math.cos(angle) * 2);
            ballDirectionY = (int) (Math.sin(angle) * 2);
            if (ballDirectionY == 0) ballDirectionY = 2; // Ensure ballDirectionY is never zero
        }
    }


    private void updateBallPosition() {
        ballPositionX += ballDirectionX;
        ballPositionY += ballDirectionY;
    }

    private void checkWallCollisions() {
        if (ballPositionX < 0 || ballPositionX > 670) {
            ballDirectionX = -ballDirectionX;
        }
        if (ballPositionY < 0) {
            ballDirectionY = -ballDirectionY;
        }
    }

    private void checkGameOver() {
        if (ballPositionY > 570) {
            play = false;
            // TODO: Game over logic
        }
    }

    private void repaintGameArea() {
        SwingUtilities.invokeLater(this::repaint);
    }

}
