public class FixedCPU extends CPU
{
    public FixedCPU(int frames, int quanta_) {
        super(quanta_);

    }

    @Override
    public void run() {
        // Loop until complete

        // Move blocked to ready
        // Take from ready
        // Check if page exists
        // if no, block and pass to IO
        // if yes, loop 3 steps
        //      increment time
        //      Move blocked to ready
        //      Tick IO
        //      Check if complete, if yes break
        //      Check if page exists
        //      if no, block and pass to IO and break
        //      if yes, continue
        // if complete, move between queues
        // if not, put back on ready queue or


    }
}
