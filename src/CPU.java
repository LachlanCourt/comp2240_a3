import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class CPU
{
    protected ArrayList<Process> readyQueue;
    protected ArrayList<Process> blockedQueue;
    protected ArrayList<Process> totalProcesses;
    protected ArrayList<Process> finishedProcesses;
    protected int quanta;
    private IOHandler io;
    private Process currentProcess;
    private int currentTime;

    public CPU(int quanta_)
    {
        readyQueue = new ArrayList<Process>();
        blockedQueue = new ArrayList<Process>();
        totalProcesses = new ArrayList<Process>();
        finishedProcesses = new ArrayList<Process>();
        quanta = quanta_;
        currentTime = 0;
    }

    public void run()
    {
        // Create IOHandler
        io = new IOHandler();
        System.out.println(readyQueue.size());
        // Loop until complete
        while (finishedProcesses.size() != totalProcesses.size()) {
            // If all processes are blocked, increment time and tick IO
            while (readyQueue.size() == 0)
            {
                currentTime++;
                io.tick();
                scanBlockedProcesses();
            }
            // Take from ready
            currentProcess = readyQueue.get(0);

            // Check if page exists
            if (!checkMemoryForPage(currentProcess))
            {
                // if no, block and pass to IO
                blockCurrentProcess();
                io.fetchFromMemory(currentProcess.getPage());

            }
            else {
                // if yes, loop quanta steps
                for (int i = 0; i < quanta; i++) {
                    //      increment time
                    //      Tick IO
                    //      Move blocked to ready
                    currentTime++;
                    io.tick();
                    scanBlockedProcesses();
                    //      Check if complete, if yes break
                    currentProcess.tick();
                    if (currentProcess.isFinished())
                    {
                        break;
                    }
                    if (!checkMemoryForPage(currentProcess))
                    {
                        // if no, block and pass to IO
                        blockCurrentProcess();
                        io.fetchFromMemory(currentProcess.getPage());
                        break;
                    }
                    //      if yes, continue
                }
            }
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

    private void blockCurrentProcess() {
        currentProcess.issueFault(currentTime);
        currentProcess.setState(Process.ProcessState.BLOCKED);
        readyQueue.remove(0);
        blockedQueue.add(currentProcess);
    }

    protected abstract void scanBlockedProcesses();

    protected abstract boolean checkMemoryForPage(Process p);

    protected abstract void addToMemory();

    public void readProcesses(String[] args)
    {
        for (int i = 2; i < args.length; i++)
        {
            ArrayList<Page> pageSequence = readProcessFile(args[i]);
            Process temp = new Process(pageSequence);
            readyQueue.add(temp);
            totalProcesses.add(temp);
        }
    }

    private ArrayList<Page> readProcessFile(String filename)
    {
        ArrayList<Page> pageSequence = new ArrayList<Page>();

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
            try
            {
                pageSequence.add(new Page(Integer.valueOf(data), filename));
            }
            catch (Exception e)
            {
                // Ignore text
            }
        }
        return pageSequence;
    }
}
