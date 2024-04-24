import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame obj = new JFrame();
        Gameplay gamePlay = new Gameplay();

        obj.setBounds(10,10,700,600);
        obj.setTitle("DX Ball Ultra");
        obj.setResizable(false);
        obj.setVisible(true);

        obj.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        obj.add(gamePlay);
        gamePlay.requestFocus();
        obj.setVisible(true);
    }
}