import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class CPU
{
    protected ArrayList<Process> unfinishedProcesses;
    protected ArrayList<Process> readyQueue;
    protected ArrayList<Process> totalProcesses;
    protected int quanta;


    public CPU(int quanta_)
    {
        unfinishedProcesses = new ArrayList<Process>();
        readyQueue = new ArrayList<Process>();
        totalProcesses = new ArrayList<Process>();
        quanta = quanta_;
    }

    public abstract void run();

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
