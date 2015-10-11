import except.GenerateException;

import java.io.*;

public class LexGenerator extends Generator {


    public void generate(File file, File workingDir, String compileCommand,
                         String compileArgs) throws IOException, GenerateException {

        String className = file.getName();
        String input = file.getAbsolutePath() + ".config";
        String output = workingDir.getPath() + File.separator + file.getName() + File.separator
                + file.getName() + ".java";
        String dialogHome = System.getProperty(Dialogc.DIALOGC_HOME);
        String dbConf;
        if (dialogHome != null)
            dbConf = dialogHome + File.separator + "conf/db.conf";
        else
            dbConf = workingDir.getPath() + File.separator + "conf/db.conf";

        String errorMessage = "";
        String cmd = "yadc " + input + " " + output + " " + className + " " + dbConf;
        int exitCode = 0;

        Process command = Runtime.getRuntime().exec(cmd);

        BufferedReader errorStream = new BufferedReader(
                new InputStreamReader(command.getErrorStream()));
        String line = "";
        while ((line = errorStream.readLine()) != null) {
            errorMessage += line + "\n";
        }
        try {
            exitCode = command.waitFor();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (exitCode != 0) {
            throw new GenerateException(
                    "Error during code compilaton/execution:\n" + errorMessage
                            + " " + cmd);
        }


        exitCode = generate_exception_class(workingDir.getAbsolutePath() + File.separator + file.getName());

        if (exitCode != 0) {
            throw new GenerateException(
                    "Error during code compilaton/execution:\n" + errorMessage
                            + " " + cmd);
        }

        runCompiler(compileCommand, compileArgs);
    }

    private int generate_exception_class(String path) {

        FileWriter writer = null;

        try {

            writer = new FileWriter(path + File.separator + "IllegalFieldValueException.java");
            writer.append("public class IllegalFieldValueException extends Exception\n" +
                    "{\n" +
                    "    public IllegalFieldValueException(String msg)\n" +
                    "    {\n" +
                    "        super(msg);\n" +
                    "    }\n" +
                    "}");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return 0;
    }

    private void runCompiler(String compileCommand, String compileArgs)
            throws GenerateException {
        int exitCode = 0;
        String errorMessage = "";
        String cmd = "";
        try {
            cmd = compileCommand + " " + compileArgs;
            Process command = Runtime.getRuntime().exec(cmd);
            BufferedReader errorStream = new BufferedReader(
                    new InputStreamReader(command.getErrorStream()));
            String line = "";
            while ((line = errorStream.readLine()) != null) {
                errorMessage += line + "\n";
            }
            exitCode = command.waitFor();
        } catch (Exception e) {
            throw new GenerateException("Failed to execute " + e);
        }
        if (exitCode != 0) {
            throw new GenerateException(
                    "Error during code compilaton/execution:\n" + errorMessage
                            + " " + cmd);
        }
    }

}
