import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;

class ADDAction implements ActionListener {
    test2dbFieldEdit test2db;
    public ADDAction(test2dbFieldEdit test2db) {
        this.test2db = test2db;
    }
    public void actionPerformed(ActionEvent e) {
       try {
           String sql = "INSERT INTO test2db (DefaultField) VALUE (?)";
                Connection con = DriverManager.getConnection("the_url_of_jdbc");
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, test2db.getDefaultField());
                stmt.executeUpdate(sql);
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) { 
                    test2db.__setId(rs.getLong(1));
                }
                test2db.appendToStatusArea("Insert value into database as " + test2db.__getId());
           } catch (Exception e1) {
                test2db.appendToStatusArea(e1.getMessage());
           }
     }
}
class DELETEAction implements ActionListener {
    test2dbFieldEdit test2db;
    public DELETEAction(test2dbFieldEdit test2db) {
        this.test2db = test2db;
    }
    public void actionPerformed(ActionEvent e) {
        try {
            String sql = "DELETE FROM test2db  WHERE DefaultField = " + test2db.getDCDefaultField() + "";
            Connection con = DriverManager.getConnection("the_url_of_jdbc");
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e1) { 
            test2db.appendToStatusArea(e1.getMessage());
        } 
    }
}
class UPDATEAction implements ActionListener {
    test2dbFieldEdit test2db;
    public UPDATEAction (test2dbFieldEdit test2db) {
        this.test2db = test2db;
    }
    public void actionPerformed(ActionEvent e) {
        String sql = "UPDATE test2db SET DefaultField = \"" + test2db.getDefaultField() + "\"" + " WHERE id = " + test2db.__getId() ;
        try {
            Connection con = DriverManager.getConnection("the_url_of_jdbc");
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e1) { 
            test2db.appendToStatusArea(e1.getMessage());
        }
     }
}
class QUERYAction implements  ActionListener {
    test2dbFieldEdit test2db;
    public QUERYAction (test2dbFieldEdit test2db) {
        this.test2db = test2db;
    }
    public void actionPerformed(ActionEvent e) {
        try { 
        String sql = "SELECT * FROM test2db  WHERE  DefaultField = " + test2db.getDCDefaultField() + "";
            Connection con = DriverManager.getConnection("the_url_of_jdbc");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                test2db.appendToStatusArea("DefaultField = " + rs.getString("DefaultField"));
            }
        } catch (Exception e1) {
            test2db.appendToStatusArea(e1.getMessage());
        }
    }
}
interface test2dbFieldEdit
{
	public void appendToStatusArea(String msg);
	public void __setId(long id);
	public long __getId();
	public void setDefaultField(String DefaultField);
	public void setDCDefaultField(String DefaultField);
	public String getDefaultField();
	public String getDCDefaultField() throws IllegalFieldValueException;
}
public class test2db extends JFrame implements test2dbFieldEdit
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
	private JButton ADD_button;
	private JButton DELETE_button;
	private JButton QUERY_button;
	private JButton UPDATE_button;

	// Constructor
	public test2db()
	{
		super("test2db");
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
		Click_button.addActionListener(new test2dbClick(this));
		buttonsPanel.add(Click_button);
		
		ADD_button = new JButton("ADD");
		ADD_button.addActionListener(new ADDAction(this));
		buttonsPanel.add(ADD_button);
		
		DELETE_button = new JButton("DELETE");
		DELETE_button.addActionListener(new DELETEAction(this));
		buttonsPanel.add(DELETE_button);
		
		QUERY_button = new JButton("QUERY");
		QUERY_button.addActionListener(new QUERYAction(this));
		buttonsPanel.add(QUERY_button);
		
		UPDATE_button = new JButton("UPDATE");
		UPDATE_button.addActionListener(new UPDATEAction(this));
		buttonsPanel.add(UPDATE_button);
		
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
		new test2db();
	}
	}
