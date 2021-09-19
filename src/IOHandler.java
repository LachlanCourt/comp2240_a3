/*******************************************************************************
 ****    COMP2240 Assignment 3
 ****    c3308061
 ****    Lachlan Court
 ****    19/09/2021
 ****    This class simulates an abstracted IO handler with a hashmap representing
 ****    a disk and member functions to handle accepting requests and loading
 ****    pages into main memory
 *******************************************************************************/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IOHandler
{
    private static final int REQUEST_TIME = 6;
    private HashMap<String, HashMap<Integer, Page>> disk;
    private HashMap<Page, Integer> fetching;
    // The io handler is aware of the CPU in order to access the memory
    private CPU cpu;

    public IOHandler(CPU cpu_)
    {
        cpu = cpu_;
        disk = new HashMap<String, HashMap<Integer, Page>>();
        fetching = new HashMap<Page, Integer>();
    }

    /**
     * Setup method as pages are being read they will be written to the disk map of the io handler
     * @param page to be stored in the disk map
     */
    public void writeToDisk(Page page)
    {
        // If a map does not exist for this particular process ID, create a new map
        if (!disk.containsKey(page.getProcessID()))
        {
            disk.put(page.getProcessID(), new HashMap<Integer, Page>());
        }
        // Add the page into the disk map
        // Will overwrite duplicate pages for the same process
        disk.get(page.getProcessID()).put(page.getPageID(), page);
    }

    /**
     * Represents a single timestep through the processing system
     */
    public void tick()
    {
        // Create a new list to store any pages whose io request has been completed and needs to be moved to the ready queue
        ArrayList<Page> readyPages = new ArrayList<Page>();
        // Loop through the currently running io requests
        for (Map.Entry<Page, Integer> entry : fetching.entrySet())
        {
            // Increment the counter for the io request
            fetching.put(entry.getKey(), fetching.get(entry.getKey()) + 1);
            // If the request has been completed, mark it as ready to move into the ready queue
            if (entry.getValue() == REQUEST_TIME)
            {
                readyPages.add(entry.getKey());
            }
        }
        // Loop through pages that need to be moved
        for (Page p : readyPages)
        {
            // Add the page to the memory of the cpu and remove it from the io request map
            cpu.addToMemory(p);
            fetching.remove(p);
        }
    }

    /**
     * Receives an io request and marks it as fetching
     * @param processID of the process making the request
     * @param pageID of the page required
     */
    public void fetchFromMemory(String processID, int pageID)
    {
        // Add the page to the fetching list to be processed every time the tick method runs
        fetching.put(disk.get(processID).get(pageID), 0);
    }
}
