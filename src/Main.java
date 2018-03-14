public class Main {

    public static void main(String[] args) {

        // create a GridWorld and add states for this world
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

        MDP mdp = new MDP(world, 0.85, 0.99);
        Policy p = new Policy(world);  // random policy

        // begin policy iteration
        boolean policyChanged; // terminate when the policy stops changing
        MDP.ACTION[] prev_action = new MDP.ACTION[world.states.length]; // maintain list of previous actions to prevent cycles
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
                // if the best action is an improvement and if it doesn't cause an endless cycle
                if(p.getAction(s) != bestAction && bestAction != prev_action[s.index]){
                    prev_action[s.index] = p.getAction(s);
                    System.out.println(s);
                    System.out.println("Swapping action " + MDP.actionToString(p.getAction(s)) + " for action " + MDP.actionToString(bestAction));
                    double val_sum = 0;
                    for(Double d : evaluation){val_sum += d;}
                    System.out.println("Eval: " + val_sum);
                    policyChanged = true;
                    p.updateAction(s, bestAction);
                }
            }
        } while(policyChanged);

        // print out the policy
        double[] evaluation = p.evaluate(mdp);
        for(State s : world.states) {
            System.out.println(s);
            System.out.println("Policy: " + MDP.actionToString(p.getAction(s)));
            System.out.println("EU : " + evaluation[s.index]);
        }

    }
}
