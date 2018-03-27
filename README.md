# CSCI 8920 - Policy Iteration

Policy iteration for grid worlds implemented with Java 8 and the JAMA matrix package.

# Instructions
This program requires Java 8 and the Java 8 SDK to be installed.  The following instructions assume that the `java` and
`javac` programs are available in the command prompt.

The program also depends on the JAMA matrix library to solve the system of equations required to evaluate a policy.  A
recent version of JAMA is included in the `lib/` directory.

1. Unzip the `policy_iteration.zip` archive to a directory of your choice.

2. Change to the `policy_iteration` directory

`cd policy_iteration`

3. Compile the program with the following command:

`javac -classpath ./src:./lib/Jama.jar ./src/Main.java -d ./out`

4. Run the program with the following command:

`java -cp ./out:./lib/Jama.jar Main`

You should see the converged policy as output in the terminal.

# Output

 Converged Policy:
State (x, y) |  Action |
-------------|---------|
      (0, 0) |       E |
      (1, 0) |       E |
      (2, 0) |       E |
      (3, 0) |       S |
      (0, 1) |       E |
      (1, 1) |       E |
      (2, 1) |       E |
      (3, 1) |       S |
      (0, 2) |       S |
      (1, 2) |     N/A |
      (2, 2) |       N |
      (3, 2) |       N |
      (0, 3) |       E |
      (1, 3) |       E |
      (2, 3) |       E |
      (3, 3) |       N |

Visualization as Grid:
| E | E | E | N |
| S |   | N | N |
| E | E | E | S |
| E | E | E | S |


# Note on the output:

This program (and its output) refer to states in the grid by the (x, y) coordinates, where the origin is in the bottom-left.
For example, the bottom-left cell is (0, 0).  The bottom-right cell is (3, 0).  The top-right is (3, 3).

Note that the state (1, 2) is unreachable and thus the policy does not output an action for the state.

To maximize clarity, the program outputs the policy as both a table (mapping states to actions) and also a grid
(showing the action for each state in the grid)

I've also included a PNG that shows the converged policy with arrows (converged_policy.png)

# Trajectory

If the agent starts from state (0, 0) and follows the policy, then the expected trajectory is:
(0, 0) -> (1, 0) -> (2, 0) -> (3, 0)

The trajectory does indeed lead to the green sector in the bottom right corner (3, 0).


# Other notes

In this program, I have treated the green and red sectors as if they are NOT final states.  (I asked about this on eLC,
but received no response)
I have also formulated the reward function such that the agent receives the reward *anytime* it enters a state.

The converged policy might be slightly different if these assumptions were modified.  For example, it might be the case
that the agent can only receive the award from one of the green sectors once (this was somewhat ambiguous in the
wording of the assignment).
