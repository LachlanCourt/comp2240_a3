/*******************************************************************************
 ****    COMP2240 Assignment 3
 ****    c3308061
 ****    Lachlan Court
 ****    19/09/2021
 ****    This class contains information about a page in memory that will be
 ****    manipulated between the IOHandler and the main memory frames in the CPU
 *******************************************************************************/

public class Page
{
    private int pageID;
    private String processID;
    // The last time that this page was used - if the main memory is too full, older frames will be loaded out
    private int lastUsed;

    public Page(int pageID_, String processID_)
    {
        pageID = pageID_;
        processID = processID_;
        lastUsed = 0;
    }

    // Getters and setters

    public int getPageID()
    {
        return pageID;
    }

    public String getProcessID()
    {
        return processID;
    }

    public int getLastUsed()
    {
        return lastUsed;
    }

    public void updateLastUsed(int update_)
    {
        lastUsed = update_;
    }
}
