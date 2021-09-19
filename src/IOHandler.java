import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import jdk.jfr.Frequency;

public class IOHandler
{
    private static final int REQUEST_TIME = 6;
    private HashMap<String, HashMap<Integer, Page>> disk;
    private CPU cpu;
    private HashMap<Page, Integer> fetching;

    public IOHandler(CPU cpu_)
    {
        cpu = cpu_;
        disk = new HashMap<String, HashMap<Integer, Page>>();
        fetching = new HashMap<Page, Integer>();
    }

    public void writeToDisk(Page page)
    {
        if (!disk.containsKey(page.getProcessID()))
        {
            disk.put(page.getProcessID(), new HashMap<Integer, Page>());
        }
        // Will overwrite duplicate pages for the same process
        disk.get(page.getProcessID()).put(page.getPageID(), page);
    }

    public void tick()
    {
        ArrayList<Page> readyPages = new ArrayList<Page>();
        for (Map.Entry<Page, Integer> entry : fetching.entrySet())
        {
            fetching.put(entry.getKey(), fetching.get(entry.getKey()) + 1);
            if (entry.getValue() == REQUEST_TIME)
            {
                readyPages.add(entry.getKey());
            }
        }
        for (Page p : readyPages)
        {
            cpu.addToMemory(p);
            fetching.remove(p);
        }
    }

    public void fetchFromMemory(String processID, int pageID)
    {
        fetching.put(disk.get(processID).get(pageID), 0);
    }
}
