import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;

class ADDAction implements ActionListener {
    test2FieldEdit test2;
    public ADDAction(test2FieldEdit test2) {
        this.test2 = test2;
    }
    public void actionPerformed(ActionEvent e) {
       try {
           String sql = "INSERT INTO test2 (DefaultField) VALUE (?)";
                Connection con = DriverManager.getConnection("the_url_of_jdbc");
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, test2.getDefaultField());
                stmt.executeUpdate(sql);
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) { 
                    test2.__setId(rs.getLong(1));
                }
                test2.appendToStatusArea("Insert value into database as " + test2.__getId());
           } catch (Exception e1) {
                test2.appendToStatusArea(e1.getMessage());
           }
     }
}
class DELETEAction implements ActionListener {
    test2FieldEdit test2;
    public DELETEAction(test2FieldEdit test2) {
        this.test2 = test2;
    }
    public void actionPerformed(ActionEvent e) {
        try {
            String sql = "DELETE FROM test2  WHERE DefaultField = " + test2.getDCDefaultField() + "";
            Connection con = DriverManager.getConnection("the_url_of_jdbc");
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e1) { 
            test2.appendToStatusArea(e1.getMessage());
        } 
    }
}
class UPDATEAction implements ActionListener {
    test2FieldEdit test2;
    public UPDATEAction (test2FieldEdit test2) {
        this.test2 = test2;
    }
    public void actionPerformed(ActionEvent e) {
        String sql = "UPDATE test2 SET DefaultField = \"" + test2.getDefaultField() + "\"" + " WHERE id = " + test2.__getId() ;
        try {
            Connection con = DriverManager.getConnection("the_url_of_jdbc");
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e1) { 
            test2.appendToStatusArea(e1.getMessage());
        }
     }
}
class QUERYAction implements  ActionListener {
    test2FieldEdit test2;
    public QUERYAction (test2FieldEdit test2) {
        this.test2 = test2;
    }
    public void actionPerformed(ActionEvent e) {
        try { 
        String sql = "SELECT * FROM test2  WHERE  DefaultField = " + test2.getDCDefaultField() + "";
            Connection con = DriverManager.getConnection("the_url_of_jdbc");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                test2.appendToStatusArea("DefaultField = " + rs.getString("DefaultField"));
            }
        } catch (Exception e1) {
            test2.appendToStatusArea(e1.getMessage());
        }
    }
}
interface test2FieldEdit
{
	public void appendToStatusArea(String msg);
	public void __setId(long id);
	public long __getId();
	public void setDefaultField(String DefaultField);
	public void setDCDefaultField(String DefaultField);
	public String getDefaultField();
	public String getDCDefaultField() throws IllegalFieldValueException;
}
public class test2 extends JFrame implements test2FieldEdit
{
private long __id;
public void __setId(long id) { this.__id = id; }
public long __getId() { return this.__id; }
	// The status area
	JTextArea statusArea;
	// Fields
	private JLabel DefaultField_label;
	private JTextField DefaultField_field;

	// Buttons
	private JButton Click_button;

	// Constructor
	public test2()
	{
		super("test2");
		JPanel fieldsPanel = new JPanel(new BorderLayout());
		JPanel buttonsPanel = new JPanel();
		JPanel upperPanel = new JPanel(new BorderLayout());
		JPanel statusPanel = new JPanel(new BorderLayout());
		upperPanel.add(fieldsPanel, BorderLayout.NORTH);
		upperPanel.add(buttonsPanel, BorderLayout.CENTER);
		getContentPane().add(upperPanel, BorderLayout.NORTH);
		getContentPane().add(statusPanel, BorderLayout.CENTER);
		JPanel labelPanel = new JPanel(new GridLayout(1, 1));
		JPanel textFieldPanel = new JPanel(new GridLayout(1, 1));
		fieldsPanel.add(labelPanel, BorderLayout.WEST);
		fieldsPanel.add(textFieldPanel, BorderLayout.CENTER);
		DefaultField_label = new JLabel("DefaultField", JLabel.RIGHT);
		DefaultField_field = new JTextField(20);
		DefaultField_label.setLabelFor(DefaultField_field);
		labelPanel.add(DefaultField_label);
		textFieldPanel.add(DefaultField_field);
				Click_button = new JButton("Click");
		Click_button.addActionListener(new test2Click(this));
		buttonsPanel.add(Click_button);
		
		statusPanel.add(new JLabel("Status", JLabel.CENTER), BorderLayout.NORTH);
		statusArea = new JTextArea();
		statusArea.setLineWrap(true);
		statusArea.setEditable(false);
		JScrollPane statusScroller = new JScrollPane(statusArea);
		statusPanel.add(statusScroller, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
		setVisible(true);
	}
	public void setDefaultField(String DefaultField)
	{
		DefaultField_field.setText(DefaultField);
	}
	public void setDCDefaultField(String DefaultField)
	{
		DefaultField_field.setText(DefaultField);
	}
	public String getDefaultField()
	{
		return DefaultField_field.getText();
	}
	public String getDCDefaultField() throws IllegalFieldValueException
	{
		return DefaultField_field.getText();
	}

	public void appendToStatusArea(String message)
	{
		statusArea.append(message + "\n");
	}
		// Main method.
	public static void main(String[] args)
	{
		new test2();
	}
	}
