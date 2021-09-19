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
    protected int quanta;
    protected int currentTime;
    protected String name = "";
    private IOHandler io;
    private Process currentProcess;

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
            // Take from ready
            currentProcess = readyQueue.get(0);

            // if yes, loop quanta steps
            for (int i = 0; i < quanta; i++)
            {
                if (!checkMemoryForPage(currentProcess))
                {
                    // if no, block and pass to IO
                    blockCurrentProcess();
                    io.fetchFromMemory(currentProcess.getProcessID(), currentProcess.getRequiredPageID());
                    break;
                }
                //      increment time
                //      Tick IO
                //      Move blocked to ready
                currentTime++;
                io.tick();
                scanBlockedProcesses();
                // Check if complete, if yes break
                currentProcess.tick(currentTime);
                if (currentProcess.isFinished())
                {
                    break;
                }
                //      if yes, continue
            }
            if (currentProcess.getState() != Process.ProcessState.BLOCKED)
            {
                // if complete, move between queues
                // if not, put back on ready queue
                readyQueue.remove(0);
                if (currentProcess.isFinished())
                {
                    finishedProcesses.add(currentProcess);
                }
                else
                {
                    readyQueue.add(currentProcess);
                }
            }
        }
    }

    private void blockCurrentProcess()
    {
        currentProcess.issueFault(currentTime);
        currentProcess.setState(Process.ProcessState.BLOCKED);
        readyQueue.remove(0);
        blockedQueue.add(currentProcess);
    }

    protected void scanBlockedProcesses()
    {
        ArrayList<Process> toMove = new ArrayList<Process>();
        for (Process p : blockedQueue)
        {
            if (checkMemoryForPage(p))
            {
                toMove.add(p);
            }
        }
        for (Process p : toMove)
        {
            blockedQueue.remove(p);
            p.setState(Process.ProcessState.READY);
            readyQueue.add(p);
        }
    }

    protected abstract void init();

    protected abstract boolean checkMemoryForPage(Process p);

    protected abstract void addToMemory(Page page);

    public void readProcesses(String[] args)
    {
        for (int i = 2; i < args.length; i++)
        {
            ArrayList<Integer> pageSequence = readProcessFile(args[i]);
            Process temp =
                new Process(pageSequence, args[i], i - 1);  // i - 1 will start at 1, giving a unique ID to each process
            readyQueue.add(temp);
            totalProcesses.add(temp);
        }
        init();
    }

    private ArrayList<Integer> readProcessFile(String filename)
    {
        ArrayList<Integer> pageSequence = new ArrayList<Integer>();

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

            Page p = new Page(Integer.valueOf(data), filename);
            io.writeToDisk(p);
            pageSequence.add(p.getPageID());
        }
        return pageSequence;
    }

    @Override public String toString()
    {
        String out =
            "FIFO - " + name + " Replacement:\nPID  Process Name      Turnaround Time  # Faults  Fault Times\n";
        Collections.sort(finishedProcesses, (Process a, Process b) -> { return a.getIntID() < b.getIntID() ? -1 : 1; });
        for (Process p : finishedProcesses)
        {
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
