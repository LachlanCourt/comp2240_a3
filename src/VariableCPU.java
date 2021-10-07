/*******************************************************************************
 ****    COMP2240 Assignment 3
 ****    c3308061
 ****    Lachlan Court
 ****    19/09/2021
 ****    This class implements a CPU which has a variable allocation memory
 ****    system
 *******************************************************************************/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VariableCPU extends CPU
{
    private HashMap<String, Page> mainMemory;
    private final int frameCount;

    public VariableCPU(int frameCount_, int quanta_)
    {
        super(quanta_);

        frameCount = frameCount_;
        mainMemory = new HashMap<String, Page>();
        name = "Variable-Global";
    }

    @Override protected void init()
    {
        // No further initialisation required after file read for variable :)
    }

    @Override protected boolean checkMemoryForPage(Process p)
    {
        // Get the page that is required by the process
        int requiredPage = p.getRequiredPageID();
        // If the process exists in the memory, update the last time it was used so that it won't get timed out if
        // memory fills up
        if (mainMemory.containsKey(p.getProcessID() + requiredPage))
        {
            // Uncommenting this line will make the CPU run Least Recently Used page replacement policy. This way it is
            // FIFO as per spec
            // mainMemory.get(p.getProcessID() + requiredPage).updateLastUsed(currentTime);
            return true;
        }
        // The page does not exist in memory :(
        return false;
    }

    @Override protected void addToMemory(Page page)
    {
        // Check if memory is too full
        if (mainMemory.size() == frameCount)
        {
            // Iterate through the hash map to find the page that was used the least recently
            Map.Entry<String, Page> oldestPage = null;
            for (Map.Entry<String, Page> entry : mainMemory.entrySet())
            {
                if (oldestPage == null || entry.getValue().getLastUsed() < oldestPage.getValue().getLastUsed())
                {
                    oldestPage = entry;
                }
            }
            // Remove the oldest page from the map
            mainMemory.remove(oldestPage.getKey());
        }
        // Add the new page to the map
        mainMemory.put(page.getProcessID() + page.getPageID(), page);
    }

    @Override protected void clearAssociatedMemory(Process currentProcess)
    {
        // Initialise a new list to store map entries that need to be removed to avoid concurrent modification
        ArrayList<Map.Entry<String, Page>> toRemove = new ArrayList<Map.Entry<String, Page>>();
        // Iterate through the hash map to find pages that are associated with the process that just finished
        for (Map.Entry<String, Page> entry : mainMemory.entrySet())
        {
            if (entry.getValue().getProcessID().equals(currentProcess.getProcessID()))
            {
                toRemove.add(entry);
            }
        }
        // Remove each entry from the map
        for (Map.Entry<String, Page> entry : toRemove)
        {
            mainMemory.remove(entry.getKey());
        }
    }
}
