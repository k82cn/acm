import java.awt.*;
import javax.swing.*;
public class testP1 extends JFrame
{
	// The status area
	JTextArea statusArea;
	// Fields
	private JLabel Name_label;
	private JTextField Name_field;
	private JLabel sID_label;
	private JTextField sID_field;
	private JLabel A1_label;
	private JTextField A1_field;
	private JLabel A2_label;
	private JTextField A2_field;
	private JLabel A3_label;
	private JTextField A3_field;
	private JLabel A4_label;
	private JTextField A4_field;

	// Buttons
	private JButton Add_button;
	private JButton Update_button;
	private JButton Delete_button;
	private JButton Query_button;

	// Constructor
	public testP1()
	{
		super("testPro1");
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
		Name_label = new JLabel("Name", JLabel.RIGHT);
		Name_field = new JTextField(20);
		Name_label.setLabelFor(Name_field);
		labelPanel.add(Name_label);
		textFieldPanel.add(Name_field);
				sID_label = new JLabel("sID", JLabel.RIGHT);
		sID_field = new JTextField(20);
		sID_label.setLabelFor(sID_field);
		labelPanel.add(sID_label);
		textFieldPanel.add(sID_field);
				A1_label = new JLabel("A1", JLabel.RIGHT);
		A1_field = new JTextField(20);
		A1_label.setLabelFor(A1_field);
		labelPanel.add(A1_label);
		textFieldPanel.add(A1_field);
				A2_label = new JLabel("A2", JLabel.RIGHT);
		A2_field = new JTextField(20);
		A2_label.setLabelFor(A2_field);
		labelPanel.add(A2_label);
		textFieldPanel.add(A2_field);
				A3_label = new JLabel("A3", JLabel.RIGHT);
		A3_field = new JTextField(20);
		A3_label.setLabelFor(A3_field);
		labelPanel.add(A3_label);
		textFieldPanel.add(A3_field);
				A4_label = new JLabel("A4", JLabel.RIGHT);
		A4_field = new JTextField(20);
		A4_label.setLabelFor(A4_field);
		labelPanel.add(A4_label);
		textFieldPanel.add(A4_field);
				Add_button = new JButton("Add");
		Add_button.addActionListener(new A2Listener_P1(this));
		buttonsPanel.add(Add_button);
		
		Update_button = new JButton("Update");
		Update_button.addActionListener(new A2Listener_P1(this));
		buttonsPanel.add(Update_button);
		
		Delete_button = new JButton("Delete");
		Delete_button.addActionListener(new A2Listener_P1(this));
		buttonsPanel.add(Delete_button);
		
		Query_button = new JButton("Query");
		Query_button.addActionListener(new A2Listener_P1(this));
		buttonsPanel.add(Query_button);
		
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
	public void setName(String Name)
	{
		Name_field.setText(Name);
	}
	public String getName()
	{
		return Name_field.getText();
	}
	public String getDCName() throws IllegalFieldValueException
	{
		return Name_field.getText();
	}

	public void setDCName(String Name) throws IllegalFieldValueException 
	{
		Name_field.setText(Name);
	}

	public void setsID(String sID)
	{
		sID_field.setText(sID);
	}
	public String getsID()
	{
		return sID_field.getText();
	}
	public String getDCsID() throws IllegalFieldValueException
	{
		return sID_field.getText();
	}

	public void setDCsID(String sID) throws IllegalFieldValueException 
	{
		sID_field.setText(sID);
	}

	public void setA1(String A1)
	{
		A1_field.setText(A1);
	}
	public String getA1()
	{
		return A1_field.getText();
	}
	public String getDCA1() throws IllegalFieldValueException
	{
		 try { Integer.parseInt(A1_field.getText()); }			catch(Exception e) { throw new IllegalFieldValueException( A1_field.getText() + " is not a integer"); };		return A1_field.getText();
	}

	public void setDCA1(String A1) throws IllegalFieldValueException 
	{
		 try { Integer.parseInt(A1); }			catch(Exception e) { throw new IllegalFieldValueException( A1 + " is not a integer"); };		A1_field.setText(A1);
	}

	public void setA2(String A2)
	{
		A2_field.setText(A2);
	}
	public String getA2()
	{
		return A2_field.getText();
	}
	public String getDCA2() throws IllegalFieldValueException
	{
		 try { Integer.parseInt(A2_field.getText()); }			catch(Exception e) { throw new IllegalFieldValueException( A2_field.getText() + " is not a integer"); };		return A2_field.getText();
	}

	public void setDCA2(String A2) throws IllegalFieldValueException 
	{
		 try { Integer.parseInt(A2); }			catch(Exception e) { throw new IllegalFieldValueException( A2 + " is not a integer"); };		A2_field.setText(A2);
	}

	public void setA3(String A3)
	{
		A3_field.setText(A3);
	}
	public String getA3()
	{
		return A3_field.getText();
	}
	public String getDCA3() throws IllegalFieldValueException
	{
		 try { Integer.parseInt(A3_field.getText()); }			catch(Exception e) { throw new IllegalFieldValueException( A3_field.getText() + " is not a integer"); };		return A3_field.getText();
	}

	public void setDCA3(String A3) throws IllegalFieldValueException 
	{
		 try { Integer.parseInt(A3); }			catch(Exception e) { throw new IllegalFieldValueException( A3 + " is not a integer"); };		A3_field.setText(A3);
	}

	public void setA4(String A4)
	{
		A4_field.setText(A4);
	}
	public String getA4()
	{
		return A4_field.getText();
	}
	public String getDCA4() throws IllegalFieldValueException
	{
		 try { Integer.parseInt(A4_field.getText()); }			catch(Exception e) { throw new IllegalFieldValueException( A4_field.getText() + " is not a integer"); };		return A4_field.getText();
	}

	public void setDCA4(String A4) throws IllegalFieldValueException 
	{
		 try { Integer.parseInt(A4); }			catch(Exception e) { throw new IllegalFieldValueException( A4 + " is not a integer"); };		A4_field.setText(A4);
	}

	public void appendToStatusArea(String message)
	{
		statusArea.append(message + "\n");
	}
		// Main method.
	public static void main(String[] args)
	{
		new testP1();
	}
	}
