import java.util.ArrayList;
import java.util.HashMap;

public class FixedCPU extends CPU
{
    private HashMap<String, ArrayList<Page>> mainMemory;
    private final int framesPerProcess;

    public FixedCPU(int frameCount, int quanta_, int processCount)
    {
        super(quanta_);

        framesPerProcess = frameCount / processCount;
        mainMemory = new HashMap<String, ArrayList<Page>>();
    }

    @Override public void init()
    {
        for (Process p : totalProcesses)
        {
            mainMemory.put(p.getProcessID(), new ArrayList<Page>());
        }
    }

    @Override protected void scanBlockedProcesses()
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

    @Override protected boolean checkMemoryForPage(Process p)
    {
        ArrayList<Page> memorySector = mainMemory.get(p.getProcessID());
        Page requiredPage = p.getPage();
        for (Page pg : memorySector)
        {
            if (pg == requiredPage)
            {
                return true;
            }
        }
        return false;
    }

    @Override protected void addToMemory() {}
}
