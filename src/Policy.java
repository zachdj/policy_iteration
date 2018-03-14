import java.util.Random;
import Jama.Matrix;

/**
 * Represents a policy for an MDP
 * A policy maps each state to an action
 */

public class Policy {
    MDP.ACTION[] policy;  // maps state i to some action
    GridWorld world;  // the world corresponding to this policy

    /**
     * Creates a randomized policy for the specified MDP
     * @param world the GridWorld containing the set of states for this policy
     */
    public Policy(GridWorld world) {
        this.world = world;
        this.policy = new MDP.ACTION[world.states.length];

        // randomize the policy
        Random rand = new Random(0);
        for(int i=0; i < this.policy.length; i++) {
            double num = rand.nextDouble();
            if( num >= 0.75) policy[i] = MDP.ACTION.NORTH;
            else if( num >= 0.50) policy[i] = MDP.ACTION.SOUTH;
            else if( num >= 0.25) policy[i] = MDP.ACTION.EAST;
            else policy[i] = MDP.ACTION.WEST;
        }
    }

    /**
     * Returns the recommended action for the given state
     * @param s the State
     * @return the action recommended by this policy for State s
     */
    public MDP.ACTION getAction(State s) {
        return this.policy[s.index];
    }

    /**
     * Updates the action that this policy recommends for state s
     * @param s the State to change
     * @param newAction the updated action
     */
    public void updateAction(State s, MDP.ACTION newAction) {
        this.policy[s.index] = newAction;
    }

    /**
     * Evaluate this policy
     * Evaluation is accomplished by solving the system of equations:
     *      -q_i = -V_i + beta * sum_j { p_ij * V_j }       for each state i
     *
     * The return value is an array of doubles where position i gives V_i (the value of state i)
     *
     * @param mdp the MDP where this policy should be evaluated
     * @return array of state values V_i
     */
    public double[] evaluate(MDP mdp) {
        // each row in the coefficient matrix represents the coefficients of the V_i
        double[][] coefficient_matrix = new double[this.world.states.length][this.world.states.length];
        // the constant matrix contains the the q-values q_i
        double[] constant_matrix = new double[this.world.states.length];

        for(State s: this.world.states) {
            int idx = s.index;
            MDP.ACTION a = this.getAction(s); // action recommended for state s

            // calculate the immediate expected reward
            double[] transition_prob = mdp.transition(s, a);
            double q = 0;
            for(State j: this.world.states) {
                q += transition_prob[j.index] * mdp.reward(s, a, j);
            }
            constant_matrix[idx] = -q;

            // calculate the coefficients in the discounted value function
            double[] coeff = new double[this.world.states.length];
            coeff[idx] = -1;
            for(State j: this.world.states) {
                coeff[j.index] += mdp.discount * transition_prob[j.index];  // second part of the Bellman value equation
            }
            coefficient_matrix[idx] = coeff;
        }

        // solve the system of equations
        Matrix coefficients = new Matrix(coefficient_matrix);
        Matrix constants = new Matrix(constant_matrix, this.world.states.length); // converts to column vector
        Matrix soln = coefficients.solve(constants);

        // convert the column vector of values to a row vector
        double[] values = new double[this.world.states.length];
        for(State s : this.world.states) {
            values[s.index] = soln.get(s.index, 0);
        }
        return values;
    }

}
