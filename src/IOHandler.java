import java.util.HashMap;

public class IOHandler
{
    private static final int REQUEST_TIME = 6;
    private HashMap<String, HashMap<Integer, Page>> disk;

    public IOHandler()
    {
        disk = new HashMap<String, HashMap<Integer, Page>>();
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

    public void tick() {}

    public void fetchFromMemory(String processID, int pageID) {}
}
