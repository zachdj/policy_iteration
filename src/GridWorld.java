/**
 * The GridWorld class represents a collection of states for a grid world MDP
 */
public class GridWorld {
    public int horizontal_cells;
    public int vertical_cells;
    public State[] states;

    /**
     * Creates a new GridWorld with the specified size
     * @param x_cells the total number of cells in the horizontal direction
     * @param y_cells the total number of cells in the vertical direction
     */
    public GridWorld(int x_cells, int y_cells) {
        this.states = new State[x_cells * y_cells];
        this.horizontal_cells = x_cells;
        this.vertical_cells = y_cells;
    }

    /**
     * Adds a state to this GridWorld at the specified coordinates
     * By default, the state will be marked as reachable and given a reward of -0.05
     * @param x the horizontal location of the state in the grid
     * @param y the vertical location of the state in the grid
     * @return the State object added to the GridWorld
     * @throws Exception if the location of the cell is outside the bounds of the grid
     */
    public State addState(int x, int y) throws Exception {
        if( x >= this.horizontal_cells || y >= this.vertical_cells || x < 0 || y < 0 ){
            throw new Exception("Tried to add state outside of grid boundaries");
        }

        int index = this.calculateIndex(x, y);
        State s = new State(index, x, y);
        this.states[index] = s;
        return this.states[index];
    }

    /**
     * Adds a state to this GridWorld at the specified coordinates.
     * Allows specifying a non-default reachability and reward
     * @param x the horizontal location of the state in the grid
     * @param y the vertical location of the state in the grid
     * @param reachable boolean indicator of whether the state is reachable or not
     * @param reward the reward achieved when the agent moves into this state from a different state
     * @return the State object added to the GridWorld
     * @throws Exception if the location of the cell is outside the bounds of the grid
     */
    public State addState(int x, int y, boolean reachable, double reward) throws Exception {
        State s = this.addState(x, y);
        s.reachable = reachable;
        s.reward = reward;
        return s;
    }

    /**
     * Checks if a location (x, y) is reachable.
     * A location is reachable if it is within the grid boundaries and not specifically labelled unreachable
     * @param x the coordinate of the location in the West-to-East direction
     * @param y the coordinate of the location in the South-to-North direction
     * @return true if the given location is (theoretically) reachable;  false otherwise
     */
    public boolean checkReachable(int x, int y){
        if( x < 0 || y < 0 || x >= this.horizontal_cells || y >= this.vertical_cells){
            return false;
        } else{
            int index = this.calculateIndex(x, y);
            State queried = this.states[index];
            return queried != null &&  queried.reachable;
        }
    }

    /**
     * Gets the State object at the specified coordinates in the grid
     * @param x the coordinate of the location in the West-to-East direction
     * @param y the coordinate of the location in the South-to-North direction
     * @return the State object at (x,y)
     */
    public State getState(int x, int y){
        int index = this.calculateIndex(x, y);
        return this.states[index];
    }

    /**
     * Calculates a unique index in a grid based on x-y coordinates.  These indicies can be used to flatten the GridWorld
     * into a 1-dimensional array
     * Cell indices start at zero and increase from left to right and from bottom to top
     * e.g. the bottom left cell is 0, the cell to the right is 1.  The cell in the top-right corner would be width*height - 1
     * @param x the coordinate of the location in the West-to-East direction
     * @param y the coordinate of the location in the South-to-North direction
     * @return integer index in the range [0, num_cells - 1] uniquely identifying the grid location
     */
    public int calculateIndex(int x, int y){
        return this.horizontal_cells*y + x;
    }

}
