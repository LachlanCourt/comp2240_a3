import java.util.*;

public class FixedCPU extends CPU
{
    private HashMap<String, HashMap<Integer, Page>> mainMemory;
    private final int framesPerProcess;

    public FixedCPU(int frameCount, int quanta_, int processCount)
    {
        super(quanta_);

        framesPerProcess = frameCount / processCount;
        mainMemory = new HashMap<String, HashMap<Integer, Page>>();
    }

    @Override public void init()
    {
        for (Process p : totalProcesses)
        {
            mainMemory.put(p.getProcessID(), new HashMap<Integer, Page>());
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
        int requiredPage = p.getRequiredPageID();
        if (mainMemory.get(p.getProcessID()).containsKey(requiredPage))
        {
            mainMemory.get(p.getProcessID()).get(requiredPage).updateLastUsed(currentTime);
            return true;
        }
        return false;
    }

    @Override protected void addToMemory(Page page)
    {
        // Check if memory is too full
        if (mainMemory.get(page.getProcessID()).size() == framesPerProcess)
        {
            // Remove if necessary
            Map.Entry<Integer, Page> oldestPage = null;
            for (Map.Entry<Integer, Page> entry : mainMemory.get(page.getProcessID()).entrySet())
            {
                if (oldestPage == null || entry.getValue().getLastUsed() < oldestPage.getValue().getLastUsed())
                {
                    oldestPage = entry;
                }
            }
            mainMemory.get(page.getProcessID()).remove(oldestPage);
        }
        // Add
        mainMemory.get(page.getProcessID()).put(page.getPageID(), page);
    }
}
