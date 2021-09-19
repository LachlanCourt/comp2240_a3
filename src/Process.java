import java.util.ArrayList;

public class Process
{
    public enum ProcessState { READY, BLOCKED }

    private ArrayList<Integer> pageSequence;
    private ProcessState state;
    private int currentInstruction;
    private ArrayList<Integer> pageFaults;
    private String processID;
    private int intID;
    private int finishTime;


    public Process(ArrayList<Integer> pageSequence_, String processID_, int intID_)
    {
        pageSequence = pageSequence_;
        processID = processID_;
        intID = intID_;
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

    public int getIntID()
    {
        return intID;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public int getTotalFaults()
    {
        return pageFaults.size();
    }

    public String getFaults()
    {
        String out = "{";
        //System.out.println(pageFaults.size());
        for (int i : pageFaults)
        {
            out += i + ", ";
        }
        out = out.substring(0, out.length() - 2); //Remove comma and space
        out += "}";
        return out;
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

    public void issueFault(int currentTime)
    {
        pageFaults.add(currentTime);
    }
}
