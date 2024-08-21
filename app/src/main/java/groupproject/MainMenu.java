package groupproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MainMenu extends JFrame implements ActionListener {
    JButton loginButton, addDoctorButton, addPatientButton, addBookingButton, viewPatientsButton, button6;
    DatabaseManager databaseManager = new DatabaseManager();

    public MainMenu() {
        setTitle("Admin System");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create buttons
        loginButton = new JButton("Login");
        addDoctorButton = new JButton("Add Doctor");
        addPatientButton = new JButton("Add Patient");
        addBookingButton = new JButton("Add Booking");
        viewPatientsButton = new JButton("View Patient");

        // Add action listeners to buttons
        loginButton.addActionListener(this);
        addDoctorButton.addActionListener(this);
        addPatientButton.addActionListener(this);
        addBookingButton.addActionListener(this);
        viewPatientsButton.addActionListener(this);

        // Add buttons to content pane
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(2, 3));
        contentPane.add(loginButton);
        contentPane.add(addDoctorButton);
        contentPane.add(addPatientButton);
        contentPane.add(addBookingButton);
        contentPane.add(viewPatientsButton);
        databaseManager.connect();
    }

    public boolean checkLogin() {
        try {
            File file = new File("filename.txt"); // Replace "filename.txt" with your file name
            Scanner scanner = new Scanner(file);

            String username = scanner.nextLine();
            String password = scanner.nextLine();

            String statement = "select pwd from public.receptionist where login=\'username\';".replace(
                    "username", username);

            DatabaseResult res = this.databaseManager.select(statement);

            // Do something with the two lines

            scanner.close();

            if (res.getValue("pwd").equals(password)) {
                return true;
            }

        } catch (IOException d) {
            return false;
        }

        return false;
    }

    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();
        if (source == loginButton) {
            new Login(databaseManager);
            this.dispose();
        } else if (source == addDoctorButton) {
            if (checkLogin()) {
                new AddDoctor(databaseManager);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Can't edit file lmao.");
            }

        } else if (source == addPatientButton) {
            if (checkLogin()) {
                new AddPatient(databaseManager);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Can't edit file lmao.");
            }
        } else if (source == addBookingButton) {
            if (checkLogin()) {
                new AddBooking(databaseManager);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Can't edit file lmao.");
            }
        } else if (source == viewPatientsButton) {
            if (checkLogin()) {
                new ViewPatients(databaseManager);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Can't edit file lmao.");
            }
        }
    }

    public static void main(String[] args) {
        MainMenu demo = new MainMenu();
        demo.setVisible(true);
    }
}
