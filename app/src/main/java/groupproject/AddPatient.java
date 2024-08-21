package groupproject;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class AddPatient extends JFrame implements ActionListener {
    private JLabel firstnameLabel;
    private JTextField firstnameText;
    private JLabel middlenameLabel;
    private JTextField middlenameText;
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

    private JLabel disabilitesLabel;
    private JTextField disabilitesText;
    private JLabel jobLabel;
    private JTextField jobText;

    private JLabel ismarriedLabel;
    private JComboBox<String> ismarriedText;
    private JLabel haschildrenLabel;
    private JComboBox<String> haschildrenText;

    private JLabel doctorLabel;
    private JComboBox<String> doctorText;

    private JButton submitButton;
    private LinkedHashMap<String, String> doctors;

    public DatabaseManager databaseManager;

    private String method = "INSERT";
    private int pid;

    public AddPatient(DatabaseManager databaseManager) {
        this(databaseManager, "INSERT");
    }

    public AddPatient(DatabaseManager databaseManager, String method) {
        this.databaseManager = databaseManager;
        this.method = method;

        setTitle("Add Patient");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(null);

        firstnameLabel = new JLabel("Firstname:");
        firstnameText = new JTextField(20);
        middlenameLabel = new JLabel("Middlename:");
        middlenameText = new JTextField(20);
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

        doctorLabel = new JLabel("Doctor:");
        this.doctors = getDoctors();
        doctorText = new JComboBox<String>((String[]) doctors.keySet().toArray());

        disabilitesLabel = new JLabel("Disabilities:");
        disabilitesText = new JTextField(20);
        jobLabel = new JLabel("Job:");
        jobText = new JTextField(20);

        ismarriedLabel = new JLabel("Married:");
        ismarriedText = new JComboBox<String>(new String[] { "Yes", "No" });
        haschildrenLabel = new JLabel("Has Children:");
        haschildrenText = new JComboBox<String>(new String[] { "Yes", "No" });

        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);

        JPanel panel = new JPanel(new GridLayout(13, 1));
        panel.add(firstnameLabel);
        panel.add(firstnameText);
        panel.add(middlenameLabel);
        panel.add(middlenameText);
        panel.add(lastnameLabel);
        panel.add(lastnameText);
        panel.add(genderLabel);
        panel.add(genderText);
        panel.add(ismarriedLabel);
        panel.add(ismarriedText);
        panel.add(haschildrenLabel);
        panel.add(haschildrenText);
        panel.add(phoneNumLabel);
        panel.add(phoneNumText);
        panel.add(emailLabel);
        panel.add(emailText);
        panel.add(addressLabel);
        panel.add(addressText);
        panel.add(disabilitesLabel);
        panel.add(disabilitesText);
        panel.add(jobLabel);
        panel.add(jobText);
        panel.add(doctorLabel);
        panel.add(doctorText);
        panel.add(submitButton);

        add(panel);
        setVisible(true);
    }

    public void insertData(
            String firstname,
            String middlename,
            String lastname,
            String gender,
            String isMarried,
            String hasChildren,
            String phoneNumber,
            String email,
            String address,
            String disabilities,
            String job,
            int did) {
        firstnameText.setText(firstname);
        middlenameText.setText(middlename);
        lastnameText.setText(lastname);
        genderText.setSelectedItem(gender);
        if (isMarried.equals("t")) {
            ismarriedText.setSelectedItem("Yes");
        } else {
            ismarriedText.setSelectedItem("No");
        }
        if (hasChildren.equals("t")) {
            haschildrenText.setSelectedItem("Yes");
        } else {
            haschildrenText.setSelectedItem("No");
        }
        phoneNumText.setText(phoneNumber);
        emailText.setText(email);
        addressText.setText(address);
        disabilitesText.setText(disabilities);
        jobText.setText(job);
        doctorText.setSelectedIndex(did);
    }

    public boolean checkValidInputs() {
        JTextField[] inputs = new JTextField[] {
                firstnameText,
                lastnameText, phoneNumText,
                emailText, addressText,
        };

        for (JTextField input : inputs) {
            if (input.getText().equals("")) {
                return false;
            }
        }

        if (ismarriedText.getSelectedItem() == null || haschildrenText.getSelectedItem() == null
                || doctorText.getSelectedItem() == null) {
            return false;
        }
        return true;
        // todo: check if all inputs are valid
    }

    public LinkedHashMap<String, String> getDoctors() {
        LinkedHashMap<String, String> doctors = new LinkedHashMap<>();
        ArrayList<LinkedHashMap<String, String>> res = databaseManager
                .select("select dID, firstname, lastname from doctor;")
                .getResources();
        for (LinkedHashMap<String, String> doctor : res) {
            doctors.put(doctor.get("firstname") + " " + doctor.get("lastname"), doctor.get("did"));
            System.out.println(doctor.get("did"));
        }
        return doctors;
    }

    public void setPatientID(int pid) {
        this.pid = pid;
    }

    public void actionPerformed(ActionEvent e) {
        this.getDoctors();
        if (!(checkValidInputs())) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out");
            return;
        }
        String married = ismarriedText.getSelectedItem().equals("Yes") ? "true" : "false";
        String hasChildren = haschildrenText.getSelectedItem().equals("Yes") ? "true" : "false";

        String doctorID = doctors.get(doctorText.getSelectedItem());

        if (method.equals("INSERT")) {
            databaseManager.insert(
                    "INSET INTO patient (dID, firstname ,middlename ,lastname, gender ,isMarried ,hasChildren ,phoneNum ,email ,address,disabilities,currentJob) VALUES ('"
                            + doctorID +
                            "','"
                            + firstnameText.getText() +
                            "','"
                            + middlenameText.getText() +
                            "','"
                            + lastnameText.getText() +
                            "','"
                            + genderText.getSelectedItem().toString() +
                            "','"
                            + married +
                            "','"
                            + hasChildren +
                            "','"
                            + phoneNumText.getText() +
                            "','"
                            + emailText.getText() +
                            "','"
                            + addressText.getText() +
                            "','"
                            + disabilitesText.getText() +
                            "','"
                            + jobText.getText() +
                            "')");

            try { // log the results
                File file = new File("src\\main\\resources\\CutePicOfACat.txt"); //
                Scanner scanner = new Scanner(file);

                String username = scanner.nextLine();

                String log = username + " has updated a patient whose full name is " + firstnameText.getText() + " "
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
        } else {
            databaseManager.update(
                    "UPDATE patient SET "
                            + "dID = " + doctorID +
                            ","
                            + "firstname = '" + firstnameText.getText() +
                            "',"
                            + "middlename = '" + middlenameText.getText() +
                            "',"
                            + "lastname = '" + lastnameText.getText() +
                            "',"
                            + "gender = '" + genderText.getSelectedItem().toString() +
                            "',"
                            + "isMarried = " + married +
                            ","
                            + "hasChildren = " + hasChildren +
                            ","
                            + "phoneNum = '" + phoneNumText.getText() +
                            "',"
                            + "email = '" + emailText.getText() +
                            "',"
                            + "address = '" + addressText.getText() +
                            "',"
                            + "disabilities = '" + disabilitesText.getText() +
                            "',"
                            + "currentJob = '" + jobText.getText() +
                            "' WHERE pID = " + pid);

            try { // log the results
                File file = new File("src\\main\\resources\\CutePicOfACat.txt"); //
                Scanner scanner = new Scanner(file);

                String username = scanner.nextLine();

                String log = username + " has added a patient whose full name is " + firstnameText.getText() + " "
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

        }
        pid = -1;

        // Login was successful
        this.dispose();

        new MainMenu();

    }
}