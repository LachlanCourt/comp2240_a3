import java.util.HashMap;

public class IOHandler
{
    private static final int REQUEST_TIME = 6;
    private HashMap<String, HashMap<Integer, Page>> disk;
    private CPU cpu;

    public IOHandler(CPU cpu_)
    {
        disk = new HashMap<String, HashMap<Integer, Page>>();
        cpu = cpu_;
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

    public void tick(int currentTime) {}

    public void fetchFromMemory(String processID, int pageID)
    {
        cpu.addToMemory(disk.get(processID).get(pageID));
    }
}
