import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Sayan Faraz on 2016-01-22.
 */
public class Engine {
    // VARIABLES
    private ArrayList<Obstacle> obstacleStack; // Arraylist of obstacles
    FlappyBird flappyBird; // FlappyBird
    int windowHeight;
    int windowWidth;

    // CONSTRAINTS
    // Starting array of constraints, 3 parameters: x-gap, min-height,
    // max-height
    private static int[] beginning_constraints = new int[]{200, 50, 600};
    private static int y_gap = 400;
    private static int starting_obstacle_pos = 300;

    // beginning constraints for obstacles
    private static ArrayList<int[]> colour_wheel = new ArrayList<>(Arrays
            .asList(new int[]{241, 101, 76}, // orange
                    new int[]{34, 77, 130}, // blue
                    new int[]{187, 54, 88}, // red
                    new int[]{50, 109, 101}, // green
                    new int[]{95, 55, 194})); // purple

    public Engine(int windowWidth, int windowHeight) {

        // Init vars
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;

        // Populate obstacle stack
        obstacleStack = new ArrayList<>();
        beginning_constraints[2] = (int) (windowHeight * 0.4);
        this.populateObstacleStack(windowWidth, beginning_constraints);

        // Make Flappy bird
        flappyBird = new FlappyBird();
    }

    public Engine reset() {
        return new Engine(windowWidth, windowHeight);
    }

    // GETTERS, SETTERS---------------------------------------------------------

    public ArrayList<Obstacle> getObstacleStack() {
        return obstacleStack;
    }

    /**
     * Set obstacle stack to ArrayList of obstacles given to function.
     *
     * @param obstacleStack {ArrayList<Obstacle>} Obstacle stack
     */
    public void setObstacleStack(ArrayList<Obstacle> obstacleStack) {
        this.obstacleStack = obstacleStack;
    }

    /**
     * Get engine's flappy bird.
     * @return {FlappyBird}
     */
    public FlappyBird getFlappyBird() {
        return flappyBird;
    }

    /**
     * Set flappy bird for this engine.
     * @param flappyBird {FlappyBird} Flappy bird object
     */
    public void setFlappyBird(FlappyBird flappyBird) {
        this.flappyBird = flappyBird;
    }

    // METHODS------------------------------------------------------------------
    /**
     * Given parameters, make a random obstacle.
     *
     * @param small_x      {int} Minimum x value
     * @param large_x      {int} Maximum x value
     * @param small_height {int} Minimum height
     * @param large_height {int} Maximum height
     * @return {Obstacle} Random obstacle
     */
    public Obstacle makeObstacle(int small_x, int large_x,
                                 int small_height, int large_height,
                                 boolean orientation) {
        // CHOOSE RANDOM STATS
        // Random position
        int rand_x = (int) (Math.random() * (large_x - small_x)) + small_x;

        // Random width, height
        int rand_height = (int) (Math.random() * (large_height - small_height)
                + small_height);
        System.out.println("Height = " + rand_height);
        int rand_width = (int) (Math.random() * 100 + 50);

        // Random Color
        int rand_color = (int) (Math.random() * 5);

        // INIT OBSTACLE BASED ON RANDOM STATS
        return new Obstacle(rand_x, orientation, rand_height,
                rand_width, Engine.colour_wheel.get(rand_color));
    }

    /**
     * Given an obstacle, make another obstacle on the same x pos but with
     * opposite orientation.
     *
     * @param obstacle {Obstacle}
     * @return {ArrayList<Obstacle>} Pair of obstacles
     */
    public ArrayList<Obstacle> makeObstaclePair(Obstacle obstacle) {

        // Init vars
        ArrayList<Obstacle> ret_array = new ArrayList<>();
        Obstacle ret_obstacle;

        // Random Color
        int rand_color = (int) (Math.random() * 5);

        ret_obstacle = new Obstacle(obstacle.getXpos(),
                !obstacle.isOrientatedUp(),
                this.windowHeight - obstacle.getHeight() - y_gap,
                obstacle.getWidth(),
                Engine.colour_wheel.get(rand_color));

        ret_array.add(obstacle);
        ret_array.add(ret_obstacle);

        return ret_array;
    }

    /**
     * Given number of obstacles, list of parameters, return a list of random
     * Obstacles.
     *
     * @param num_of_obstacles {int} Number of obstacles
     * @param beginning_pos {int} What x pos to start making obstacles from
     * @param constraints      {int[]} Array of constraints, 3 parameters:
     *                         x-gap, min-height, max-height
     * @return {ArrayList} List of obstacles
     */
    public ArrayList<Obstacle> makeMultipleObstacles
    (int num_of_obstacles, int beginning_pos, int[] constraints) {

        // Check if constraints is formatted properly
        if (isFormatConstraints(constraints)) {

            // Set current_pos
            int current_pos = beginning_pos;

            // Create obstacles array list
            ArrayList<Obstacle> list_of_Obstacles = new ArrayList<>();

            // Create obstacles
            // Each coordinate will
            for (int i = 0; i < num_of_obstacles; i++) {
                // Need to make sure that each block doesn't overlap with next
                // So add smallest_x to current_pos

                Obstacle new_obstacle = makeObstacle(current_pos,
                        constraints[0] + current_pos,
                        constraints[1],
                        constraints[2], true);

                ArrayList<Obstacle> obstacle_pair = makeObstaclePair
                        (new_obstacle);

                list_of_Obstacles.add(0, obstacle_pair.get(0));
                list_of_Obstacles.add(0, obstacle_pair.get(1));

                // New current_pos will be at end of prev block
                current_pos = list_of_Obstacles.get(0).getXpos() +
                        list_of_Obstacles.get(0).getWidth();
            }

            // Return Constraints list
            return list_of_Obstacles;
        } else
            throw new IllegalArgumentException("You need to enter 4 integer " +
                    "parameters to make an Obstacle.");
    }

    /**
     * Given number of obstacles, list of parameters, add obstacles to
     * obstacle_stack
     *
     * @param num_of_obstacles {int} Number of obstacles
     * @param constraints      {int[]} Array of constraints, 3 parameters:
     *                         x-gap, min-height, max-height
     */
    public void addMultipleObstacles(int num_of_obstacles, int beginning_pos,
                                     int[] constraints) {
        // Create list of obstacles
        try {
            ArrayList<Obstacle> new_obstacles = makeMultipleObstacles
                    (num_of_obstacles, beginning_pos, constraints);

            for (int i = 0; i < num_of_obstacles * 2; i++)
                this.obstacleStack.add(0, new_obstacles.get(i));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("You need to enter 3 integer " +
                    "parameters to make an Obstacle.");
        }
    }

    /**
     * Given window width, list of parameters, add obstacles to obstacle_stack
     * until window is full
     *
     * @param window_width {int} Window width
     * @param constraints  {int[]} Array of constraints, 3 parameters: x-gap,
     *                     min-height, max-height
     */
    public void populateObstacleStack(int window_width, int[]
            constraints) {
        // Case 1: obstacle list is empty
        if (this.obstacleStack.isEmpty()) {
            // Add first obstacle
            addMultipleObstacles(1, starting_obstacle_pos, constraints);

        }

        // Now either way, obstacle list isn't empty
        // So add obstacles until last obstacle's outside of window
        int last_pos = obstacleStack.get(0).getXpos() + obstacleStack.get
                (0).getWidth();  // right-most edge of last obstacle
        while (last_pos < window_width) {
            // Add an obstacle
            addMultipleObstacles(1, last_pos, constraints);

            // Update last pos
            last_pos = obstacleStack.get(0).getXpos() + obstacleStack.get
                    (0).getWidth();
        }
    }

    /**
     * Move obstacles in obstacle stack to the left by specified amount
     * @param amountToMove {int} Amount to move obstacles by
     */
    public void moveObstacleStackToLeft(int amountToMove) {
        for (Obstacle obstacle :
                obstacleStack) {
            obstacle.setXpos(obstacle.getXpos() - amountToMove);
        }
    }

    /**
     * Delete any obstacles that are off the screen. Push a new obstacle
     * every time an obstacle is deleted
     */
    public void deleteOffScreenObstacles() {
        // VARS
        Iterator<Obstacle> obstacleIterator = obstacleStack.iterator();
        int num_of_obstacles_to_add = 0;

        while (obstacleIterator.hasNext()) {
            // Get next obstacle to check
            Obstacle obstacle = obstacleIterator.next();

            // Check if obstacle is out of screen
            if (obstacle.getXpos() + obstacle.getWidth() < -1) {
                // If it is, remove it and remember to add new obstacle later
                obstacleIterator.remove();
                num_of_obstacles_to_add++;
            }
        }

        // Add all the new obstacles needed
        addMultipleObstacles(num_of_obstacles_to_add, obstacleStack.get(0).getXpos() +
                obstacleStack.get(0).getWidth(), beginning_constraints);

        //TODO: Add a 'greatest x pos' variable so that we know where
        // the last block is


//        for(Obstacle obstacle: obstacleStack) {
//            if(obstacle.getXpos() + obstacle.getWidth() < -1) {
//                obstacleStack.remove(obstacle);
//                addMultipleObstacles(1, obstacleStack.get(0).getXpos() +
//                        obstacleStack.get(0).getWidth(), beginning_constraints);
//
//                //TODO: Add a 'greatest x pos' variable so that we know where
//                // the last block is
//            }
//        }
    }


    /**
     * Return true if flappy bird has crashed; else return false.
     * @return {boolean} Crashed or not?
     */
    public void detectFlappyBirdCrashes() {

        // If collision with any obstacle, kill the bird
        for (Obstacle obstacle :
                obstacleStack) {

            if (flappyBird.insideObstacle(obstacle, y_gap)) {
                flappyBird.kill();
            }
        }
    }
    // HELPER FUNCTIONS---------------------------------------------------------

    /**
     * Checks if constraints array is formatted properly.
     *
     * @param constraints {int[]} Array of constraints
     * @return {boolean}
     */
    private static boolean isFormatConstraints(int[] constraints) {
        // Is length=3? Is gap > 0? Is small_height < large_height?
        return constraints.length == 3 && constraints[0] > 0 &&
                constraints[1] < constraints[2];
    }
}
