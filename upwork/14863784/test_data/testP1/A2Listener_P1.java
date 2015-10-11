import java.awt.event.*;

public class A2Listener_P1 implements ActionListener
{

	testP1 d;

	public A2Listener_P1(testP1 d)
	{
		this.d = d;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			d.setDCName("Doug");
			d.setDCsID("a2ID");
			d.setDCA1("a2A1");
			d.setDCA2("a2A2");
			d.setDCA3("a2A3");
			d.setDCA4("a2A4");
			d.setDCsID("a2A5");
			d.appendToStatusArea("button pressed.");
		}
    
		catch(Exception tr)
		{
			d.appendToStatusArea("ERROR: " + tr.getMessage());
		}

	}

}
