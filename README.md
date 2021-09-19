
# COMP2240 - Operating Systems
## Assignment 3
### Task

Create a program that simulates a CPU running a Round Robin scheduling algorithm that has a memory management system to restrict the number of pages that can exist in memory frames at a given time. For the purposes of the simulation it was assumed that all processes arrived at T=0

The assignment involved the implementation of both a Fixed-Local memory system which involved dividing the total number of frames by the number of processes, and a Variable-Global memory system in which all frames were available for any process to use. Once processes were finished, the Variable-Global memory is cleared of pages associated with that process.

### Compile
`javac A3.java`

### Run
`java A3 <frames> <quanta> <filename> [filename]...`

Where 
1. `frames` is the number of frames available
2. `quanta` is the time quanta of the Round Robin scheduling algorithm and represents the number of timesteps that a process complete, assuming all its pages are currently in the memory
3. `filename` refers to any number of files that represent one process each 
