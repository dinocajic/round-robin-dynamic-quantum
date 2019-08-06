import java.util.Random;

/**
 * QuantumTime
 *
 * Creates the dynamic quantum data structure and retrieves optimized quantum from it
 */
class QuantumTime {

    // The 3D array containing the optimized quanta for each possible scenario
    private int[][][] optimizedQuanta;

    /**
     * Get optimized quantum time
     *
     * @param numOfProcesses - the remaining number of processes in the ready queue
     * @param timeRemaining  - the total remaining time of the processes in the ready queue
     * @return quantum time
     */
    int getQuantum( int numOfProcesses, int timeRemaining, int longestProcessTime ) {

        // If the index value of the first dimension cannot be found, set it as the last value of the 1d-array
        if ( numOfProcesses > this.optimizedQuanta.length || numOfProcesses <= 0 ) {
            numOfProcesses = this.optimizedQuanta.length - 1;
        }

        // If the index value of the second dimension cannot be found, set it as the highest value of the 2d-array
        if ( timeRemaining > this.optimizedQuanta[numOfProcesses].length || timeRemaining <= 0 ) {
            timeRemaining = this.optimizedQuanta[numOfProcesses].length - 1;
        }

        // If the index value of the third dimension cannot be found, set it as the highest value of the 3d-array
        if ( longestProcessTime > this.optimizedQuanta[numOfProcesses][timeRemaining].length || longestProcessTime <= 0 ) {
            longestProcessTime = this.optimizedQuanta[numOfProcesses][timeRemaining].length - 1;
        }

        return optimizedQuanta[numOfProcesses][timeRemaining][longestProcessTime];
    }

    /**
     * Populates the optimized quanta
     *
     * @param numOfProcesses
     * @param totalBurstTime
     * @param longestBurst
     */
    void populateOptimizedQuanta(int numOfProcesses, int totalBurstTime, int longestBurst) {
        optimizedQuanta = new int[ numOfProcesses ][ totalBurstTime ][ longestBurst ];

        this.generateBestQuantum();
    }

    /**
     * Generates the best quantum values for each combination
     */
    private void generateBestQuantum() {
        // Loop through processes
        for ( int numOfProcesses = 1; numOfProcesses < optimizedQuanta.length; numOfProcesses++ ) {
            // Loop through total time remaining
            for ( int timeRemaining = 1; timeRemaining < optimizedQuanta[ numOfProcesses ].length; timeRemaining++ ) {
                // Loop through largest remaining process
                for ( int longestProcessTime = 1; longestProcessTime < optimizedQuanta[ numOfProcesses ][ timeRemaining ].length; longestProcessTime++ ) {
                    // Can there be less time than the number of processes? No, so set to zero.
                    if ( timeRemaining < numOfProcesses ) {
                        optimizedQuanta[ numOfProcesses ][ timeRemaining ][ longestProcessTime ] = 0;
                        continue;
                    }
                    // Can the largest process be smaller than the total time remaining? No, so set to zero.
                    if ( longestProcessTime > timeRemaining ) {
                        optimizedQuanta[ numOfProcesses ][ timeRemaining ][ longestProcessTime ] = 0;
                        continue;
                    }

                    // Needs to be another check. i.e. Total Time Remaining = 20, num of processes = 3, largest process = 19
                    // In this case, you have 1 process = 19, the second = 1, and the third = 0. Can't have that.
                    // If total-time-remaining - num-processes
                    // 20 - [3 - 1] = 20 - 2 = 18 < 19? Yes, so set to zero.
                    // On the other hand, if Total Time was 21, then 21 - [3 - 1] = 19 < 19? No, so it can work.
                    // 1 process = 19, second process = 1, and third = 1
                    if ( timeRemaining - ( numOfProcesses - 1 ) < longestProcessTime ) {
                        optimizedQuanta[ numOfProcesses ][ timeRemaining ][ longestProcessTime ] = 0;
                        continue;
                    }

                    // Generates a random sorted array with randomProcesses[0] = smallest time
                    int[] randomProcesses = generateRandomQueue( numOfProcesses, timeRemaining, longestProcessTime );

                    // Next we need to cycle through each process and generate the best time quantum for that particular sequence
                    // Get best turnaround time and best wait time
                    optimizedQuanta[ numOfProcesses ][ timeRemaining ][ longestProcessTime ] = getOptimumTimeQuantum( randomProcesses );
                }
            }
        }
    }

    /**
     * Generates a random queue for the number of processes. Each process will have a random burst time. The longest
     * burst time is set to the first process. The remaining processes each get a random burst time.
     *
     * @param numOfProcesses
     * @param totalTime
     * @param longestProcessTime
     * @return
     */
    int[] generateRandomQueue(int numOfProcesses, int totalTime, int longestProcessTime) {
        int[] processes = new int[ numOfProcesses ];
        int timeForOtherProcesses = totalTime - longestProcessTime;

        processes[0] = longestProcessTime;

        if ( numOfProcesses == 1 ) {
            return processes;
        }

        int randomHighValue = timeForOtherProcesses / ( numOfProcesses - 1 );

        for ( int i = 1; i < numOfProcesses - 1; i++ ) {

            int randomTime = new Random().nextInt( (randomHighValue - 1) + 1 );

            if ( randomTime == 0 ) {
                randomTime = 1;
            }

            timeForOtherProcesses = timeForOtherProcesses - randomTime;
            processes[i] = randomTime;

        }

        processes[ numOfProcesses - 1 ] = timeForOtherProcesses;

        return processes;
    }

    /**
     * Returns the optimum time quantum for a specific queue
     *
     * @param processes
     * @return
     */
    private int getOptimumTimeQuantum( int[] processes ) {

        double bestAverageWaiting = Double.MAX_VALUE;
        int    bestQuantum        = 1;

        // Loop through each quantum possibility
        for (int qt = 1; qt <= processes[0]; qt++) {

            double waitingTimeAvg = getAverageWaiting(processes, qt);

            // compare with best avg waiting. If smaller update it and update bestQuantum
            if ( waitingTimeAvg < bestAverageWaiting ) {
                bestAverageWaiting = waitingTimeAvg;
                bestQuantum = qt;
            }

        }

        return bestQuantum;
    }

    /**
     * Returns the average waiting time for a specific queue and time slice
     *
     * @param processes
     * @param qt
     * @return
     */
    double getAverageWaiting( int[] processes, int qt ) {
        int[] burstRemaining = processes.clone();
        int[] finishedOn     = new int[ processes.length ];
        int totalTimeElapsed = 0;

        // Since the first value will have the largest burst, keep looping until it's down to zero
        while ( burstRemaining[0] > 0 ) {

            // Loop through each process and send it to the CPU
            for ( int i = 0; i < processes.length; i++ ) {

                // Since the process is not actually being deleted from the queue, check to make sure it's not a zero
                if ( burstRemaining[i] == 0 ) {
                    continue;
                }

                // Remaining CPU burst
                int qt_remaining = qt;

                // Simulates execution in the CPU
                while ( qt_remaining > 0 && burstRemaining[i] > 0 ) {
                    qt_remaining--;      // decrement time remaining in the CPU
                    burstRemaining[i]--; // decrement remaining burst time for the process
                    totalTimeElapsed++;  // increment the total time it has elapsed
                }

                // Upon exiting the CPU, we check to see if the process finished. If it did, set finishing time for it.
                if ( burstRemaining[i] == 0 ) {
                    finishedOn[i] = totalTimeElapsed;
                }

                totalTimeElapsed++; // increment total time elapsed for switching out processes from CPU

            }
        }

        // calculate waiting time for each process
        double waitingTimeTotal = 0;

        for ( int i = 0; i < processes.length; i++ ) {

            double turnaround = finishedOn[i]; // turnaround = completion - arrival. Arrival is set to 0 for all
            double waiting    = turnaround - burstRemaining[i];

            waitingTimeTotal += waiting; // Set total waiting time

        }

        return waitingTimeTotal / processes.length;
    }
}