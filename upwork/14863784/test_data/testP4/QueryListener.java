import java.awt.event.*;

public class QueryListener implements ActionListener
{

    testP4 d;

    public QueryListener(testP4 d)
    {
        this.d = d;
    }
    
    public void actionPerformed(ActionEvent ev)
    {
        try{
            d.appendToStatusArea("MemberNumber = " + d.getDCMemberNumber());
        }catch(Exception e)
        {
            d.appendToStatusArea("MemberNumber = incorrect type (" + e.getMessage() + ")");
        }

        try{
            d.appendToStatusArea("Last Name = " + d.getDCLastName());
        }catch(Exception e)
        {
            d.appendToStatusArea("Last Name = incorrect type (" + e.getMessage() + ")");
        }
        try{
            d.appendToStatusArea("First Name = " + d.getDCFirstName());
        }catch(Exception e)
        {
            d.appendToStatusArea("First Name = incorrect type (" + e.getMessage() + ")");
        }
        try{
            d.appendToStatusArea("Address = " + d.getDCAddress());
        }catch(Exception e)
        {
            d.appendToStatusArea("Address = incorrect type (" + e.getMessage() + ")");
        }
        try{
            d.appendToStatusArea("Total Rentals = " + d.getDCTotalRentals());
        }catch(Exception e)
        {
            d.appendToStatusArea("Total Rentals = incorrect type (" + e.getMessage() + ")");
        }
        try{
            d.appendToStatusArea("Account Balance = " + d.getDCAccount_balance());
        }catch(Exception e)
        {
            d.appendToStatusArea("Account Balance = incorrect type (" + e.getMessage() + ")");
        }

    }

}
