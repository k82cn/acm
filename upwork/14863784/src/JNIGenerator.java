
import java.io.*;
import java.util.*;

import except.GenerateException;

public class JNIGenerator extends Generator
{

    private Map<String, String> ftypes = new HashMap<String, String>();

    public void generate(File file, File workingDir, String compileCommand,
                         String compileArgs) throws IOException, GenerateException
    {
        NativeManager nativeManager = new NativeManager();
        nativeManager.manage("title", NativeManager.ParameterType.STRING_TYPE,
                             true);
        nativeManager.manage("fields", NativeManager.ParameterType.LIST_TYPE,
                             true);
        nativeManager.manage("buttons", NativeManager.ParameterType.LIST_TYPE,
                             true);
        // System.out.println(cm.getNativeAccessPointer());

        // Parse config
        nativeManager.parseFrom(file.getPath() + ".config");

        // Get the values
        String title = trim(nativeManager.getStringValue("title"));
        String[] fields = nativeManager.getListValue("fields");
        String[] buttons = nativeManager.getListValue("buttons");

        nativeManager = new NativeManager();

        // The 2nd run will manage the previous list elements

        for (String field : fields)
        {
            String f = trim(field);
            nativeManager.manage(f, NativeManager.ParameterType.STRING_TYPE,
                                 true);
        }
        for (String button : buttons)
        {
            String b = trim(button);
            nativeManager.manage(b, NativeManager.ParameterType.STRING_TYPE,
                                 true);
        }

        // Parse config
        nativeManager.parseFrom(file.getPath() + ".config");

        for (String field : fields)
        {
            String f = trim(field);
            String value = trim(nativeManager.getStringValue(f));

            if (!value.equals("integer") && !value.equals("string")
                && !value.equals("float"))
            {
                throw new GenerateException("Field '" + f
                                            + "' with invalid type '" + value + "'");
            }
            ftypes.put(f, value);
        }

        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(
                workingDir.getPath() + "/" + file.getName() + "/"
                + file.getName() + ".java")));

        // Import statements
        writer.println("import java.awt.*;");
        writer.println("import javax.swing.*;");
        writer.println();
        // Class definition
        writer.println("public class " + file.getName() + " extends JFrame");
        writer.println("{");
        writer.println("\t");
        // Instance variables
        writer.println("\t// The status area");
        writer.println("\tJTextArea statusArea;");
        writer.println("\t");
        writer.println("\t// Fields");
        for (String f : fields)
        {
            String field = trim(f);
            writer.println("\tprivate JLabel " + field + "_label;");
            writer.println("\tprivate JTextField " + field + "_field;");
        }
        writer.println("\t");
        writer.println("\t// Buttons");
        for (String b : buttons)
        {
            String button = trim(b);
            writer.println("\tprivate JButton " + button + "_button;");
        }
        writer.println("\t");
        // The constructor
        writer.println("\t// Constructor");
        writer.println("\tpublic " + file.getName() + "()");
        writer.println("\t{");
        writer.println("\t\tsuper(\"" + title + "\");");
        writer.println("\t\tJPanel fieldsPanel = new JPanel(new BorderLayout());");
        writer.println("\t\tJPanel buttonsPanel = new JPanel();");
        writer.println("\t\tJPanel upperPanel = new JPanel(new BorderLayout());");
        writer.println("\t\tJPanel statusPanel = new JPanel(new BorderLayout());");
        writer.println("\t\tupperPanel.add(fieldsPanel, BorderLayout.NORTH);");
        writer.println("\t\tupperPanel.add(buttonsPanel, BorderLayout.CENTER);");
        writer.println("\t\tgetContentPane().add(upperPanel, BorderLayout.NORTH);");
        writer.println("\t\tgetContentPane().add(statusPanel, BorderLayout.CENTER);");
        writer.println("\t\tJPanel labelPanel = new JPanel(new GridLayout("
                       + fields.length + ", 1));");
        writer.println("\t\tJPanel textFieldPanel = new JPanel(new GridLayout("
                       + fields.length + ", 1));");
        writer.println("\t\tfieldsPanel.add(labelPanel, BorderLayout.WEST);");
        writer.println("\t\tfieldsPanel.add(textFieldPanel, BorderLayout.CENTER);");
        for (String f : fields)
        {
            String field = trim(f);
            writer.println("\t\t" + field + "_label = new JLabel(\"" + field
                           + "\", JLabel.RIGHT);");
            writer.println("\t\t" + field + "_field = new JTextField(20);");
            writer.println("\t\t" + field + "_label.setLabelFor(" + field
                           + "_field);");
            writer.println("\t\tlabelPanel.add(" + field + "_label);");
            writer.println("\t\ttextFieldPanel.add(" + field + "_field);");
            writer.println("\t\t");
        }

        for (String b : buttons)
        {
            String button = trim(b);
            writer.println("\t\t" + button + "_button = new JButton(\""
                           + button + "\");");
            writer.println("\t\t" + button + "_button.addActionListener(new "
                           + trim(nativeManager.getStringValue(button))
                           + "(this));");
            writer.println("\t\tbuttonsPanel.add(" + button + "_button);");
            writer.println("\t\t");
        }
        writer.println("\t\tstatusPanel.add(new JLabel(\"Status\", JLabel.CENTER), BorderLayout.NORTH);");
        writer.println("\t\tstatusArea = new JTextArea();");
        writer.println("\t\tstatusArea.setLineWrap(true);");
        writer.println("\t\tstatusArea.setEditable(false);");
        writer.println("\t\tJScrollPane statusScroller = new JScrollPane(statusArea);");
        writer.println("\t\tstatusPanel.add(statusScroller, BorderLayout.CENTER);");
        writer.println("\t\tsetDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);");
        writer.println("\t\tsetSize(600, 400);");
        writer.println("\t\tsetVisible(true);");
        writer.println("\t}");
        writer.println("\t");
        // Bean set/get methods
        for (String f : fields)
        {
            String field = trim(f);
            String type = ftypes.get(field);

            writer.println("\tpublic String get" + field + "()");
            writer.println("\t{");
            writer.println("\t\treturn " + field + "_field.getText();");
            writer.println("\t}");

            writer.println("\tpublic String getDC" + field + "() throws IllegalFieldValueException");
            writer.println("\t{");

            if (type.equals("integer"))
            {
                writer.println("try { Integer.parseInt(" + field 
                        + "_field.getText()); } catch (Exception e) { throw new IllegalFieldValueException(\"" 
                        + field + " is not an integer.\"); };");
            } else if (type.equals("float"))
            {
                writer.println("try { Float.parseFloat(" + field 
                        + "_field.getText()); } catch (Exception e) { throw new IllegalFieldValueException(\"" 
                        + field + " is not a float.\"); };");
            }

            writer.println("\t\treturn " + field + "_field.getText();");
            writer.println("\t}");



            writer.println("\t");
            writer.println("\tpublic void set" + field + "(String " + field + ")");
            writer.println("\t{");
            writer.println("\t\t" + field + "_field.setText(" + field + ");");
            writer.println("\t}");

            writer.println("\tpublic void setDC" + field + "(String " + field + ") throws IllegalFieldValueException");
            writer.println("\t{");
            if (type.equals("integer"))
            {
                writer.println("try { Integer.parseInt(" + field 
                        + "); } catch (Exception e) { throw new IllegalFieldValueException(\"" 
                        + field + " is not an integer.\"); };");
            } else if (type.equals("float"))
            {
                writer.println("try { Float.parseFloat(" + field 
                        + "); } catch (Exception e) { throw new IllegalFieldValueException(\"" 
                        + field + " is not a float.\"); };");
            }

            writer.println("\t\t" + field + "_field.setText(" + field + ");");
            writer.println("\t}");

            writer.println("\t");
        }
        // Append to status area
        writer.println("\tpublic void appendToStatusArea(String message)");
        writer.println("\t{");
        writer.println("\t\tstatusArea.append(message + \"\\n\");");
        writer.println("\t}");
        writer.println("\t");
        // main()
        writer.println("\t// Main method.");
        writer.println("\tpublic static void main(String[] args)");
        writer.println("\t{");
        writer.println("\t\tnew " + file.getName() + "();");
        writer.println("\t}");
        writer.println("\t");
        writer.println("}");
        writer.close();

        int exitCode = 0;
        String errorMessage = "";
        String cmd = "";
        try
        {
            cmd = compileCommand + " " + compileArgs;
            Process command = Runtime.getRuntime().exec(cmd);
            BufferedReader errorStream = new BufferedReader(
                new InputStreamReader(command.getErrorStream()));
            String line = "";
            while ((line = errorStream.readLine()) != null)
            {
                errorMessage += line + "\n";
            }
            exitCode = command.waitFor();
        }
        catch (Exception e)
        {
            throw new GenerateException("Failed to execute " + e);
        }
        if (exitCode != 0)
        {
            throw new GenerateException(
                "Error during code compilaton/execution:\n" + errorMessage
                + " " + cmd);
        }
    }

    private String trim(String field)
    {
        return field.replaceAll("\"", "").replaceAll(" ", "");
    }

}
