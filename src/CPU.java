/*******************************************************************************
 ****    COMP2240 Assignment 3
 ****    c3308061
 ****    Lachlan Court
 ****    19/09/2021
 ****    This class declares the abstract signature of a CPU which handles the
 ****    processing of a number of processes read in from a text file, and
 ****    calculates the time taken to finish each process
 *******************************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public abstract class CPU
{
    protected ArrayList<Process> readyQueue;
    protected ArrayList<Process> blockedQueue;
    protected ArrayList<Process> totalProcesses;
    protected ArrayList<Process> finishedProcesses;
    protected String name = "";
    protected int currentTime;
    protected int quanta;

    private Process currentProcess;
    private IOHandler io;

    public CPU(int quanta_)
    {
        readyQueue = new ArrayList<Process>();
        blockedQueue = new ArrayList<Process>();
        totalProcesses = new ArrayList<Process>();
        finishedProcesses = new ArrayList<Process>();
        io = new IOHandler(this);
        quanta = quanta_;
        currentTime = 0;
    }

    /**
     * Main method of the CPU which handles the simulation from start to finish
     */
    public void run()
    {
        // Loop until complete
        while (finishedProcesses.size() != totalProcesses.size())
        {
            // If all processes are blocked, increment time and tick IO
            while (readyQueue.size() == 0)
            {
                currentTime++;
                io.tick();
                scanBlockedProcesses();
            }
            // Take first process from ready queue
            currentProcess = readyQueue.get(0);

            // Loop quanta steps
            for (int i = 0; i < quanta; i++)
            {
                // Check if the page required next for the current process is currently in memory
                if (!checkMemoryForPage(currentProcess))
                {
                    // if no, block and request from IO
                    blockCurrentProcess();
                    io.fetchFromMemory(currentProcess.getProcessID(), currentProcess.getRequiredPageID());
                    break;
                }
                // Increment time and tick IO
                currentTime++;
                io.tick();
                scanBlockedProcesses();

                // Increment the program counter for the current process and if complete, break
                currentProcess.tick(currentTime);
                if (currentProcess.isFinished())
                {
                    break;
                }
            }
            // If the process either timed out according to the quanta or if the process has finished
            // Will not run if the process is blocked
            if (currentProcess.getState() != Process.ProcessState.BLOCKED)
            {
                // If complete, move between queues
                // If not, put back on ready queue
                readyQueue.remove(0);
                if (currentProcess.isFinished())
                {
                    finishedProcesses.add(currentProcess);
                    clearAssociatedMemory(currentProcess);
                }
                else
                {
                    readyQueue.add(currentProcess);
                }
            }
        }
    }

    /**
     * For handle the clearing of the memory after a process has completed
     * @param currentProcess that just finished
     */
    protected abstract void clearAssociatedMemory(Process currentProcess);

    /**
     * Handles blocking the current process if waiting for io
     */
    private void blockCurrentProcess()
    {
        // Issue a page fault and set the state to blocked
        currentProcess.issueFault(currentTime);
        currentProcess.setState(Process.ProcessState.BLOCKED);
        // Remove process from ready queue and add it to the blocked queue
        readyQueue.remove(0);
        blockedQueue.add(currentProcess);
    }

    /**
     * Scans through the list of blocked processes, and if their io request is ready will move them back to the ready
     * queue
     */
    protected void scanBlockedProcesses()
    {
        // Initialise a new list to store processes that need to move to avoid concurrent modification
        ArrayList<Process> toMove = new ArrayList<Process>();
        for (Process p : blockedQueue)
        {
            // Check if the process is in memory now and if so add it to the move list
            if (checkMemoryForPage(p))
            {
                toMove.add(p);
            }
        }
        // Loop through the list of processes to move
        for (Process p : toMove)
        {
            // Remove from blocked queue, mark it as ready, and move to ready queue
            blockedQueue.remove(p);
            p.setState(Process.ProcessState.READY);
            readyQueue.add(p);
        }
    }

    /**
     * Initialise the CPU if there are things that need to be done after the processes have been read and cannot run
     * during the constructor
     */
    protected abstract void init();

    /**
     * Check if the page required for the specified process currently exists in the main memory of the cpu
     * @param p Process to be assessed
     * @return indication of whether the process would be able to continue processing
     */
    protected abstract boolean checkMemoryForPage(Process p);

    /**
     * Method to allow the io handler to add a new page into the main memory. Will also handle removing old pages if
     * the memory is full
     * @param page to be added to the main memory
     */
    protected abstract void addToMemory(Page page);

    /**
     * Handle the reading of all processes from files and adding the data into relevant queues ready to commence the
     * simulation
     * @param args from the command line passed in raw but after validation - it can be assumed that they are valid
     */
    public void readProcesses(String[] args)
    {
        // The first two arguments refer to the frame size and the quanta
        for (int i = 2; i < args.length; i++)
        {
            // Read the page sequence in from the file
            ArrayList<Integer> pageSequence = readProcessFile(args[i]);
            // Give a unique ID to each process, starting from i - 1 which will start at 1
            Process temp = new Process(pageSequence, args[i], i - 1);
            // Add to relevant queues
            readyQueue.add(temp);
            totalProcesses.add(temp);
        }
        // Now that processes have been read, init method can be run
        init();
    }

    /**
     * Read a single file in as a sequence of pages required to complete processing
     * @param filename to be read
     * @return sequence of page id's required
     */
    private ArrayList<Integer> readProcessFile(String filename)
    {
        // Declare a list to hold page id's
        ArrayList<Integer> pageSequence = new ArrayList<Integer>();

        // Try to open a scanner with the specified filename
        Scanner input = null;
        try
        {
            input = new Scanner(new File(filename));
        }
        catch (FileNotFoundException e)
        {
            System.err.println("File " + filename + "does not exist");
            System.exit(1);
        }

        // Loop through each line of the file
        while (input.hasNext())
        {
            String data = input.next();

            // Ignore text
            try
            {
                Integer.valueOf(data);
            }
            catch (Exception e)
            {
                continue;
            }

            // Create a new page with the data read and add it to the io handler
            Page p = new Page(Integer.valueOf(data), filename);
            io.writeToDisk(p);
            // Add just the id to the list to be stored in the process
            pageSequence.add(p.getPageID());
        }
        return pageSequence;
    }

    /**
     * Overridden string method to match spec
     * @return string report of the simulation
     */
    @Override public String toString()
    {
        // Declare heading and sort processes by ID
        String out =
            "FIFO - " + name + " Replacement:\nPID  Process Name      Turnaround Time  # Faults  Fault Times\n";
        Collections.sort(finishedProcesses, (Process a, Process b) -> { return a.getIntID() < b.getIntID() ? -1 : 1; });
        // Loop through each process, now sorted
        for (Process p : finishedProcesses)
        {
            // Grab required data for the report and then format and add to the return string
            String pid = String.valueOf(p.getIntID());
            String pname = p.getProcessID();
            String tt = String.valueOf(p.getFinishTime());
            String f = String.valueOf(p.getTotalFaults());
            String ft = p.getFaults();
            out += String.format("%-5s%-18s%-17s%-10s%-1s", pid, pname, tt, f, ft) + "\n";
        }
        return out;
    }
}
