import java.util.ArrayList;
import java.util.HashMap;

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
        HashMap<Integer, Page> memorySector = mainMemory.get(p.getProcessID());
        int requiredPage = p.getRequiredPageID();
        if (memorySector.containsKey(requiredPage))
        {
            memorySector.get(requiredPage).updateLastUsed(currentTime);
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
        mainMemory.get(page.getProcessID()).put(page.getPageID(), page);
    }
}
