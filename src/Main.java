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

        State t1 = world.getState(0, 0);
        State t2 = world.getState(2, 3);
        State t3 = world.getState(1, 1);

        System.out.println(t2);
        double[] probs = mdp.transition(t2, MDP.ACTION.EAST);
        for(int i=0; i < probs.length; i++){
            System.out.println("state " + i + ": " + probs[i]);
        }
        System.out.print("\n");


    }
}
