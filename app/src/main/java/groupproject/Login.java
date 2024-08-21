package groupproject;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;

public class Login extends JFrame implements ActionListener {

    private JLabel userLabel;
    private JTextField userText;
    private JLabel passwordLabel;
    private JPasswordField passwordText;
    private JButton loginButton;
    private DatabaseManager databaseManager;
    private JButton logoutButton;

    public Login(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        userLabel = new JLabel("Username:");
        userText = new JTextField(20);
        passwordLabel = new JLabel("Password:");
        passwordText = new JPasswordField(20);
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);

        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(loginButton);
        panel.add(logoutButton);

        add(panel);
        setVisible(true);
    }

    //
    public boolean checkLogin(String username, String password) {
        String statement = "select pwd from public.receptionist where login=\'username\';".replace(
                "username", username);
        DatabaseResult res = this.databaseManager.select(statement);
        System.out.println(res);
        if (!(res.getValue("pwd").equals(password))) {
            return false;
        }
        return true;
    }

    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();
        if (source == loginButton) {

            String username = userText.getText();
            String password = new String(passwordText.getPassword());

            if (!(checkLogin(username, password))) {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!");
            } else {

                // Create a file with incospicious name so if system comprimesed still likely to
                // not be hacked
                File file = new File("src\\main\\resources\\CutePicOfACat.txt");
                // Check if file exists
                if (!file.exists()) {
                    try {
                        // Create new file
                        file.createNewFile();
                    } catch (IOException d) {
                        JOptionPane.showMessageDialog(this,
                                "This broke no clue why lmao, check your read/write permissions in this location");
                        d.printStackTrace();
                    }
                } else {
                    try {
                        file.delete();
                        file.createNewFile();
                    } catch (IOException d) {
                        JOptionPane.showMessageDialog(this,
                                "This broke no clue why lmao, check your read/write permissions in this location");
                        d.printStackTrace();
                    }
                }

                // Writes to the file that is used as a cookie for authentication
                try {
                    FileWriter writer = new FileWriter(file);
                    writer.write(username + "\n");
                    writer.write(password + "\n");
                    writer.close();
                } catch (IOException f) {
                    JOptionPane.showMessageDialog(this, "Can't edit file lmao.");
                    f.printStackTrace();
                }

                String log = username + " has logged in";
                try {
                    Files.write(Paths.get("src\\main\\resources\\logs.txt"), log.getBytes(), StandardOpenOption.APPEND);
                } catch (IOException h) {
                    JOptionPane.showMessageDialog(this, "Can't edit file lmao.");
                    h.printStackTrace();
                }

                new MainMenu();
                // Login was successful
                this.dispose();

            }

        } else if (source == logoutButton) {
            File file = new File("src\\main\\resources\\CutePicOfACat.txt");
            file.delete();
        }

    }

}