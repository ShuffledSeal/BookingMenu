package groupproject;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.sql.Time;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

public class AddBooking extends JFrame implements ActionListener {

    private JLabel doctorLabel;
    private JComboBox<String> doctorText;

    private JLabel patientLabel;
    private JComboBox<String> patientText;

    private JLabel dateLabel;
    private JTextField dateText;

    private JLabel timeLabel;
    private JTextField timeText;

    private JButton submitButton;
    private HashMap<String, String> doctors;
    private HashMap<String, String> patients;

    public DatabaseManager databaseManager;

    public AddBooking(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

        setTitle("Add Booking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(200, 300);
        setLocationRelativeTo(null);

        // Creating a format for date and time fields
        dateLabel = new JLabel("Date:");
        MaskFormatter dateMask = null;
        try {
            dateMask = new MaskFormatter("####-##-##");
            dateMask.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateText = new JFormattedTextField(dateMask);
        dateText.setColumns(20);

        timeLabel = new JLabel("Time:");
        MaskFormatter timeMask = null;
        try {
            timeMask = new MaskFormatter("##:##");
            timeMask.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeText = new JFormattedTextField(timeMask);
        timeText.setColumns(20);

        // doctors and patients are stored in combo boxes to make it easier to book
        // appointments
        doctorLabel = new JLabel("Doctor:");
        this.doctors = getDoctors();
        doctorText = new JComboBox<String>((String[]) doctors.keySet().toArray());

        patientLabel = new JLabel("Patient:");
        this.patients = getPatients();
        patientText = new JComboBox<String>((String[]) patients.keySet().toArray());

        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);

        // Creates the Java GUI
        JPanel panel = new JPanel(new GridLayout(5, 1));
        JComponent[] components = new JComponent[] {
                dateLabel, dateText,
                timeLabel, timeText,
                doctorLabel, doctorText,
                patientLabel, patientText,
                submitButton
        };

        for (JComponent component : components) {
            panel.add(component);
        }

        add(panel);
        setVisible(true);
    }

    // faster testing
    public void addDummyText(
            String dateTest,
            String timeTest,
            int doctorIndex,
            int patientIndex) {
        dateText.setText(dateTest);
        timeText.setText(timeTest);
        doctorText.setSelectedIndex(doctorIndex);
        patientText.setSelectedIndex(patientIndex);
    }

    public boolean checkValidInputs() {
        JTextField[] inputs = new JTextField[] {
                dateText,
                timeText
        };

        for (JTextField input : inputs) {
            if (input.getText().equals("")) {
                return false;
            }
        }

        // Check if date and time are valid and not before current date and time
        try {
            String dateString = dateText.getText();
            String timeString = timeText.getText();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateFormat.setLenient(false);
            Date inputDateTime = dateFormat.parse(dateString + " " + timeString);

            Date now = new Date();
            if (inputDateTime.before(now)) {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    // Does an sql statement to view the doctors and retrieve them
    public LinkedHashMap<String, String> getDoctors() {
        LinkedHashMap<String, String> doctors = new LinkedHashMap<>();
        ArrayList<LinkedHashMap<String, String>> res = databaseManager
                .select("select dID, firstname, lastname from doctor;")
                .getResources();
        for (LinkedHashMap<String, String> doctor : res) {
            doctors.put(doctor.get("firstname") + " " + doctor.get("lastname"), doctor.get("dID"));
        }
        return doctors;
    }

    // Does an sql statement to view the patients and retrieve them
    public LinkedHashMap<String, String> getPatients() {
        LinkedHashMap<String, String> patients = new LinkedHashMap<>();
        ArrayList<LinkedHashMap<String, String>> res = databaseManager
                .select("select pID, firstname, lastname from patient;")
                .getResources();
        for (LinkedHashMap<String, String> patient : res) {
            patients.put(patient.get("firstname") + " " + patient.get("lastname"), patient.get("pID"));
        }
        return patients;
    }

    public void actionPerformed(ActionEvent e) {
        this.getDoctors();
        if (!(checkValidInputs())) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out");
            return;
        }

        String timeString = timeText.getText();
        DateFormat format = new SimpleDateFormat("HH:mm");
        Date time;
        try {
            time = format.parse(timeString);
        } catch (ParseException ex) {
            ex.printStackTrace();
            return;
        }

        Time sqlTime = new Time(time.getTime());

        String dateString = dateText.getText();
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format2.parse(dateString);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        // submits the data
        String doctorID = doctors.get(doctorText.getSelectedItem());
        String patientID = patients.get(patientText.getSelectedItem());
        databaseManager.insert(
                "INSERT INTO booking (dID, pID,  date, time) VALUES ('"
                        + doctorID +
                        "','"
                        + patientID +
                        "','"
                        + date +
                        "','"
                        + sqlTime +
                        "')");
        // the function was successfully executed
        try {
            File file = new File("src\\main\\resources\\CutePicOfACat.txt.txt"); //
            Scanner scanner = new Scanner(file);

            String username = scanner.nextLine();

            String log = username + " has added a booking with values " + doctorID + " as doctor ID " + patientID
                    + " as patient ID for date " + dateText.getText();
            try {
                Files.write(Paths.get("src\\main\\resources\\logs.txt"), log.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException f) {
                // no point in adding exception handling
            }
            scanner.close();
        } catch (Exception l) {
            // TODO: handle exception
        }
        new MainMenu();
        this.dispose();

    }
}