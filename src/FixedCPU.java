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
        int requiredPage = p.getRequiredPageID();
        for (Page pg : memorySector)
        {
            if (pg.getPageID() == requiredPage)
            {
                pg.updateLastUsed(currentTime);
                return true;
            }
        }
        return false;
    }

    @Override protected void addToMemory(Page page)
    {
        // Check if memory is too full
        if (mainMemory.get(page.getProcessID()).size() == framesPerProcess)
        {
            // Remove if necessary
            int oldestPageIndex = 0;
            int oldestPageTime = 0;
            for (int i = 0; i < framesPerProcess; i++)
            {
                if (mainMemory.get(page.getProcessID()).get(i).getLastUsed() < oldestPageTime)
                {
                    oldestPageIndex = i;
                    oldestPageTime = mainMemory.get(page.getProcessID()).get(i).getLastUsed();
                }
            }
            mainMemory.get(page.getProcessID()).remove(oldestPageIndex);
        }
        // Add
        mainMemory.get(page.getProcessID()).add(page);

    }
}
