package groupproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;

import javax.swing.*;

public class AddPatientTest {
    private DatabaseManager databaseManager;

    @Test
    public void checkValidInputs() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();
        databaseManager.insert(
                "INSERT INTO doctor (firstname, lastname, gender, phoneNum, email, address) VALUES ('"
                        + "Adam" +
                        "','"
                        + "Driver" +
                        "','"
                        + 'M' +
                        "','"
                        + "076942069420" +
                        "','"
                        + "sithsRUs@gmail.com" +
                        "','"
                        + "64 Zoo Lane" +
                        "')");
        AddPatient addPatient = new AddPatient(databaseManager);
        addPatient.insertData(
                "Pog",
                "Pogella",
                "Pogellas",
                "F",
                "t",
                "f",
                "084314395131",
                "pog@hotmail.com",
                "21 Cant Walk",
                "none",
                "binwoman",
                1);

        assertTrue(addPatient.checkValidInputs());
    }

    @Test
    public void checkInvalidInputs() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();
        databaseManager.insert(
                "INSERT INTO doctor (firstname, lastname, gender, phoneNum, email, address) VALUES ('"
                        + "Adam" +
                        "','"
                        + "Driver" +
                        "','"
                        + 'M' +
                        "','"
                        + "076942069420" +
                        "','"
                        + "sithsRUs@gmail.com" +
                        "','"
                        + "64 Zoo Lane" +
                        "')");
        AddPatient addPatient = new AddPatient(databaseManager);
        addPatient.insertData(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                0);

        assertFalse(addPatient.checkValidInputs());
    }

    @Test
    public void checkEdgeCase() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();
        databaseManager.insert(
                "INSERT INTO doctor (firstname, lastname, gender, phoneNum, email, address) VALUES ('"
                        + "Adam" +
                        "','"
                        + "Driver" +
                        "','"
                        + 'M' +
                        "','"
                        + "076942069420" +
                        "','"
                        + "sithsRUs@gmail.com" +
                        "','"
                        + "64 Zoo Lane" +
                        "')");
        AddPatient addPatient = new AddPatient(databaseManager);
        addPatient.insertData(
                "ZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamn",
                "ZanielZanielZanielZanielZanielZanielZanielZanielZanielZanielZanielZanielZanielZanielZanielZanielZanielZaniel",
                "ZanZanZanZanZanZanZanZanZanZanZanZanZanZanZan",
                "M",
                "f",
                "f",
                "08559251582",
                "zamnzaniel@animaljam.com",
                "216 Baker Street",
                "none",
                "gamergamergamergamergamergamergamer",
                1);

        assertTrue(addPatient.checkValidInputs());
    }

    @Test
    public void testSubmitValid() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();
        databaseManager.insert(
                "INSERT INTO doctor (firstname, lastname, gender, phoneNum, email, address) VALUES ('"
                        + "Adam" +
                        "','"
                        + "Driver" +
                        "','"
                        + 'M' +
                        "','"
                        + "076942069420" +
                        "','"
                        + "sithsRUs@gmail.com" +
                        "','"
                        + "64 Zoo Lane" +
                        "')");

        AddPatient addPatient = new AddPatient(databaseManager);

        addPatient.insertData(
                "Pog",
                "Pogella",
                "Pogellas",
                "F",
                "t",
                "f",
                "084314395131",
                "pog@hotmail.com",
                "21 Cant Walk",
                "none",
                "binwoman",
                1);

        try {
            Thread.sleep(500); // sleep for 0.5 seconds (500 milliseconds)
        } catch (InterruptedException e) {
            // handle exception
        }

        // extracts the ContentPane from the JFrame as well as extracts the button from
        // ContentPane
        Container nestedPanel = (Container) addPatient.getContentPane().getComponent(0);
        Component[] components = nestedPanel.getComponents();
        JButton submitButton = (JButton) components[22];

        // Simulates a click on the submit button
        submitButton.doClick();

        // checks if the input is valid
        boolean result = addPatient.checkValidInputs();
        assertTrue(result);

        // Selects the patient from the database
        DatabaseResult res = addPatient.databaseManager
                .select("SELECT * FROM patient WHERE pid='1'");
        String patientFirstName = res.getValue("firstname");
        String patientphoneNum = res.getValue("phonenum");
        String patientAddress = res.getValue("address");
        // Checks if the patient is in the database
        assertEquals("Pog", patientFirstName);
        assertEquals("084314395131", patientphoneNum);
        assertEquals("21 Cant Walk", patientAddress);

    }

    @Test
    public void testSubmitInvalid() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();
        databaseManager.insert(
                "INSERT INTO doctor (firstname, lastname, gender, phoneNum, email, address) VALUES ('"
                        + "Adam" +
                        "','"
                        + "Driver" +
                        "','"
                        + 'M' +
                        "','"
                        + "076942069420" +
                        "','"
                        + "sithsRUs@gmail.com" +
                        "','"
                        + "64 Zoo Lane" +
                        "')");
        AddPatient addPatient = new AddPatient(databaseManager);

        addPatient.insertData(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                0);

        try {
            Thread.sleep(500); // sleep for 0.5 seconds (500 milliseconds)
        } catch (InterruptedException e) {
            // handle exception
        }

        // extracts the ContentPane from the JFrame as well as extracts the button from
        // ContentPane
        Container nestedPanel = (Container) addPatient.getContentPane().getComponent(0);
        Component[] components = nestedPanel.getComponents();
        JButton submitButton = (JButton) components[22];

        // Simulates a click on the submit button
        submitButton.doClick();

        // checks if the input is valid
        boolean result = addPatient.checkValidInputs();
        assertFalse(result);

        // Selects the patient from the database

        try {
            DatabaseResult res = addPatient.databaseManager
                    .select("SELECT * FROM patient WHERE pid='2'");
            String patientFirstName = res.getValue("firstname");
            // Checks if the patient is in the database
            assertEquals("NotPog", patientFirstName);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);

        }
    }

    @Test
    public void testSubmitEdgeCase() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();
        databaseManager.insert(
                "INSERT INTO doctor (firstname, lastname, gender, phoneNum, email, address) VALUES ('"
                        + "Adam" +
                        "','"
                        + "Driver" +
                        "','"
                        + 'M' +
                        "','"
                        + "076942069420" +
                        "','"
                        + "sithsRUs@gmail.com" +
                        "','"
                        + "64 Zoo Lane" +
                        "')");
        AddPatient addPatient = new AddPatient(databaseManager);

        addPatient.insertData(
                "ZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamnZamn",
                "ZanielZanielZanielZanielZanielZanielZanielZanielZanielZanielZanielZanielZanielZanielZanielZanielZanielZaniel",
                "ZanZanZanZanZanZanZanZanZanZanZanZanZanZanZan",
                "M",
                "f",
                "f",
                "08559251582",
                "zamnzaniel@animaljam.com",
                "216 Baker Street",
                "none",
                "gamergamergamergamergamergamergamer",
                1);

        try {
            Thread.sleep(500); // sleep for 0.5 seconds (500 milliseconds)
        } catch (InterruptedException e) {
            // handle exception
        }

        // extracts the ContentPane from the JFrame as well as extracts the button from
        // ContentPane
        Container nestedPanel = (Container) addPatient.getContentPane().getComponent(0);
        Component[] components = nestedPanel.getComponents();
        JButton submitButton = (JButton) components[22];

        // Simulates a click on the submit button
        submitButton.doClick();

        // checks if the input is valid
        boolean result = addPatient.checkValidInputs();
        assertTrue(result);

        // Selects the patient from the database
        DatabaseResult res = addPatient.databaseManager
                .select("SELECT * FROM patient WHERE pid='1'");
        String patientFirstName = res.getValue("firstname");
        String patientphoneNum = res.getValue("phonenum");
        String patientAddress = res.getValue("address");
        // Checks if the patient is in the database
        assertEquals("Pog", patientFirstName);
        assertEquals("084314395131", patientphoneNum);
        assertEquals("21 Cant Walk", patientAddress);
    }

}
