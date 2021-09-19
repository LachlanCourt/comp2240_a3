/*******************************************************************************
 ****    COMP2240 Assignment 3
 ****    c3308061
 ****    Lachlan Court
 ****    19/09/2021
 ****    This class implements a CPU which has a variable allocation memory
 ****    system
 *******************************************************************************/

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
            mainMemory.get(p.getProcessID() + requiredPage).updateLastUsed(currentTime);
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
}
