import java.util.ArrayList;

public class Process
{
    public enum ProcessState
    {
        READY,
        BLOCKED
    }

    private ArrayList<Page> pageSequence;
    private ProcessState state;
    private int currentInstruction;
    private ArrayList<Integer> pageFaults;


    public Process(ArrayList<Page> pageSequence_)
    {
        pageSequence = pageSequence_;
        state = ProcessState.READY;
        currentInstruction = 0;
        pageFaults = new ArrayList<Integer>();
    }

    public Page getPage() {
        return pageSequence.get(currentInstruction);
    }

    public ProcessState getState() {
        return state;
    }

    public void setState(ProcessState state_) {
        this.state = state_;
    }

    public void tick() {
    }

    public boolean isFinished() {
        return true;
    }

    public void issueFault(int currentTime) {
    }
}
