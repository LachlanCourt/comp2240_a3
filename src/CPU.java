import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CPU
{
    protected ArrayList<Process> unfinishedProcesses;
    protected ArrayList<Process> readyQueue;
    protected ArrayList<Process> totalProcesses;
    protected int quanta;


    public CPU(int frames, int quanta)
    {
        unfinishedProcesses = new ArrayList<Process>();
        readyQueue = new ArrayList<Process>();
        totalProcesses = new ArrayList<Process>();

    }

    public void readProcesses(String[] args)
    {
        for (int i = 2; i < args.length; i++)
        {
            ArrayList<Integer> pageSequence = readProcessFile(args[i]);
            Process temp = new Process(pageSequence);
            unfinishedProcesses.add(temp);
            readyQueue.add(temp);
            totalProcesses.add(temp);
        }
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
            try
            {
                pageSequence.add(Integer.valueOf(data));
            }
            catch (Exception e)
            {
                // Ignore text
            }
        }
        return pageSequence;
    }
}
