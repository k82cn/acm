
import java.awt.*;
import javax.swing.text.*;

public class DialogcDocument extends DefaultStyledDocument
{
    public enum DocumentType
    {
        Config, Setup
    };

    private SimpleAttributeSet normalText;
    private SimpleAttributeSet commentText;
    private SimpleAttributeSet delimiterText;

    public DialogcDocument(DocumentType documentType)
    {
        normalText = new SimpleAttributeSet();
        StyleConstants.setForeground(normalText, Color.black);

        commentText = new SimpleAttributeSet();
        StyleConstants.setForeground(commentText, new Color(0x00, 0xc0, 0x80));
        StyleConstants.setBackground(commentText, new Color(0xe8, 0xff, 0xf3));

        delimiterText = new SimpleAttributeSet();
        StyleConstants
        .setForeground(delimiterText, new Color(0xff, 0x00, 0x80));
    }

    public void insertString(int offset, String str, AttributeSet a)
    throws BadLocationException
    {
        super.insertString(offset, str, a);
    }

    public void remove(int offset, int length) throws BadLocationException
    {
        super.remove(offset, length);
    }
}
