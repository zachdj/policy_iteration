/**
 * The State class is used to represent states in the MDP
 */
public class State{
    public int index;  // unique integer identifying the object
    public int x;  // gives the location of the grid cell in the West-to-East direction
    public int y;  // gives the location of the grid cell in the South-to-North direction
    public boolean reachable;
    public double reward;

    /**
     * Create a new State
     * @param index a unique identifier for this state
     * @param x the horizontal location of the state in the grid (zero-indexed)
     * @param y the vertical location of the state in the grid (zero-indexed)
     */
    public State(int index, int x, int y){
        this.index = index;
        this.x = x;
        this.y = y;
        this.reachable = true;
        this.reward = -0.05;
    }

    public boolean equals(Object other){
        if(other instanceof State){
            State o = (State)other;
            return o.index == this.index;
        } else{
            return false;
        }
    }
}
