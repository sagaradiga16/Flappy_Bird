import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception{
        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("Flappy Bird");
        //frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create an instance of Flappy_bird
        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack(); // If you don't use frame.pack(),
        // the width and height of the title bar would've been taken into account
        // we want it to be 360x640 excluding the title bar
        flappyBird.requestFocus();
        frame.setVisible(true);

    }
}