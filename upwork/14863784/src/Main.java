import except.GenerateException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;

class AddActions implements ActionListener {


    public void actionPerformed(ActionEvent e) {

        String sql = "INSERT";

        try {
            Connection con = DriverManager.getConnection("");
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);



            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next())
            {
               // .setId(rs.getLong(1));
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
            return;
        }

    }
}

class DeleteActions implements ActionListener
{
    public void actionPerformed(ActionEvent e) {

        String sql = "DELETE";
        try {
            Connection con = DriverManager.getConnection("");
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}


class UpdateAction implements ActionListener
{
    public void actionPerformed(ActionEvent e) {

        String sql = "UPDATE set ";

        try {
            Connection con = DriverManager.getConnection("");
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e1) {
            e1.printStackTrace();
            return;
        }

    }
}

class QueryAction implements  ActionListener
{
    public void actionPerformed(ActionEvent e) {

        String sql = "SELECT * from '" + "test" + "' WHERE "+
                "aa"+"=" + "";

        try {
            Connection con = DriverManager.getConnection("");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                String aa = rs.getString("");


            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}

public class Main {

    public static void main(String[] args) throws IOException, GenerateException {

    }

}
