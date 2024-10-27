import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; //To store all the pipes in the game
import java.util.Random; // To place our pipes in random positions
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener{
    int boardWidth = 360;
    int boardHeight = 640;

    // Images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    // Bird
    int birdX = boardHeight/8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;


    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img){
            this.img = img;
        }
    }
    // Pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false; // to check if the bird has passed the pipe yet

        Pipe(Image img){
            this.img = img;
        }
    }

    // game logic
    Bird bird;
    int velocityX = -6; // moves pipe to the left (simulates pipe moving to right)
    int velocityY = 0; // bird moving in vertical upward direction
    int gravity = 1; // the bird slows down every frame by 1 pixel

    // since we have many pipes, we need to store them in a list
    ArrayList<Pipe> pipes;
    Random random = new Random();


    Timer gameLoop;
    Timer placePipesTimer;

    boolean gameOver = false;
    double score = 0;

    // create constructors
    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        // Loading Images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        // bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        // Place pipes Timer
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipesTimer.start();

        // game timer
        gameLoop = new Timer(1000/60, this);
        // draws the frame 60 times per second
        gameLoop.start();
    }
    public void placePipes(){
        // Math.random gives a value between 0 and 1, multiply it by pipe height/2
        // It gives a random number between 0 and 256
        // 0 - 128 - (0 to 256)
        // ranges from 1/4th to 3/4 th the pipe height
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * pipeHeight/2);
        int openingSpace = boardHeight/4;

        Pipe toppipe = new Pipe(topPipeImg);
        toppipe.y = randomPipeY;
        pipes.add(toppipe);

        Pipe bottompipe = new Pipe(bottomPipeImg);
        bottompipe.y = toppipe.y + pipeHeight + openingSpace;
        pipes.add(bottompipe);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g); // super refers to the parent class JPanel
        draw(g);
    }

    public void draw(Graphics g){
        // background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        // bird
        g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);

        // pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
        g.setColor(Color.white);
        g.setFont(new Font("Montserrat", Font.BOLD, 26));
        if (gameOver){
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        }
        else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }
    public void move(){
        // Update all the moves

        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        //pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;
            if (!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5;
            }

            if (collision(bird, pipe)){
                gameOver = true;
            }
        }
        if (bird.y > boardHeight){
            gameOver = true;
        }

    }
    public boolean collision(Bird a, Pipe b){
        return a.x < b.x + b.width && // a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x && // a's top right corner passes b's top left corner
               a.y < b.y + b.height && // a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y; // a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -10;
            if (gameOver){
                // restart the game by resetting the conditions
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
