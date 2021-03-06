The simulation was run with the following command and arguments

A3 30 3 Process1.txt Process2.txt Process3.txt Process4.txt

where 30 is the number of frames and 3 is the quantum size for Round Robin algorithm and the other arguments are text file names containing page references for each process.

The simulation made the following assumptions:

-Issuing a page fault and subsequently blocking a process takes no time (this explains why all four processes are able to issue a page fault "simultaneously" at t=0)
-Execution of a swapped page can occur immediately after the page arrives in main memory (e.g. page 1 of process1 arrived in main memory at t=6 and executed at t=6, then a page fault was issued for the next page at t=7.

