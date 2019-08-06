import java.util.Arrays;

/**
 * Tests the dynamic vs static quantum values
 * Modify the properties below
 */
public class Main {

    // Modify the following fields -------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    // Specify the values to generate the 3D data structure
    private static int numOfProcesses   = 20;
    private static int totalBurstTime   = 20;
    private static int longestBurstTime = 20;

    // Specify the static quantum which you want to test against
    private static int staticQuantumValue = 5;

    // Specify the total number of processes in the queue, total burst time and longest burst time in the queue.
    private static int[][] queues = {
            {4,  19, 4},
            {5,  17, 3},
            {6,  12, 5},
            {7,  13, 2},
            {8,  18, 3},
            {9,  16, 2},
            {16, 19, 3},
            {16, 19, 4}
    };

    // End Modify the following Fields ---------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Populates the 3D array with optimized Dynamic Quantum values.
     * Calls test() method to get average waiting times for the queues specified above and display the results.
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Dynamic Quantum Time Simulation");
        System.out.println("The following code simulates a Round Robin Ready Queue.");
        System.out.println("It displays the average waiting times of the queue for both the dynamic and static quanta");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("");

        QuantumTime quantumTime = new QuantumTime();

        // Populate the 3D array with optimized Dynamic Quantum values
        quantumTime.populateOptimizedQuanta(numOfProcesses, totalBurstTime, longestBurstTime);

        for ( int[] queue : queues ) {

            int[] randProcess = quantumTime.generateRandomQueue( queue[0], queue[1], queue[2] );

            test( randProcess, queue[0], queue[1], queue[2], quantumTime );
        }
    }

    /**
     * Displays the average waiting times for both the static quantum and the dynamic quantum values
     *
     * @param queue
     * @param numOfProcesses
     * @param totalQueueBurstTime
     * @param longestProcessTime
     * @param quantumTime
     */
    private static void test( int[] queue, int numOfProcesses, int totalQueueBurstTime, int longestProcessTime, QuantumTime quantumTime ) {
        int bestQuantum   = quantumTime.getQuantum( numOfProcesses, totalQueueBurstTime, longestProcessTime );

        System.out.println("Test");
        System.out.println("-----------------------------------------------------------");
        System.out.println("Queue: " + Arrays.toString(queue) );
        System.out.println("");
        System.out.println("Number of Processes: " + numOfProcesses );
        System.out.println("Total Burst Time: " + totalQueueBurstTime );
        System.out.println("Longest Process Time: " + queue[0] );
        System.out.println("Optimum Quantum: " + bestQuantum );
        System.out.println("Static Quantum: " + staticQuantumValue );
        System.out.println("");

        System.out.println( "Average Waiting Time for dynamic quantum " + quantumTime.getAverageWaiting(queue, bestQuantum) );
        System.out.println( "Average Waiting Time for static quantum "  + quantumTime.getAverageWaiting(queue, staticQuantumValue) );
        System.out.println("-----------------------------------------------------------");
        System.out.println("");
    }
}