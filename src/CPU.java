import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class CPU
{
    protected ArrayList<Process> unfinishedProcesses;
    protected ArrayList<Process> readyQueue;
    protected ArrayList<Process> blockedQueue;
    protected ArrayList<Process> totalProcesses;
    protected ArrayList<Process> finishedProcesses;
    protected ArrayList<Integer> pageFaults;
    protected int quanta;

    public CPU(int quanta_)
    {
        unfinishedProcesses = new ArrayList<Process>();
        readyQueue = new ArrayList<Process>();
        blockedQueue = new ArrayList<Process>();
        totalProcesses = new ArrayList<Process>();
        finishedProcesses = new ArrayList<Process>();
        pageFaults = new ArrayList<Integer>();
        quanta = quanta_;
    }

    public void run() {
        // Create IOHandler
        // Loop until complete

        // Move blocked to ready
        // Take from ready
        // Check if page exists
        // if no, block and pass to IO
        // if yes, loop 3 steps
        //      increment time
        //      Tick IO
        //      Move blocked to ready
        //      Check if complete, if yes break
        //      Check if page exists
        //      if no, block and pass to IO and break
        //      if yes, continue
        // if complete, move between queues
        // if not, put back on ready queue or

    }

    public void readProcesses(String[] args)
    {
        for (int i = 2; i < args.length; i++)
        {
            ArrayList<Page> pageSequence = readProcessFile(args[i]);
            Process temp = new Process(pageSequence);
            unfinishedProcesses.add(temp);
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
