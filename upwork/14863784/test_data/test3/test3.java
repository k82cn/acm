import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;

class ADDAction implements ActionListener {
    test3FieldEdit test3;
    public ADDAction(test3FieldEdit test3) {
        this.test3 = test3;
    }
    public void actionPerformed(ActionEvent e) {
       try {
           String sql = "INSERT INTO test3 (Integer1,Integer2,Float1,Float2,String1,String2) VALUE (?,?,?,?,?,?)";
                Connection con = DriverManager.getConnection("the_url_of_jdbc");
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, Integer.parseInt(test3.getInteger1()));
                stmt.setInt(2, Integer.parseInt(test3.getInteger2()));
                stmt.setDouble(3, Double.parseDouble(test3.getFloat1()));
                stmt.setDouble(4, Double.parseDouble(test3.getFloat2()));
                stmt.setString(5, test3.getString1());
                stmt.setString(6, test3.getString2());
                stmt.executeUpdate(sql);
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) { 
                    test3.__setId(rs.getLong(1));
                }
                test3.appendToStatusArea("Insert value into database as " + test3.__getId());
           } catch (Exception e1) {
                test3.appendToStatusArea(e1.getMessage());
           }
     }
}
class DELETEAction implements ActionListener {
    test3FieldEdit test3;
    public DELETEAction(test3FieldEdit test3) {
        this.test3 = test3;
    }
    public void actionPerformed(ActionEvent e) {
        try {
            String sql = "DELETE FROM test3  WHERE Integer1 = " + test3.getDCInteger1() + " AND Integer2 = " + test3.getDCInteger2() + " AND Float1 = " + test3.getDCFloat1() + " AND Float2 = " + test3.getDCFloat2() + " AND String1 = " + test3.getDCString1() + " AND String2 = " + test3.getDCString2() + "";
            Connection con = DriverManager.getConnection("the_url_of_jdbc");
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e1) { 
            test3.appendToStatusArea(e1.getMessage());
        } 
    }
}
class UPDATEAction implements ActionListener {
    test3FieldEdit test3;
    public UPDATEAction (test3FieldEdit test3) {
        this.test3 = test3;
    }
    public void actionPerformed(ActionEvent e) {
        String sql = "UPDATE test3 SET Integer1 = \"" + test3.getInteger1() + "\", Integer2 = \"" + test3.getInteger2() + "\", Float1 = \"" + test3.getFloat1() + "\", Float2 = \"" + test3.getFloat2() + "\", String1 = \"" + test3.getString1() + "\", String2 = \"" + test3.getString2() + "\"" + " WHERE id = " + test3.__getId() ;
        try {
            Connection con = DriverManager.getConnection("the_url_of_jdbc");
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e1) { 
            test3.appendToStatusArea(e1.getMessage());
        }
     }
}
class QUERYAction implements  ActionListener {
    test3FieldEdit test3;
    public QUERYAction (test3FieldEdit test3) {
        this.test3 = test3;
    }
    public void actionPerformed(ActionEvent e) {
        try { 
        String sql = "SELECT * FROM test3  WHERE  Integer1 = " + test3.getDCInteger1() + " AND  Integer2 = " + test3.getDCInteger2() + " AND  Float1 = " + test3.getDCFloat1() + " AND  Float2 = " + test3.getDCFloat2() + " AND  String1 = " + test3.getDCString1() + " AND  String2 = " + test3.getDCString2() + "";
            Connection con = DriverManager.getConnection("the_url_of_jdbc");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                test3.appendToStatusArea("Integer1 = " + rs.getString("Integer1"));
                test3.appendToStatusArea("Integer2 = " + rs.getString("Integer2"));
                test3.appendToStatusArea("Float1 = " + rs.getString("Float1"));
                test3.appendToStatusArea("Float2 = " + rs.getString("Float2"));
                test3.appendToStatusArea("String1 = " + rs.getString("String1"));
                test3.appendToStatusArea("String2 = " + rs.getString("String2"));
            }
        } catch (Exception e1) {
            test3.appendToStatusArea(e1.getMessage());
        }
    }
}
interface test3FieldEdit
{
	public void appendToStatusArea(String msg);
	public void __setId(long id);
	public long __getId();
	public void setInteger1(String Integer1);
	public void setDCInteger1(String Integer1);
	public String getInteger1();
	public String getDCInteger1() throws IllegalFieldValueException;
	public void setInteger2(String Integer2);
	public void setDCInteger2(String Integer2);
	public String getInteger2();
	public String getDCInteger2() throws IllegalFieldValueException;
	public void setFloat1(String Float1);
	public void setDCFloat1(String Float1);
	public String getFloat1();
	public String getDCFloat1() throws IllegalFieldValueException;
	public void setFloat2(String Float2);
	public void setDCFloat2(String Float2);
	public String getFloat2();
	public String getDCFloat2() throws IllegalFieldValueException;
	public void setString1(String String1);
	public void setDCString1(String String1);
	public String getString1();
	public String getDCString1() throws IllegalFieldValueException;
	public void setString2(String String2);
	public void setDCString2(String String2);
	public String getString2();
	public String getDCString2() throws IllegalFieldValueException;
}
public class test3 extends JFrame implements test3FieldEdit
{
private long __id;
public void __setId(long id) { this.__id = id; }
public long __getId() { return this.__id; }
	// The status area
	JTextArea statusArea;
	// Fields
	private JLabel Integer1_label;
	private JTextField Integer1_field;
	private JLabel Integer2_label;
	private JTextField Integer2_field;
	private JLabel Float1_label;
	private JTextField Float1_field;
	private JLabel Float2_label;
	private JTextField Float2_field;
	private JLabel String1_label;
	private JTextField String1_field;
	private JLabel String2_label;
	private JTextField String2_field;

	// Buttons
	private JButton DefaultButton_button;

	// Constructor
	public test3()
	{
		super("test3");
		JPanel fieldsPanel = new JPanel(new BorderLayout());
		JPanel buttonsPanel = new JPanel();
		JPanel upperPanel = new JPanel(new BorderLayout());
		JPanel statusPanel = new JPanel(new BorderLayout());
		upperPanel.add(fieldsPanel, BorderLayout.NORTH);
		upperPanel.add(buttonsPanel, BorderLayout.CENTER);
		getContentPane().add(upperPanel, BorderLayout.NORTH);
		getContentPane().add(statusPanel, BorderLayout.CENTER);
		JPanel labelPanel = new JPanel(new GridLayout(6, 1));
		JPanel textFieldPanel = new JPanel(new GridLayout(6, 1));
		fieldsPanel.add(labelPanel, BorderLayout.WEST);
		fieldsPanel.add(textFieldPanel, BorderLayout.CENTER);
		Integer1_label = new JLabel("Integer1", JLabel.RIGHT);
		Integer1_field = new JTextField(20);
		Integer1_label.setLabelFor(Integer1_field);
		labelPanel.add(Integer1_label);
		textFieldPanel.add(Integer1_field);
				Integer2_label = new JLabel("Integer2", JLabel.RIGHT);
		Integer2_field = new JTextField(20);
		Integer2_label.setLabelFor(Integer2_field);
		labelPanel.add(Integer2_label);
		textFieldPanel.add(Integer2_field);
				Float1_label = new JLabel("Float1", JLabel.RIGHT);
		Float1_field = new JTextField(20);
		Float1_label.setLabelFor(Float1_field);
		labelPanel.add(Float1_label);
		textFieldPanel.add(Float1_field);
				Float2_label = new JLabel("Float2", JLabel.RIGHT);
		Float2_field = new JTextField(20);
		Float2_label.setLabelFor(Float2_field);
		labelPanel.add(Float2_label);
		textFieldPanel.add(Float2_field);
				String1_label = new JLabel("String1", JLabel.RIGHT);
		String1_field = new JTextField(20);
		String1_label.setLabelFor(String1_field);
		labelPanel.add(String1_label);
		textFieldPanel.add(String1_field);
				String2_label = new JLabel("String2", JLabel.RIGHT);
		String2_field = new JTextField(20);
		String2_label.setLabelFor(String2_field);
		labelPanel.add(String2_label);
		textFieldPanel.add(String2_field);
				DefaultButton_button = new JButton("DefaultButton");
		DefaultButton_button.addActionListener(new test3Default(this));
		buttonsPanel.add(DefaultButton_button);
		
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

	public void setInteger2(String Integer2)
	{
		Integer2_field.setText(Integer2);
	}
	public void setDCInteger2(String Integer2)
	{
		Integer2_field.setText(Integer2);
	}
	public String getInteger2()
	{
		return Integer2_field.getText();
	}
	public String getDCInteger2() throws IllegalFieldValueException
	{
		 try { Integer.parseInt(Integer2_field.getText()); }			catch(Exception e) { throw new IllegalFieldValueException( Integer2_field.getText() + " is not a integer"); };		return Integer2_field.getText();
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

	public void setFloat2(String Float2)
	{
		Float2_field.setText(Float2);
	}
	public void setDCFloat2(String Float2)
	{
		Float2_field.setText(Float2);
	}
	public String getFloat2()
	{
		return Float2_field.getText();
	}
	public String getDCFloat2() throws IllegalFieldValueException
	{
		 try { Float.parseFloat(Float2_field.getText()); }			catch(Exception e) { throw new IllegalFieldValueException( Float2_field.getText() + " is not a float" ); };		return Float2_field.getText();
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

	public void setString2(String String2)
	{
		String2_field.setText(String2);
	}
	public void setDCString2(String String2)
	{
		String2_field.setText(String2);
	}
	public String getString2()
	{
		return String2_field.getText();
	}
	public String getDCString2() throws IllegalFieldValueException
	{
		return String2_field.getText();
	}

	public void appendToStatusArea(String message)
	{
		statusArea.append(message + "\n");
	}
		// Main method.
	public static void main(String[] args)
	{
		new test3();
	}
	}
