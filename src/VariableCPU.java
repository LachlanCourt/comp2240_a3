import java.util.HashMap;

public class VariableCPU extends CPU
{
    private HashMap<String, Page> mainMemory;
    private final int frameCount;

    public VariableCPU(int frameCount_, int quanta_, int processCount)
    {
        super(quanta_);

        frameCount = frameCount_;
        mainMemory = new HashMap<String, Page>();
        name = "Variable-Global";
    }

    @Override
    protected void init() {
        // No further initialisation required after file read for variable :)
    }

    @Override
    protected boolean checkMemoryForPage(Process p)
    {
        int requiredPage = p.getRequiredPageID();
        if (mainMemory.containsKey(p.getProcessID()+requiredPage))
        {
            mainMemory.get(p.getProcessID()+requiredPage).updateLastUsed(currentTime);
            return true;
        }
        return false;
    }

    @Override
    protected void addToMemory(Page page)
    {
        
    }
}
