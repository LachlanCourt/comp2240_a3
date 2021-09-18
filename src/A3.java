import java.io.File;

public class A3
{
    public static void main(String[] args)
    {
        if (!validArgs(args))
        {
            System.err.println("Invalid arguments. Usage: <frames:integer> <quantum:integer> <filename:string>+");
            System.exit(1);
        }
        A3 a = new A3();
        a.run(args);
    }

    public void run(String[] args)
    {
        
    }

    private static boolean validArgs(String[] args)
    {
        if (args.length < 3)
        {
            return false;
        }

        for (int i = 2; i < args.length - 2; i++)
        {
            File f = new File(args[i]);
            if (!f.exists())
            {
                return false;
            }
        }

        try
        {
            Integer.valueOf(args[0]);
            Integer.valueOf(args[1]);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
}
