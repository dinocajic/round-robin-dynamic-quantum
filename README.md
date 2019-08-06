# round-robin-dynamic-quantum

Read the Research Paper at:
http://doi.org/10.5281/zenodo.3361714

The Preemptive Round Robin Scheduling Algorithm is an important scheduling algorithm used in both process scheduling and network scheduling. Processes are executed for a predefined unit of time called a quantum. Once the CPU executes the process for the specified time slice, the process either terminates or returns to the back of the ready queue if the process has any remaining burst time left. Numerous proposals have been made to improve the static quantum time of the Round Robin Scheduling Algorithm; most research focuses on the optimization of the ready queue. In this paper, I proposed having predefined optimized quantum times for most process that can be retrieved whenever a new process enters the ready queue.

Open Main.java
Either run the program with the test values specified or change the values for:
  •	numOfProcesses
    o Total number of processes that the data structure can hold
  •	totalBurstTime
    o	The total maximum burst time of all processes inside the ready queue
  •	longestBurstTime
    o	The maximum burst time of a single process inside the ready queue
  •	staticQuantumValue
    o	The static value that you want to test the dynamic quantum against
  •	queues
    o	Add an array with the following values:
      	Number of processes inside a single queue
      	Total process burst time inside the queue
      	The longest burst time of a process inside the queue

See example inside Main.java
Run Main.java
