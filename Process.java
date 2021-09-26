/*******************************************************************************
 ****    COMP2240 Assignment 3
 ****    c3308061
 ****    Lachlan Court
 ****    19/09/2021
 ****    This class contains information about a Process that is being operated
 ****    on by the CPU. It requires pages and maintains a list of the sequence
 ****    of pages it will require during its process from start to finish
 *******************************************************************************/

import java.util.ArrayList;

public class Process
{
    public enum ProcessState { READY, BLOCKED }

    private ProcessState state;
    private ArrayList<Integer> pageSequence;
    private ArrayList<Integer> pageFaults;
    private String processID;
    private int currentInstruction;
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

    /**
     * Represents a single timestep through the processing system
     * @param currentTime of the simulation
     */
    public void tick(int currentTime)
    {
        // move to the next instruction, and if the process has completed then save the time it finished
        currentInstruction++;
        // Check against the size of the pageSequence list, not -1 because we increment first
        if (currentInstruction == pageSequence.size())
        {
            finishTime = currentTime;
        }
    }

    /**
     * Used for generating a formatted list of page faults at the end of the simulation to match the spec
     * @return a formatted string of page faults that match the spec
     */
    public String getFaults()
    {
        String out = "{";
        // Loop through each fault
        for (int i : pageFaults)
        {
            out += i + ", ";
        }
        // Remove comma and space
        out = out.substring(0, out.length() - 2);
        out += "}";
        return out;
    }

    /**
     * Save a page fault at the current time
     * @param currentTime of the simulation
     */
    public void issueFault(int currentTime)
    {
        pageFaults.add(currentTime);
    }

    // Getters and setters

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

    public int getFinishTime()
    {
        return finishTime;
    }

    public int getTotalFaults()
    {
        return pageFaults.size();
    }

    public void setState(ProcessState state_)
    {
        this.state = state_;
    }

    public boolean isFinished()
    {
        return finishTime >= 0;
    }
}
