import java.awt.event.*;

public class A2Listener_P3 implements ActionListener
{

	testP3 d;

	public A2Listener_P3(testP3 d)
	{
		this.d = d;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			d.setDCF_Name("Doug");
      d.setDCL_Name("L_D");
      
			d.appendToStatusArea("button pressed.");
		}
		catch(Exception tr)
		{
			d.appendToStatusArea("ERROR");
		}

	}

}
