import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.lang.*;
import java.io.IOException;

/**
 * Created by Sayan Faraz on 2016-01-11.
 */
public class Game extends JPanel implements MouseListener {

    private int game_play; // 0 for home screen, 1 for play, 2 for pause
    private boolean entering_game_play; // new game?

    public Game() {
        super();

        game_play = 0; // init at home screen
        entering_game_play = true; // on play press, will make new game
    }

    // INITIALIZE UI
    private void initUI(JFrame jframe, JPanel jpanel) {

        // Set JFrame Appearance
        jframe.setTitle("Flappy Bird"); // title
        jframe.setSize(1500, 1000); // size of window
        jframe.setLocationRelativeTo(null);
        jframe.setBackground(new Color(194, 217, 239)); // background colour;

        // Set JFrame Behaviour
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit on close

        // Add JPanel
        jframe.setContentPane(jpanel);
        jframe.setVisible(true);

        // Add Mouse Listener
        addMouseListener(this);
    }

    private void homeScreen(Graphics graphics) {
        // Define BufferedImages
        BufferedImage home_screen_img = null;
        BufferedImage play_button_img = null;

        // Try to load the images; return exception if error
        try {
            home_screen_img = ImageIO.read(new File("src" + File.separator +
                    "resources" + File.separator
                    + "Home Page Background.png"));
            play_button_img = ImageIO.read(new File("src" + File.separator +
                    "resources" + File.separator
                    + "Home Page Play Button.png"));

            // Draw the background, play button if loading successful
            graphics.drawImage(home_screen_img, 0, 0, null);

            graphics.drawImage(play_button_img, 512, 719, null);

        } catch (IOException e) {
            System.out.print("Home Page exception handled");
        }
    }

    // GRAPHICS ENGINE
    public void paintComponent(Graphics graphics) {

        // Home Screen, Play, Pause
        switch (game_play) {
            // Home Screen
            case 0:
                entering_game_play = true;
                homeScreen(graphics);
                break;
            case 1:
                // New game? Need a fresh canvas to draw on
                if (entering_game_play) {
                    graphics.setColor(new Color(194, 217, 239));
                    graphics.fillRect(0, 0, 1500, 1000);
                    System.out.print("Color blue");

                    // Game started, so set entering_game_play to false so that
                    // canvas isn't redrawn
                    entering_game_play = false;
                }
                gameEngine(graphics);
                break;
            // Pause
            case 2:
                gamePause();
        }
    }

    // GAME ENGINE--------------------------------------------------------------

    private void gameEngine(Graphics graphics) {
        BufferedImage bird_img = null;

        // Load bird img
        try {
            bird_img = ImageIO.read(new File("src" + File.separator +
                    "resources" + File.separator + "bird.png"));
        } catch (IOException e) {
            System.out.print("Bird exception handled");
        }

        // Draw bird img
        int[] scaled_parameters = scaleBirdInts(10);
        graphics.drawImage(bird_img, 30, 40, scaled_parameters[0],
                scaled_parameters[1], null);
    }

    private void gamePause() {
        game_play = 0;
    }

    // MOUSE LISTENERS----------------------------------------------------------
    public void mousePressed(MouseEvent e) {
        return;
    }

    public void mouseReleased(MouseEvent e) {
        return;
    }

    public void mouseEntered(MouseEvent e) {
        return;
    }

    public void mouseExited(MouseEvent e) {
        return;
    }

    public void mouseClicked(MouseEvent e) {
        // Get coords of mouse click
        int x = e.getX();
        int y = e.getY();

        System.out.print(x + ", " + y + " ");

        // PLAY BUTTON
        // Is home screen?
        if (game_play == 0) {

            // Did mouse press Play button?
            if (x > 512 && x < 989 && y > 719 && y < 829) {
                // Game's starting :O
                game_play = 1;
                repaint();
            }
        }
    }

    // HELPER FUNCTIONS---------------------------------------------------------

    /**
     * Returns scaled dimensions of bird image, given a scale factor.
     *
     * @param scale_factor {int}
     * @return {int[]} {new_width, new_height}
     */
    private int[] scaleBirdInts(int scale_factor) {
        // Init ret list
        int[] ret_list;

        int new_width = 973 / scale_factor;
        int new_height = 782 / scale_factor;

        // Return scaled parameters
        ret_list = new int[]{new_width, new_height};

        return ret_list;
    }

    // MAIN---------------------------------------------------------------------
    public static void main(String[] args) {
        FlappyBird flappyBird = new FlappyBird();
        JFrame Game_frame = new JFrame();
        Game game = new Game();
        game.initUI(Game_frame, game);

        game.setBackground(Color.black);

        System.out.print(flappyBird.getPos()[0]);
    }
}