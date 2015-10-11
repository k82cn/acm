
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import except.GenerateException;

public abstract class Generator
{
    public abstract void generate(File file, File workingDir, String compileCommand,
                                  String compileArgs) throws IOException, GenerateException;


    public void run(File file, String runtimeCommand, String runtimeArguments)
    throws IOException, GenerateException
    {
        int exitCode = 0;
        String errorMessage = "";
        String cmd = "";
        try
        {
            cmd = runtimeCommand + " " + runtimeArguments;
            Process pcommand = Runtime.getRuntime().exec(cmd);
            BufferedReader errorStream = new BufferedReader(
                new InputStreamReader(pcommand.getErrorStream()));
            BufferedReader printStream = new BufferedReader(
                new InputStreamReader(pcommand.getInputStream()));
            String line = "";
            while ((line = errorStream.readLine()) != null)
            {
                errorMessage += line + "\n";
            }
            while ((line = printStream.readLine()) != null)
            {
                System.out.println(line);
            }
            exitCode = pcommand.waitFor();
        }
        catch (Exception e)
        {
            throw new GenerateException("Failed to execute " + e);
        }
        if (exitCode != 0)
        {
            throw new GenerateException("Error during execution:\n"
                                        + errorMessage + " " + cmd);
        }
    }
}
