import java.util.ArrayList;

public class FixedCPU extends CPU
{
    private ArrayList<ArrayList<Page>> frames;
    private final int framesPerProcess;

    public FixedCPU(int frameCount, int quanta_, int processCount) {
        super(quanta_);

        framesPerProcess = frameCount / processCount;
        frames = new ArrayList<ArrayList<Page>>();
        for (int i = 0; i < processCount; i++)
        {
            frames.add(new ArrayList<Page>());
        }
    }


}
