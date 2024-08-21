package groupproject;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class AddDoctor extends JFrame implements ActionListener {
    private JLabel firstnameLabel;
    private JTextField firstnameText;
    private JLabel lastnameLabel;
    private JTextField lastnameText;
    private JLabel genderLabel;
    private JComboBox<String> genderText;
    private JLabel phoneNumLabel;
    private JTextField phoneNumText;
    private JLabel emailLabel;
    private JTextField emailText;
    private JLabel addressLabel;
    private JTextField addressText;
    private JButton submitButton;

    private ArrayList<JTextField> inputs;

    public DatabaseManager databaseManager;

    public AddDoctor(DatabaseManager databaseManager) {

        inputs = new ArrayList<>();

        this.databaseManager = databaseManager;

        setTitle("Add Doctor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(null);

        firstnameLabel = new JLabel("Firstname:");
        firstnameText = new JTextField(20);
        lastnameLabel = new JLabel("Lastname:");
        lastnameText = new JTextField(20);
        genderLabel = new JLabel("Gender:");
        genderText = new JComboBox<String>(new String[] { "M", "F", "O", "P" });
        phoneNumLabel = new JLabel("Phone Number:");
        phoneNumText = new JTextField(20);
        emailLabel = new JLabel("Email:");
        emailText = new JTextField(20);
        addressLabel = new JLabel("Address:");
        addressText = new JTextField(20);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);

        inputs.add(firstnameText);
        inputs.add(lastnameText);
        inputs.add(phoneNumText);
        inputs.add(emailText);
        inputs.add(addressText);

        JPanel panel = new JPanel(new GridLayout(7, 1));
        panel.add(firstnameLabel);
        panel.add(firstnameText);
        panel.add(lastnameLabel);
        panel.add(lastnameText);
        panel.add(genderLabel);
        panel.add(genderText);
        panel.add(phoneNumLabel);
        panel.add(phoneNumText);
        panel.add(emailLabel);
        panel.add(emailText);
        panel.add(addressLabel);
        panel.add(addressText);
        panel.add(submitButton);

        add(panel);
        setVisible(true);
    }

    public void insertData(
            String firstname,
            String lastname,
            String gender,
            String phoneNum,
            String email,
            String address) {
        firstnameText.setText(firstname);
        lastnameText.setText(lastname);
        genderText.setSelectedItem(gender);
        phoneNumText.setText(phoneNum);
        emailText.setText(email);
        addressText.setText(address);
    }

    public boolean checkValidInputs() {
        for (JTextField tjf : inputs) {
            if (tjf.getText().equals("") || tjf.getText() == null) {
                return false;
            }
        }
        return true;
    }

    public void actionPerformed(ActionEvent e) {
        if (!(checkValidInputs())) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out");
            return;
        }
        databaseManager.insert(
                "INSERT INTO doctor (firstname, lastname, gender, phoneNum, email, address) VALUES ('"
                        + firstnameText.getText() +
                        "','"
                        + lastnameText.getText() +
                        "','"
                        + genderText.getSelectedItem().toString() +
                        "','"
                        + phoneNumText.getText() +
                        "','"
                        + emailText.getText() +
                        "','"
                        + addressText.getText() +
                        "')");
        // Login was successful
        try { // Adding it to logs
            File file = new File("src\\main\\resources\\CutePicOfACat.txt"); //
            Scanner scanner = new Scanner(file);

            String username = scanner.nextLine();

            String log = username + " has added a Doctor whose full name is " + firstnameText.getText() + " "
                    + lastnameText.getText() + ".";
            try {
                Files.write(Paths.get("src\\main\\resources\\logs.txt"), log.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException f) {
                // no point in adding exception handling
            }
            scanner.close();
        } catch (Exception l) {
            // TODO: handle exception
        }
        this.dispose();

        new MainMenu();

    }
}