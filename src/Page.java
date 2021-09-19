public class Page
{
    private int pageID;
    private String processID;
    private int lastUsed;

    public Page(int pageID_, String processID_)
    {
        pageID = pageID_;
        processID = processID_;
        lastUsed = 0;
    }

    public int getPageID()
    {
        return pageID;
    }

    public void setPage(int page)
    {
        this.pageID = page;
    }

    public String getProcessID()
    {
        return processID;
    }

    public void setProcessID(String processID)
    {
        this.processID = processID;
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
