import java.util.ArrayList;

public class FixedCPU extends CPU
{
    private ArrayList<ArrayList<Page>> mainMemory;
    private final int framesPerProcess;

    public FixedCPU(int frameCount, int quanta_, int processCount)
    {
        super(quanta_);

        framesPerProcess = frameCount / processCount;
        mainMemory = new ArrayList<ArrayList<Page>>();
        for (int i = 0; i < processCount; i++)
        {
            mainMemory.add(new ArrayList<Page>());
        }
    }

    @Override
    protected void scanBlockedProcesses() {
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

    @Override
    protected boolean checkMemoryForPage(Process p) {
        return true;
    }

    @Override
    protected void addToMemory() {

    }
}
