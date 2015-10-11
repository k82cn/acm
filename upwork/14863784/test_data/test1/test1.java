import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;

class ADDAction implements ActionListener {
    test1FieldEdit test1;
    public ADDAction(test1FieldEdit test1) {
        this.test1 = test1;
    }
    public void actionPerformed(ActionEvent e) {
       try {
           String sql = "INSERT INTO test1 (Integer1,Float1,String1) VALUE (?,?,?)";
                Connection con = DriverManager.getConnection("the_url_of_jdbc");
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, Integer.parseInt(test1.getInteger1()));
                stmt.setDouble(2, Double.parseDouble(test1.getFloat1()));
                stmt.setString(3, test1.getString1());
                stmt.executeUpdate(sql);
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) { 
                    test1.__setId(rs.getLong(1));
                }
                test1.appendToStatusArea("Insert value into database as " + test1.__getId());
           } catch (Exception e1) {
                test1.appendToStatusArea(e1.getMessage());
           }
     }
}
class DELETEAction implements ActionListener {
    test1FieldEdit test1;
    public DELETEAction(test1FieldEdit test1) {
        this.test1 = test1;
    }
    public void actionPerformed(ActionEvent e) {
        try {
            String sql = "DELETE FROM test1  WHERE Integer1 = " + test1.getDCInteger1() + " AND Float1 = " + test1.getDCFloat1() + " AND String1 = " + test1.getDCString1() + "";
            Connection con = DriverManager.getConnection("the_url_of_jdbc");
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e1) { 
            test1.appendToStatusArea(e1.getMessage());
        } 
    }
}
class UPDATEAction implements ActionListener {
    test1FieldEdit test1;
    public UPDATEAction (test1FieldEdit test1) {
        this.test1 = test1;
    }
    public void actionPerformed(ActionEvent e) {
        String sql = "UPDATE test1 SET Integer1 = \"" + test1.getInteger1() + "\", Float1 = \"" + test1.getFloat1() + "\", String1 = \"" + test1.getString1() + "\"" + " WHERE id = " + test1.__getId() ;
        try {
            Connection con = DriverManager.getConnection("the_url_of_jdbc");
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e1) { 
            test1.appendToStatusArea(e1.getMessage());
        }
     }
}
class QUERYAction implements  ActionListener {
    test1FieldEdit test1;
    public QUERYAction (test1FieldEdit test1) {
        this.test1 = test1;
    }
    public void actionPerformed(ActionEvent e) {
        try { 
        String sql = "SELECT * FROM test1  WHERE  Integer1 = " + test1.getDCInteger1() + " AND  Float1 = " + test1.getDCFloat1() + " AND  String1 = " + test1.getDCString1() + "";
            Connection con = DriverManager.getConnection("the_url_of_jdbc");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                test1.appendToStatusArea("Integer1 = " + rs.getString("Integer1"));
                test1.appendToStatusArea("Float1 = " + rs.getString("Float1"));
                test1.appendToStatusArea("String1 = " + rs.getString("String1"));
            }
        } catch (Exception e1) {
            test1.appendToStatusArea(e1.getMessage());
        }
    }
}
interface test1FieldEdit
{
	public void appendToStatusArea(String msg);
	public void __setId(long id);
	public long __getId();
	public void setInteger1(String Integer1);
	public void setDCInteger1(String Integer1);
	public String getInteger1();
	public String getDCInteger1() throws IllegalFieldValueException;
	public void setFloat1(String Float1);
	public void setDCFloat1(String Float1);
	public String getFloat1();
	public String getDCFloat1() throws IllegalFieldValueException;
	public void setString1(String String1);
	public void setDCString1(String String1);
	public String getString1();
	public String getDCString1() throws IllegalFieldValueException;
}
public class test1 extends JFrame implements test1FieldEdit
{
private long __id;
public void __setId(long id) { this.__id = id; }
public long __getId() { return this.__id; }
	// The status area
	JTextArea statusArea;
	// Fields
	private JLabel Integer1_label;
	private JTextField Integer1_field;
	private JLabel Float1_label;
	private JTextField Float1_field;
	private JLabel String1_label;
	private JTextField String1_field;

	// Buttons
	private JButton Set_button;
	private JButton Get_button;

	// Constructor
	public test1()
	{
		super("test1");
		JPanel fieldsPanel = new JPanel(new BorderLayout());
		JPanel buttonsPanel = new JPanel();
		JPanel upperPanel = new JPanel(new BorderLayout());
		JPanel statusPanel = new JPanel(new BorderLayout());
		upperPanel.add(fieldsPanel, BorderLayout.NORTH);
		upperPanel.add(buttonsPanel, BorderLayout.CENTER);
		getContentPane().add(upperPanel, BorderLayout.NORTH);
		getContentPane().add(statusPanel, BorderLayout.CENTER);
		JPanel labelPanel = new JPanel(new GridLayout(3, 1));
		JPanel textFieldPanel = new JPanel(new GridLayout(3, 1));
		fieldsPanel.add(labelPanel, BorderLayout.WEST);
		fieldsPanel.add(textFieldPanel, BorderLayout.CENTER);
		Integer1_label = new JLabel("Integer1", JLabel.RIGHT);
		Integer1_field = new JTextField(20);
		Integer1_label.setLabelFor(Integer1_field);
		labelPanel.add(Integer1_label);
		textFieldPanel.add(Integer1_field);
				Float1_label = new JLabel("Float1", JLabel.RIGHT);
		Float1_field = new JTextField(20);
		Float1_label.setLabelFor(Float1_field);
		labelPanel.add(Float1_label);
		textFieldPanel.add(Float1_field);
				String1_label = new JLabel("String1", JLabel.RIGHT);
		String1_field = new JTextField(20);
		String1_label.setLabelFor(String1_field);
		labelPanel.add(String1_label);
		textFieldPanel.add(String1_field);
				Set_button = new JButton("Set");
		Set_button.addActionListener(new test1Set(this));
		buttonsPanel.add(Set_button);
		
		Get_button = new JButton("Get");
		Get_button.addActionListener(new test1Get(this));
		buttonsPanel.add(Get_button);
		
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
	public void setInteger1(String Integer1)
	{
		Integer1_field.setText(Integer1);
	}
	public void setDCInteger1(String Integer1)
	{
		Integer1_field.setText(Integer1);
	}
	public String getInteger1()
	{
		return Integer1_field.getText();
	}
	public String getDCInteger1() throws IllegalFieldValueException
	{
		 try { Integer.parseInt(Integer1_field.getText()); }			catch(Exception e) { throw new IllegalFieldValueException( Integer1_field.getText() + " is not a integer"); };		return Integer1_field.getText();
	}

	public void setFloat1(String Float1)
	{
		Float1_field.setText(Float1);
	}
	public void setDCFloat1(String Float1)
	{
		Float1_field.setText(Float1);
	}
	public String getFloat1()
	{
		return Float1_field.getText();
	}
	public String getDCFloat1() throws IllegalFieldValueException
	{
		 try { Float.parseFloat(Float1_field.getText()); }			catch(Exception e) { throw new IllegalFieldValueException( Float1_field.getText() + " is not a float" ); };		return Float1_field.getText();
	}

	public void setString1(String String1)
	{
		String1_field.setText(String1);
	}
	public void setDCString1(String String1)
	{
		String1_field.setText(String1);
	}
	public String getString1()
	{
		return String1_field.getText();
	}
	public String getDCString1() throws IllegalFieldValueException
	{
		return String1_field.getText();
	}

	public void appendToStatusArea(String message)
	{
		statusArea.append(message + "\n");
	}
		// Main method.
	public static void main(String[] args)
	{
		new test1();
	}
	}
