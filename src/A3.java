/*******************************************************************************
 ****    COMP2240 Assignment 3
 ****    c3308061
 ****    Lachlan Court
 ****    19/09/2021
 ****    This class is the main running class of the simulation. It runs some
 ****    validation on the command line args, declares a CPU of each type, loads
 ****    the processes, runs the simulation, and then outputs the result
 *******************************************************************************/

import java.io.File;

public class A3
{
    public static void main(String[] args)
    {
        // If the arguments are not valid then the simulation cannot start
        if (!validArgs(args))
        {
            System.err.println("Invalid arguments. Usage: <frames:integer> <quanta:integer> <filename:string>+\nWhere frames >= number of files");
            System.exit(1);
        }
        A3 a = new A3();
        a.run(args);
    }

    public void run(String[] args)
    {
        // Declare and run a fixed local CPU
        FixedCPU fixed = new FixedCPU(Integer.valueOf(args[0]), Integer.valueOf(args[1]), args.length - 2);
        fixed.readProcesses(args);
        fixed.run();

        // Declare and run a variable global CPU
        VariableCPU variable = new VariableCPU(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
        variable.readProcesses(args);
        variable.run();

        // Output the report from each
        System.out.println(fixed + "\n");
        System.out.println(variable);
    }

    private static boolean validArgs(String[] args)
    {
        // There should be at least 3 arguments - the frame count, the quanta, and at least one process
        if (args.length < 3)
        {
            return false;
        }

        // Each argument that represents a file should point to a file that exists
        for (int i = 2; i < args.length; i++)
        {
            File f = new File(args[i]);
            if (!f.exists())
            {
                return false;
            }
        }
        try
        {
            // The first two arguments should be integers
            Integer.valueOf(args[0]);
            Integer.valueOf(args[1]);
        }
        catch (Exception e)
        {
            return false;
        }
        // There must be at least one frame per process for fixed-local
        if (args.length - 2 > Integer.valueOf(args[0]))
        {
            return false;
        }
        // Everything's valid!
        return true;
    }
}

//                                   ....                          ....
//                                  ..x....                      ....x..
//                                 ..xx......     ........     ......xx..
//                                ..xxxx...,,. .............. .,,...xxxx..
//                                ..xxxxx,,,,..................,,,,xxxxx..
//                                 .,,,,..,,...................,,..,,,,,.
//                               ........ ,,,.................,,, .........
//                             ....... .(((,,,...............,,,))). ........
//                            ..... ..,,a@@@@a,,...........,,a@@@@a,,.. ......
//                           .......,,a@@`  '@@,...........,@@`  '@@a,,........
//                           .......,,@@@    @@@,.a@@@@@a.,@@@    @@@,,........
//                           ....,,,,,,@@@aa@@@,,,,`@@@',,,,@@@aa@@@,,,,,,,....
//                            ...,,,,,,,,,,,,,,,,,,,,|,,,,,,,,,,,,,,,,,,,,,...
//                              ...,,,,,,,,,,,,,,,,`   ',,,,,,,,,,,,,,,,,...
//                                  .. ,,,,,,,,,,,,,...,,,,,,,,,,,,,, ..
//                          (     ......... ,,,,,,,,,,,,,,,,,,, ...........
//                       (   )  .............._ _ _ _ _ _ _ _................   (
//                        )  ( ...............................................   )
//                       (   ) ...............................................  (  )
//                        ) ( ,,,,,,,,,,,,,,, ................. ,,,,,,,,,,,,,,,, ) (
//                     ,%%%%,,,,,,,,,,,,,,,,,, ............... ,,,,,,,,,,,,,,,,,,%%%%,
//                     %%%%%`.,,(,,(,,(,,(,,'%%%%%%%%%%%%%%%%%%`,,,),,),,),,),,.'%%%%%
//                     `%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%'
//                        %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//                        ::::::;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;::::::
//                       ::::::;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;::::::
//                      ::::::;;%%;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;::::::
//                     ::::::;;%%;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;%%::::::
//                    ::::::;;%%%;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;%%::::::
//                    ::::::;;%%%;;;;A;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;%%%:::::
//                    ::::::;;;%%;;;;;AA;;;;;;;;;;;;;;;;;;;;A;;;;;;;;;;;;;;;;;;%%%:::::
//                    ::::::;;;;%%;;;;;AAA;;;;;;;;;;;;;;;;AA;;;;;;;;;;;;A;;;;;;%%::::::
//                    ::::::;;A;;;;;;;;;AAA;;;;;;;;;A;;;;AAA;;;;;;;;;;;;;AA;;;%%;::::::
//                     ::::::;AA;;;;;;;;;AAA;;;;;;;A;;;;;AAAA;;;;;A;;;;;;AAA;;;;::::::
//                      ::::::;AAA;;;;;;;AAA;;A;;;AA;;;;;;AAAA;;;;AA;;;;;AAA;;;::::::
//                        :::::;AAA;;;;;AAA;;AA;;;AAA;;;;;;AAAA;;AAA;;;;AAAA;;:::::
//                           :::;AAAA;;AAAA;;AAA;;;AAA;;;;AAAAA;AAA;;;;AAAAAA:::
//                              ::AAAAAAAA;;;;AAA;AAAAA;;AAAAA;;;AAA;;AAAAAAA
//                                .::::::                           ::::::.
//                               :::::::'                           `:::::::