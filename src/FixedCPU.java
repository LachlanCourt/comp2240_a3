/*******************************************************************************
 ****    COMP2240 Assignment 3
 ****    c3308061
 ****    Lachlan Court
 ****    19/09/2021
 ****    This class implements a CPU which has a fixed allocation memory system
 ****    with each process getting an equal number of frames
 *******************************************************************************/

import java.util.HashMap;
import java.util.Map;

public class FixedCPU extends CPU
{
    private HashMap<String, HashMap<Integer, Page>> mainMemory;
    private final int framesPerProcess;

    public FixedCPU(int frameCount, int quanta_, int processCount)
    {
        super(quanta_);

        framesPerProcess = frameCount / processCount;
        mainMemory = new HashMap<String, HashMap<Integer, Page>>();
        name = "Fixed-Local";
    }

    @Override public void init()
    {
        // The internal memory map for each process requires knowledge of the processID to act as the key so this
        // method needs to run after the files have been read, not in the constructor
        for (Process p : totalProcesses)
        {
            mainMemory.put(p.getProcessID(), new HashMap<Integer, Page>());
        }
    }

    @Override protected boolean checkMemoryForPage(Process p)
    {
        // Get the page that is required by the process
        int requiredPage = p.getRequiredPageID();
        // If the process exists in the memory, update the last time it was used so that it won't get timed out if
        // memory fills up
        if (mainMemory.get(p.getProcessID()).containsKey(requiredPage))
        {
            mainMemory.get(p.getProcessID()).get(requiredPage).updateLastUsed(currentTime);
            return true;
        }
        // The page does not exist in memory :(
        return false;
    }

    @Override protected void addToMemory(Page page)
    {
        // Check if memory is too full
        if (mainMemory.get(page.getProcessID()).size() == framesPerProcess)
        {
            // Iterate through the hash map to find the page that was used the least recently
            Map.Entry<Integer, Page> oldestPage = null;
            for (Map.Entry<Integer, Page> entry : mainMemory.get(page.getProcessID()).entrySet())
            {
                if (oldestPage == null || entry.getValue().getLastUsed() < oldestPage.getValue().getLastUsed())
                {
                    oldestPage = entry;
                }
            }
            // Remove the oldest page from the map
            mainMemory.get(page.getProcessID()).remove(oldestPage.getKey());
        }
        // Add the new page to the map
        mainMemory.get(page.getProcessID()).put(page.getPageID(), page);
    }

    @Override protected void clearAssociatedMemory(Process currentProcess)
    {
        // Delete the map that is relevant for this process
        mainMemory.remove(currentProcess.getProcessID());
    }
}
