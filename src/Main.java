import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) {

        // create a GridWorld and add states for this problem
        GridWorld world = new GridWorld(4, 4);
        try {
            world.addState(0, 0);
            world.addState(1, 0);
            world.addState(2, 0);
            world.addState(3, 0, true, 1); // "good" state
            world.addState(0, 1);
            world.addState(1, 1);
            world.addState(2, 1);
            world.addState(3, 1);
            world.addState(0, 2);
            world.addState(1, 2, false, -0.05); // unreachable state
            world.addState(2, 2);
            world.addState(3, 2);
            world.addState(0, 3, true, -1);  // "bad" state
            world.addState(1, 3);
            world.addState(2, 3);
            world.addState(3, 3, true, 1); // "good" state
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }

        // Create the MDP and the initial randomized policy
        MDP mdp = new MDP(world, 0.85, 0.99);
        Policy p = new Policy(world);  // random policy

        // begin policy iteration
        boolean policyChanged;
        do {
            policyChanged = false;
            // evaluate the policy
            double[] evaluation = p.evaluate(mdp);
            // for each state
            for(State s: world.states) {
                // select the best action
                double bestVal = Integer.MIN_VALUE;
                MDP.ACTION bestAction = null;
                for (MDP.ACTION a : MDP.ACTION.values()) {
                    double[] transition_prob = mdp.transition(s, a);
                    double q = 0;
                    double future = 0;
                    for(State j: world.states) {
                        q += transition_prob[j.index] * mdp.reward(s, a, j);
                        future += transition_prob[j.index] * evaluation[j.index];
                    }
                    future = mdp.discount * future;
                    double val = q + future;
                    if(val > bestVal){
                        bestVal = val;
                        bestAction = a;
                    }
                }

                // update the policy
                if(p.getAction(s) != bestAction) {
                    policyChanged = true;
                    p.updateAction(s, bestAction);
                }
            }
        } while(policyChanged);  // loop until the policy stabilizes

        // print out the policy
        PrintWriter pw = new PrintWriter(System.out);
        pw.println("\n Converged Policy:");
        pw.printf("%12s | %7s |\n", "State (x, y)", "Action");
        pw.println("-------------|---------|");
        for(State s : world.states) {
            if(s.reachable) {
                pw.printf("%12s | %7s |\n", s, MDP.actionToString(p.getAction(s)));
            } else {
                pw.printf("%12s | %7s |\n", s, "N/A");
            }
        }

        pw.println("\nVisualization as Grid:");
        for(int y=world.vertical_cells-1; y>=0; y--){
            pw.print("|");
            for(int x=0; x < world.horizontal_cells; x++){
                State s = world.getState(x, y);
                if(s.reachable){
                    pw.print(" " + MDP.actionToString(p.getAction(s)) + " |");
                } else{
                    pw.print("   |");
                }
            }
            pw.print("\n");
        }
        pw.flush();
    }
}
