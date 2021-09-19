import java.util.ArrayList;

public class Process
{
    public enum ProcessState { READY, BLOCKED }

    private ArrayList<Integer> pageSequence;
    private ProcessState state;
    private int currentInstruction;
    private ArrayList<Integer> pageFaults;
    private String processID;
    private int finishTime;


    public Process(ArrayList<Integer> pageSequence_, String processID_)
    {
        pageSequence = pageSequence_;
        processID = processID_;
        state = ProcessState.READY;
        currentInstruction = 0;
        pageFaults = new ArrayList<Integer>();
        finishTime = -1;
    }

    public int getRequiredPageID()
    {
        return pageSequence.get(currentInstruction);
    }

    public ProcessState getState()
    {
        return state;
    }

    public String getProcessID()
    {
        return processID;
    }

    public void setState(ProcessState state_)
    {
        this.state = state_;
    }

    public void tick(int currentTime)
    {
        currentInstruction++;
        if (currentInstruction == pageSequence.size() - 1)
        {
            finishTime = currentTime;
        }
    }

    public boolean isFinished()
    {
        return finishTime >= 0;
    }

    public void issueFault(int currentTime) {}
}
