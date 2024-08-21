package groupproject;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

class JTextLimit extends PlainDocument {
    private int limit;
    private boolean intOnly;

    JTextLimit(int limit) {
        super();
        this.limit = limit;
        this.intOnly = false;
    }

    JTextLimit(boolean intOnly) {
        super();
        this.limit = -1;
        this.intOnly = intOnly;
    }

    JTextLimit(int limit, boolean intOnly) {
        super();
        this.limit = limit;
        this.intOnly = intOnly;
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }
        if (intOnly) {
            for (int i = 0; i < str.length(); i++) {
                if (!(Character.isDigit(str.charAt(i)))) {
                    return;
                }
            }
        }
        System.out.println(str);
        if (limit == -1) {
            super.insertString(offset, str, attr);
            return;
        }
        if ((getLength() + str.length()) <= limit) {
            // TODO: Pasted text to be truncated if its too long
            super.insertString(offset, str, attr);
        }
    }
}

public class InputField extends JTextField {
    public InputField(int columns) {
        super();
        setColumns(columns);
    }

    public InputField(int columns, int limit) {
        super();
        setColumns(columns);
        setDocument(new JTextLimit(limit));
    }

    public InputField(int columns, boolean intOnly) {
        super();
        setColumns(columns);
        setDocument(new JTextLimit(intOnly));
    }

    public InputField(int columns, int limit, boolean intOnly) {
        super();
        setColumns(columns);
        setDocument(new JTextLimit(limit, intOnly));
    }
}
