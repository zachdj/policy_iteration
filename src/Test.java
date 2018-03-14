public class Test {

    public static void main(String[] args) {
        // create a GridWorld and add states for this world
        GridWorld world = new GridWorld(4, 3);
        try {
            world.addState(0, 0);
            world.addState(1, 0);
            world.addState(2, 0);
            world.addState(3, 0); // "good" state
            world.addState(0, 1);
            world.addState(1, 1, false, -0.05);
            world.addState(2, 1);
            world.addState(3, 1, true, -1);
            world.addState(0, 2);
            world.addState(1, 2); // unreachable state
            world.addState(2, 2);
            world.addState(3, 2, true, 1);
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }

        MDP mdp = new MDP(world, 0.8, 0.9);
        Policy p = new Policy(world);
        double[] evaluation = p.evaluate(mdp);

        for(State s : world.states) {
            System.out.println(s);
            System.out.println("Policy: " + MDP.actionToString(p.getAction(s)));
            System.out.println("Value: " + evaluation[s.index]);
        }
    }
}
