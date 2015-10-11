package gui;


import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;



public class CompileModeDialog extends JDialog implements ActionListener
{
    public static final int LEX = 0;
    public static final int JNI = 1;
    private boolean changed;
    private int generatorType;
    private JRadioButton lexButton;
    private
    JRadioButton jniButton;

    public CompileModeDialog(Frame owner, String title, int gt)
    {

        super(owner, title, true);


        JPanel panel = new JPanel(new BorderLayout());

        lexButton = new JRadioButton("Lex/Yacc");
        lexButton.setActionCommand("Lex/Yacc");
        lexButton.setSelected(LEX == gt);

        jniButton = new JRadioButton("JNI");

        jniButton.setActionCommand("JNI");
        jniButton.setSelected(JNI == gt);

        //Group the radio buttons.
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(jniButton);
        radioGroup.add(lexButton);

        //Register a listener for the radio buttons.
        jniButton.addActionListener(this);
        lexButton.addActionListener(this);


        panel.add(jniButton, BorderLayout.CENTER);
        panel.add(lexButton, BorderLayout.WEST);

        JPanel bottomButtons = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setChanged(true);
                dispose();
            }
        });
        bottomButtons.add(okButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setChanged(false);
                dispose();
            }
        });
        bottomButtons.add(cancelButton);
        panel.add(bottomButtons, BorderLayout.SOUTH);

        getContentPane().add(panel, BorderLayout.NORTH);

        setSize(400, 100);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent windowEvent)
            {
                setChanged(false);
                dispose();
            }
        });
        setVisible(true);
    }

    public boolean getChanged()
    {
        return changed;
    }

    public void setChanged(boolean changed)
    {
        this.changed = changed;
    }

    public int getGeneratorType()
    {
        return this.generatorType;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // TODO Auto-generated method stub
        if (this.jniButton.isSelected())
        {
            this.generatorType = JNI;
        }
        else if (this.lexButton.isSelected())
        {
            this.generatorType = LEX;
        }
    }
}
