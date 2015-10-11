import except.GenerateException;
import gui.CompileModeDialog;
import gui.FileBrowseDialog;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class Dialogc extends JFrame
{
    public enum DialogChoice
    {
        SAVE, IGNORE, CANCEL
    };

    public static final String DIALOGC_HOME = "dialogc.home";

    public static final String TITLE = "Dialogc";
    public static final String LOAD_FAILED = "Can't open the document. Check permissions and filename.";
    public static final String SAVE_FAILED = "Can't save the document. Check permissions";

    private JTextField notice = new JTextField();
    private JTextArea editorConfig;
    private JFileChooser fileChooser;
    private DialogcDocument documentConfig;
    private ActionListener fileNewAction, fileOpenAction, fileSaveAction,
            fileSaveAsAction, generateAction, generateAndRunAction;
    String helpMessage = "";
    String aboutMessage = "";
    private File compileJavaCmd = new File("javac");
    private String compilerArgs = "";
    private File runJavaCmd = new File("java");
    private String runArgs = "";
    private File workDir = new File(".");
    private String filename, filenameWithoutExtension;
    private javax.swing.filechooser.FileFilter configFilter;
    private boolean untitled = true;
    private boolean modified = false;
    private int fileIndex;

    private Generator generator = new LexGenerator();
    protected int generatorType = gui.CompileModeDialog.LEX;

    public static void main(String[] args) throws Exception
    {
        new Dialogc();
    }

    public Dialogc() throws Exception
    {
        helpMessage = loadFile("README.txt");
        aboutMessage = loadFile("About.txt");
        fileChooser = new JFileChooser();
        configFilter = new javax.swing.filechooser.FileFilter()
        {
            public boolean accept(File file)
            {
                return file.getName().endsWith(".config");
            }

            public String getDescription()
            {
                return "Dialogc configuration (*.config)";
            }
        };
        fileChooser.addChoosableFileFilter(configFilter);
        setSize(640, 480);
        editorConfig = new JTextArea();

        editorConfig.getDocument().addDocumentListener(new DocumentListener()
        {

            @Override
            public void insertUpdate(DocumentEvent e)
            {
                Dialogc.this.setModified(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                Dialogc.this.setModified(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                Dialogc.this.setModified(true);
            }

        });

        JScrollPane scrollPane = new JScrollPane(editorConfig);

        getContentPane().add(BorderLayout.CENTER, scrollPane);

        JMenuBar menuBar = new JMenuBar();
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JMenu fileMenu = setFileMenu(toolBar);

        JMenu generateMenu = setupCompileMenu(toolBar);

        // Configuration menu and its items
        JMenu configMenu = setupConfigMenu();

        JMenu helpMenu = setupHelpMenu();

        menuBar.add(fileMenu);
        menuBar.add(generateMenu);
        menuBar.add(configMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(notice, BorderLayout.SOUTH);

        setVisible(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent windowEvent)
            {
                quit();
            }
        });
        newDocument();
    }

    private JMenu setupHelpMenu()
    {
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpMenuItemHelp = new JMenuItem("Help");
        helpMenuItemHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
                                        0));
        helpMenuItemHelp.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                JTextArea area = new JTextArea();
                area.setRows(20);
                area.setColumns(50);
                area.setEditable(false);
                area.setText(helpMessage);
                area.setLineWrap(true);
                JOptionPane.showMessageDialog(null, new JScrollPane(area),
                                              "Help", JOptionPane.PLAIN_MESSAGE);
            }
        });
        JMenuItem helpMenuItemAbout = new JMenuItem("About");
        helpMenuItemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8,
                                         0));
        helpMenuItemAbout.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                infoMessage(aboutMessage);
            }
        });
        helpMenu.add(helpMenuItemHelp);
        helpMenu.add(helpMenuItemAbout);
        return helpMenu;
    }

    private JMenu setupCompileMenu(JToolBar toolBar)
    {
        JMenu generateMenu = new JMenu("Compile");
        JMenuItem generateMenuItem = new JMenuItem("Compile");
        generateMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10,
                                        InputEvent.CTRL_DOWN_MASK));
        JButton generateButton = new JButton();
        generateButton.setIcon(new ImageIcon(Dialogc.class
                                             .getResource("icons/generate.png"), ""));
        generateAction = new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                generateCode();
            }
        };
        generateMenuItem.addActionListener(generateAction);
        generateButton.addActionListener(generateAction);

        JMenuItem generateAndRunMenuItem = new JMenuItem(
            "Generate, Compile and Run");
        generateAndRunMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F11, InputEvent.CTRL_DOWN_MASK));
        JButton generateAndRunButton = new JButton();
        generateAndRunButton.setIcon(new ImageIcon(Dialogc.class
                                     .getResource("icons/generateRun.png"), ""));
        generateAndRunAction = new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                generateAndRunDocument();
            }
        };
        generateAndRunMenuItem.addActionListener(generateAndRunAction);
        generateAndRunButton.addActionListener(generateAndRunAction);
        generateMenu.add(generateMenuItem);
        generateMenu.add(generateAndRunMenuItem);

        toolBar.add(generateButton);
        toolBar.add(generateAndRunButton);
        return generateMenu;
    }

    private JMenu setFileMenu(JToolBar toolBar)
    {
        // File menu and items
        JMenu fileMenu = new JMenu("File");
        JMenuItem fileNewMenuItem = new JMenuItem("New");
        fileNewMenuItem.setAccelerator(KeyStroke.getKeyStroke('N',
                                       InputEvent.CTRL_DOWN_MASK));
        JButton fileNewButton = new JButton();
        fileNewButton.setIcon(new ImageIcon(Dialogc.class
                                            .getResource("icons/new.png"), ""));
        fileNewAction = new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                newDocument();
            }
        };
        fileNewMenuItem.addActionListener(fileNewAction);
        fileNewButton.addActionListener(fileNewAction);
        JMenuItem fileOpenMenuItem = new JMenuItem("Open");
        fileOpenMenuItem.setAccelerator(KeyStroke.getKeyStroke('O',
                                        InputEvent.CTRL_DOWN_MASK));
        JButton fileOpenButton = new JButton();
        fileOpenButton.setIcon(new ImageIcon(Dialogc.class
                                             .getResource("icons/open.png"), ""));
        fileOpenAction = new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                openDocument();
            }
        };
        fileOpenMenuItem.addActionListener(fileOpenAction);
        fileOpenButton.addActionListener(fileOpenAction);
        JMenuItem fileSaveMenuItem = new JMenuItem("Save");
        fileSaveMenuItem.setAccelerator(KeyStroke.getKeyStroke('S',
                                        InputEvent.CTRL_DOWN_MASK));
        JButton fileSaveButton = new JButton();
        fileSaveButton.setIcon(new ImageIcon(Dialogc.class
                                             .getResource("icons/save.png"), ""));
        fileSaveAction = new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                try
                {
                    saveDocument();
                }
                catch (IOException e)
                {
                    errorMessage(SAVE_FAILED);
                    return;
                }
            }
        };
        fileSaveMenuItem.addActionListener(fileSaveAction);
        fileSaveButton.addActionListener(fileSaveAction);
        JMenuItem fileSaveAsMenuItem = new JMenuItem("Save As");
        JButton fileSaveAsButton = new JButton();
        fileSaveAsButton.setIcon(new ImageIcon(Dialogc.class
                                               .getResource("icons/save.png"), ""));
        fileSaveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke('S',
                                          InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
        fileSaveAsAction = new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                try
                {
                    saveAsDocument();
                }
                catch (IOException e)
                {
                    errorMessage(SAVE_FAILED);
                    return;
                }
            }
        };

        fileSaveAsMenuItem.addActionListener(fileSaveAsAction);
        fileSaveAsButton.addActionListener(fileSaveAsAction);
        JMenuItem fileQuitMenuItem = new JMenuItem("Quit");
        fileQuitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
                                        InputEvent.ALT_DOWN_MASK));
        fileQuitMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                quit();
            }
        });
        fileMenu.add(fileNewMenuItem);
        fileMenu.add(fileOpenMenuItem);
        fileMenu.add(fileSaveMenuItem);
        fileMenu.add(fileSaveAsMenuItem);
        fileMenu.add(fileQuitMenuItem);
        toolBar.add(fileNewButton);
        toolBar.add(fileOpenButton);
        toolBar.add(fileSaveButton);
        toolBar.add(fileSaveAsButton);
        return fileMenu;
    }

    private JMenu setupConfigMenu()
    {
        JMenu configMenu = new JMenu("Config");

        JMenuItem configModeMenuItem = new JMenuItem("Compiler Mode ");
        configModeMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                                              KeyEvent.VK_F6, InputEvent.CTRL_DOWN_MASK));
        configModeMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                // TODO: update the code for radio
                CompileModeDialog compileMode = new CompileModeDialog(
                    Dialogc.this, "Compile Mode", Dialogc.this.generatorType);
                if (compileMode.getChanged())
                {
                    Dialogc.this.generatorType = compileMode.getGeneratorType();
                    switch (Dialogc.this.generatorType)
                    {
                    case gui.CompileModeDialog.LEX:
                        Dialogc.this.generator = new LexGenerator();
                        break;
                    case gui.CompileModeDialog.JNI:
                        Dialogc.this.generator = new JNIGenerator();
                        break;
                    default:
                        Dialogc.this.generator = new LexGenerator();
                    }
                }
            }
        });

        JMenuItem configJavacMenuItem = new JMenuItem("Java Compiler ");
        configJavacMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                                               KeyEvent.VK_F1, InputEvent.CTRL_DOWN_MASK));
        configJavacMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                fileChooser.setFileFilter(null);
                FileBrowseDialog dialog = new FileBrowseDialog(Dialogc.this,
                        "Java Compiler", compileJavaCmd, fileChooser);
                if (dialog.getChanged())
                {
                    compileJavaCmd = dialog.getSelectedFile();
                }
                fileChooser.addChoosableFileFilter(configFilter);
            }
        });
        JMenuItem configMenuItemCompileOptions = new JMenuItem(
            "Compile Options");
        configMenuItemCompileOptions.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_F2, InputEvent.CTRL_DOWN_MASK));
        configMenuItemCompileOptions.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                Object result = JOptionPane.showInputDialog(Dialogc.this,
                                "Compile Options", "Compile Options",
                                JOptionPane.PLAIN_MESSAGE, null, null, compilerArgs);
                if (result != null)
                {
                    compilerArgs = (String) result;
                }
            }
        });
        JMenuItem configJavaMenuItem = new JMenuItem("Java Runtime");
        configJavaMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                                              KeyEvent.VK_F3, InputEvent.CTRL_DOWN_MASK));
        configJavaMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                fileChooser.setFileFilter(null);
                FileBrowseDialog dialog = new FileBrowseDialog(Dialogc.this,
                        "Java Runtime", runJavaCmd, fileChooser);
                if (dialog.getChanged())
                {
                    runJavaCmd = dialog.getSelectedFile();
                }
                fileChooser.addChoosableFileFilter(configFilter);
            }
        });
        JMenuItem configJavaOptsMenuItem = new JMenuItem("Runtime Options");
        configJavaOptsMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F4, InputEvent.CTRL_DOWN_MASK));
        configJavaOptsMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                Object result = JOptionPane.showInputDialog(Dialogc.this,
                                "Runtime Options", "Runtime Options",
                                JOptionPane.PLAIN_MESSAGE, null, null, runArgs);
                if (result != null)
                {
                    runArgs = (String) result;
                }
            }
        });
        JMenuItem configWDMenuItem = new JMenuItem("Working Directory");
        configWDMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,
                                        InputEvent.CTRL_DOWN_MASK));
        configWDMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                fileChooser.setFileFilter(null);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                FileBrowseDialog dialog = new FileBrowseDialog(Dialogc.this,
                        "Working Directory", workDir, fileChooser);
                if (dialog.getChanged())
                {
                    workDir = dialog.getSelectedFile();
                    updateArguments();
                }
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.addChoosableFileFilter(configFilter);
            }
        });

        configMenu.add(configModeMenuItem);
        configMenu.add(configJavacMenuItem);
        configMenu.add(configMenuItemCompileOptions);
        configMenu.add(configJavaMenuItem);
        configMenu.add(configJavaOptsMenuItem);
        configMenu.add(configWDMenuItem);
        return configMenu;
    }

    public void newDocument()
    {
        if (modified)
        {
            switch (unsavedChangesDialog())
            {
            case SAVE:
                try
                {
                    if (!saveDocument())
                    {
                        return;
                    }
                }
                catch (IOException e)
                {
                    errorMessage(SAVE_FAILED);
                    return;
                }
                break;
            case IGNORE:
                break;
            case CANCEL:
                return;
            }
        }

        untitled = true;
        setModified(false);
        fileIndex++;
        setFilename("Document " + fileIndex);
        documentConfig = new DialogcDocument(
            DialogcDocument.DocumentType.Config);
        documentConfig.addDocumentListener(new CodeEditorDocumentListener());
        editorConfig.setDocument(documentConfig);
        editorConfig.requestFocus();
    }

    public void openDocument()
    {
        if (modified)
        {
            switch (unsavedChangesDialog())
            {
            case SAVE:
                try
                {
                    if (!saveDocument())
                    {
                        return;
                    }
                }
                catch (IOException e)
                {
                    errorMessage(SAVE_FAILED);
                    return;
                }
                break;
            case IGNORE:
                break;
            case CANCEL:
                return;
            }
        }

        try
        {
            File f = new File(workDir.getCanonicalPath());
            fileChooser.setCurrentDirectory(f);
        }
        catch (IOException e)
        {
        }
        int result = fileChooser.showOpenDialog(this);
        String contents;
        if (result != JFileChooser.APPROVE_OPTION)
        {
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();

        try
        {
            contents = loadFile(selectedFile.getPath());
            // Update working directory to parent directory of configure file
            workDir = selectedFile.getCanonicalFile().getParentFile();
        }
        catch (IOException e)
        {
            errorMessage(LOAD_FAILED);
            return;
        }


        setFilename(selectedFile.getAbsolutePath());
        documentConfig = new DialogcDocument(
            DialogcDocument.DocumentType.Config);

        try
        {
            documentConfig.insertString(0, contents, null);
        }
        catch (BadLocationException e)
        {
        }
        documentConfig.addDocumentListener(new CodeEditorDocumentListener());
        editorConfig.setDocument(documentConfig);
    }

    public boolean saveAsDocument() throws IOException
    {
        try
        {
            File f = new File(workDir.getCanonicalPath());

            fileChooser.setCurrentDirectory(f);
        }
        catch (IOException e)
        {
        }
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            setFilename(fileChooser.getSelectedFile().getPath());
            saveFile(filename);
            return true;
        }
        return false;
    }

    public boolean saveDocument() throws IOException
    {

        if (untitled)
        {
            return saveAsDocument();
        }
        else
        {
            saveFile(filename);
            return true;
        }
    }

    public void saveFile(String filename) throws IOException
    {
        try
        {
            PrintWriter writer = new PrintWriter(new BufferedWriter(
                    new FileWriter(filename)));
            writer.print(documentConfig.getText(0, documentConfig.getLength()));
            writer.close();
        }
        catch (BadLocationException e)
        {
        }

        untitled = false;
        setModified(false);
    }

    public String loadFile(String filename) throws IOException
    {
        String loadResult = "";
        Scanner scanner = new Scanner(new BufferedReader(new FileReader(
                                          filename)));
        while (scanner.hasNextLine())
        {
            loadResult += scanner.nextLine() + "\n";
        }
        scanner.close();

        untitled = false;
        setModified(false);
        return loadResult;
    }

    public void setFilename(String filename)
    {
        int dotPosition;

        this.filename = filename;

        dotPosition = filename.lastIndexOf(".");
        if (dotPosition == -1)
        {
            filenameWithoutExtension = filename;
        }
        else
        {
            filenameWithoutExtension = filename.substring(0, dotPosition);
        }
        refreshTitle();
        updateArguments();
    }

    public void refreshTitle()
    {
        setTitle((modified ? "*" : "") + TITLE + " - " + filename);
        this.notice.setText(filename + (this.modified ? " [modified]" : ""));;
    }

    public void setModified(boolean modified)
    {
        this.modified = modified;
        refreshTitle();
    }

    public void updateArguments()
    {
        String projectName = new File(filenameWithoutExtension).getName();
        compilerArgs = "-classpath " + workDir + ":" + workDir + "/"
                       + projectName + " -d " + workDir + "/" + projectName + " "
                       + workDir + "/" + projectName + "/" + projectName + ".java";
        runArgs = "-classpath " + workDir + ":" + workDir + "/" + projectName
                  + " " + projectName;
    }

    public boolean generateCode()
    {
        if (modified || untitled)
        {
            if (!untitled)
            {
                switch (unsavedChangesDialog("Unsaved changes.\n",
                                             "\nPlease save changes ! \n"))
                {
                case SAVE:
                    try
                    {
                        if (!saveDocument())
                        {
                            return false;
                        }
                    }
                    catch (IOException e)
                    {
                        errorMessage(SAVE_FAILED);
                        return false;
                    }
                    break;
                case IGNORE:
                    break;
                case CANCEL:
                    return false;
                }
            }
            else
            {
                switch (unsavedFileDialog("Unsaved changes.\n",
                                          "\nPlease save changes ! \n"))
                {
                case SAVE:
                    try
                    {
                        if (!saveDocument())
                        {
                            return false;
                        }
                    }
                    catch (IOException e)
                    {
                        errorMessage(SAVE_FAILED);
                        return false;
                    }
                    break;
                case IGNORE:
                    break;
                case CANCEL:
                    return false;
                }
            }
        }

        try
        {
            generate(filename);
        }
        catch (IOException e)
        {
            errorMessage(e.getMessage());
            return false;
        }
        catch (GenerateException e)
        {
            errorMessage(e.getMessage());
            return false;
        }

        return true;
    }

    public void generateAndRunDocument()
    {
        if (generateCode())
        {
            try
            {
                runFile(filename);
            }
            catch (IOException e)
            {
                errorMessage(e.getMessage());
            }
            catch (GenerateException e)
            {
                errorMessage(e.getMessage());
            }
        }
    }

    public void generate(String filename) throws IOException, GenerateException
    {
        boolean success = false;
        File f = null;

        f = new File(workDir + "/"
                     + new File(filenameWithoutExtension).getName());
        if (f.isDirectory())
        {
            String[] choices = { "Compile", "Don't compile" };
            int result = JOptionPane.showOptionDialog(this,
                         "A directory named '" + f.getPath()
                         + "' already exists? Compile and overwrite?",
                         "Warning", JOptionPane.YES_NO_OPTION,
                         JOptionPane.WARNING_MESSAGE, null, choices, choices[0]);
            if (result != 0)
            {
                return;
            }
        }

        try
        {
            success = f.isDirectory() || f.mkdir();
        }
        catch (SecurityException e)
        {
        }
        if (!success)
        {
            errorMessage("The project subdirectory '" + filename
                         + "' could not be created. Cancelling compilation.");
            return;
        }

        generator.generate(new File(filenameWithoutExtension), workDir,
                           compileJavaCmd.getPath(), compilerArgs);

        infoMessage("Generate and compile code successfully.");

    }

    public void runFile(String filename) throws IOException, GenerateException
    {
        generator.run(new File(filenameWithoutExtension), runJavaCmd.getPath(),
                      runArgs);
    }

    public void infoMessage(String message)
    {
        JOptionPane.showMessageDialog(this, message, "Message",
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    public void errorMessage(String message)
    {
        JOptionPane.showMessageDialog(this, message, "Error",
                                      JOptionPane.ERROR_MESSAGE);
    }

    public DialogChoice unsavedChangesDialog()
    {
        return unsavedChangesDialog("", "");
    }

    public DialogChoice unsavedChangesDialog(String preceding, String succeeding)
    {
        String[] choices = { "Save", "Continue without saving", "Cancel" };
        int result = JOptionPane.showOptionDialog(this, preceding
                     + "You have unsaved changes. Save first?" + succeeding,
                     "Warning", JOptionPane.YES_NO_CANCEL_OPTION,
                     JOptionPane.WARNING_MESSAGE, null, choices, choices[0]);
        switch (result)
        {
        case 0:
            return DialogChoice.SAVE;
        case 1:
            return DialogChoice.IGNORE;
        case 2:
        default:
            return DialogChoice.CANCEL;
        }
    }

    public DialogChoice unsavedFileDialog(String preceding, String succeeding)
    {
        String[] choices = { "Save", "Cancel" };
        int result = JOptionPane.showOptionDialog(this, preceding
                     + "You have unsaved changes. Save first?" + succeeding,
                     "Warning", JOptionPane.YES_NO_CANCEL_OPTION,
                     JOptionPane.WARNING_MESSAGE, null, choices, choices[0]);
        switch (result)
        {
        case 0:
            return DialogChoice.SAVE;
        case 1:
        default:
            return DialogChoice.CANCEL;
        }
    }

    public void quit()
    {
        if (modified)
        {
            switch (unsavedChangesDialog())
            {
            case SAVE:
                try
                {
                    if (!saveDocument())
                    {
                        return;
                    }
                }
                catch (IOException e)
                {
                    errorMessage(SAVE_FAILED);
                    return;
                }
                break;
            case IGNORE:
                break;
            case CANCEL:
                return;
            }
        }
        System.exit(0);
    }

    public void setGenerator(Generator generator)
    {
        // TODO Auto-generated method stub
        this.generator = generator;
    }

    public Generator getGenerator()
    {
        return this.generator;
    }

    class CodeEditorDocumentListener implements DocumentListener
    {

        @Override
        public void insertUpdate(DocumentEvent e)
        {
            Dialogc.this.setModified(true);
        }

        @Override
        public void removeUpdate(DocumentEvent e)
        {
            Dialogc.this.setModified(true);
        }

        @Override
        public void changedUpdate(DocumentEvent e)
        {
            Dialogc.this.setModified(true);
        }

    }

}
