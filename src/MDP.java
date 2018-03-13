/***
 * Represents the UAV MDP as a tuple (states, actions, transition, reward)
 * The actions are hard-coded for a GridWorld agent
 * States are represented by a GridWorld object (which has a list of states in the world)
 * Actions are represented by a Java enum
 * Transition probabilities are specified in the constructor.  With probability p, the agent will move in its intended
 *      direction.  With probability (1-p) / 2 the agent will move in one of the directions perpendicular.
 * The reward function is set when creating states.  Each state has a property "reward" which will be given to the agent
 *      when the agent enters that state from some OTHER state
 */
public class MDP {
    // states are represented by a GridWorld object
    private GridWorld gridworld;
    private double move_success_prob;  // the probability movement will be successful in its intended direction

    // actions that the UAV can take
    public enum ACTION {
        NORTH, SOUTH, EAST, WEST
    }

    /**
     * The transition function for the UAV MDP.
     * Maps state-action pairs to a vector of state probabilities
     * @param s the current state
     * @param a the action taken in state s
     * @return a double[] with an element for each state in the GridWorld.  The value at position i will be the
     *      probability of transitioning to state i from state s using action a
     */
    public double[] transition(State s, ACTION a) {
        // the agent's position in State s
        int x = s.x;
        int y = s.y;

        // the agent's "intended" direction
        int intended_x = x;
        int intended_y = y;

        // the directions perpendicular to the intended direction
        int ortho_x1 = x;
        int ortho_y1 = y;
        int ortho_x2 = x;
        int ortho_y2 = y;

        switch(a){
            case NORTH:
                intended_y += 1;
                ortho_x1 -= 1;
                ortho_x2 += 1;

            case SOUTH:
                intended_y -= 1;
                ortho_x1 -= 1;
                ortho_x2 += 1;

            case EAST:
                intended_x += 1;
                ortho_y1 -= 1;
                ortho_y2 += 1;

            case WEST:
                intended_x -= 1;
                ortho_y1 -= 1;
                ortho_y2 += 1;
        }

        double[] probabilities = new double[this.gridworld.states.length];  // array of zeros for each state

        // handle probabilities of transitioning in the intended direction
        int intended_cell_idx;
        if(this.gridworld.checkReachable(intended_x, intended_y)) {
            // if the intended direction of movement is reachable, then we add 0.85 to the probability for that state
            intended_cell_idx = gridworld.calculateIndex(intended_x, intended_y);
        } else {
            // if the intended cell isn't reachable, then the agent will "bounce back" to its current position
            intended_cell_idx = gridworld.calculateIndex(x, y);
        }
        probabilities[intended_cell_idx] += this.move_success_prob;

        // handle probabilities of transitioning in the perpendicular directions
        int ortho_idx;
        double prob = (1 - this.move_success_prob) * 0.5;
        if(this.gridworld.checkReachable(ortho_x1, ortho_y1)) {
            // if the first perpendicular direction is reachable, then we add (1 - p) / 2 to its probability
            ortho_idx = gridworld.calculateIndex(ortho_x1, ortho_y1);
        } else {
            // if the intended cell isn't reachable, then the agent will "bounce back" to its current position
            ortho_idx = gridworld.calculateIndex(x, y);
        }
        probabilities[ortho_idx] += prob;

        // the other orthogonal direction
        if(this.gridworld.checkReachable(ortho_x2, ortho_y2)) {
            ortho_idx = gridworld.calculateIndex(ortho_x2, ortho_y2);
        } else {
            ortho_idx = gridworld.calculateIndex(x, y);
        }
        probabilities[ortho_idx] += prob;

        // the array now has correct transition probabilities for each state
        return probabilities;
    }

    /**
     * Reward function for the MDP
     * Rewards are specified for each state.  The reward is given if the agent transitions from an old state into
     *      a DIFFERENT new state
     * For this particular problem, the action has no bearing on the
     * @param s the old state
     * @param a the action taken
     * @param s_new the new state
     * @return the reward obtained from the (s, a, s') tuple
     */
    public double reward(State s, ACTION a, State s_new){
        if(s.equals(s_new)) {
            return 0;
        } else {
            return s_new.reward;
        }
    }

    /**
     * Constructor for the MDP
     * @param gridWorld the GridWorld object specifying the states and rewards for this MDP
     * @param success_prob the probability of successfully moving in the intended direction
     */
    public MDP(GridWorld gridWorld, double success_prob){
        this.gridworld = gridWorld;
        this.move_success_prob = success_prob;
    }
}
