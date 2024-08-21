package groupproject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;

import javax.swing.*;

class AddDoctorTest {

    private DatabaseManager databaseManager;

    @Test
    void checkValidInputsTest() {
        // Create a new instance of the AddDoctor class
        AddDoctor addDoctor = new AddDoctor(databaseManager);

        // Set values for the input fields
        addDoctor.insertData(
                "Pog",
                "Poggers",
                "PepeHands",
                "07694206942",
                "peepooHey@twitch.com",
                "64 Zoo Lane");

        // Call the method being tested
        boolean result = addDoctor.checkValidInputs();

        // Check the output using assertions
        assertTrue(result);
    }

    @Test
    void checkEdgeCase() {
        // Create a new instance of the AddDoctor class
        AddDoctor addDoctor = new AddDoctor(databaseManager);

        // Set values for the input fields
        addDoctor.insertData(
                "Pog",
                "Poggers",
                "PepeHands",
                "555999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999555555599999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999955555559999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999995555",
                "peepooHey@twitch.com",
                "64 Zoo Lane");

        // Call the method being tested
        boolean result = addDoctor.checkValidInputs();

        // Check the output using assertions
        assertTrue(result);
    }

    @Test
    void checkInvalidInputsTest() {
        AddDoctor addDoctor = new AddDoctor(databaseManager);

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();

        try {
            Thread.sleep(500); // sleep for 0.5 seconds (500 milliseconds)
        } catch (InterruptedException e) {
            // handle exception but not needed in this case
        }

        // Set empty values for the input fields
        addDoctor.insertData(
                "",
                "",
                "",
                "",
                "",
                "");

        boolean result = addDoctor.checkValidInputs();
        assertFalse(result);

    }

    @Test
    void testValidSubmit() {
        // Connects to the database , can't do it in BeforeEach method
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();
        AddDoctor addDoctor = new AddDoctor(databaseManager);

        try {
            Thread.sleep(500); // sleep for 0.5 seconds (500 milliseconds)
        } catch (InterruptedException e) {
            // handle exception
        }

        // extracts the ContentPane from the JFrame as well as extracts the button from
        // ContentPane
        Container nestedPanel = (Container) addDoctor.getContentPane().getComponent(0);
        Component[] components = nestedPanel.getComponents();
        JButton submitButton = (JButton) components[12];
        addDoctor.insertData(
                "Pog",
                "Poggers",
                "PepeHands",
                "07694206942",
                "peepooHey@twitch.com",
                "64 Zoo Lane");

        // Simulates a click on the submit button
        submitButton.doClick();

        // checks if the input is valid
        boolean result = addDoctor.checkValidInputs();
        assertTrue(result);

        // Selects the doctor from the database
        DatabaseResult res = addDoctor.databaseManager
                .select("SELECT * FROM doctor WHERE did='1'");
        String doctorFirstName = res.getValue("firstname");
        String doctorphoneNum = res.getValue("phonenum");
        String doctorAddress = res.getValue("address");
        // Checks if the doctor is in the database
        assertEquals("Pog", doctorFirstName);
        assertEquals("07694206942", doctorphoneNum);
        assertEquals("64 Zoo Lane", doctorAddress);
    }

    @Test
    void testSubmitInvalid() {
        // Connects to the database, can't do it in BeforeEach method
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();
        AddDoctor addDoctor = new AddDoctor(databaseManager);
        // Sleeps until database is connected
        try {
            Thread.sleep(500); // sleep for 0.5 seconds (500 milliseconds)
        } catch (InterruptedException e) {
            // handle exception
        }
        // Extracts the ContentPane from the JFrame as well as extracts the button from
        // ContentPane
        Container nestedPanel = (Container) addDoctor.getContentPane().getComponent(0);
        Component[] components = nestedPanel.getComponents();
        JButton submitButton = (JButton) components[12];

        // Set empty values for the input fields
        addDoctor.insertData(
                "",
                "",
                "",
                "",
                "",
                "");

        submitButton.doClick();
        // Tries to select the doctor from the database but expected to fail since the
        // input is invalid
        try {
            DatabaseResult res = addDoctor.databaseManager
                    .select("SELECT * FROM doctor WHERE did='1'");
            String doctorFirstName = res.getValue("firstname", 1);
            assertEquals("Pablo", doctorFirstName);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);

        }

    }

    @Test
    void testSubmitEdgeCase() {
        // Connects to the database, can't do it in BeforeEach method
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();
        AddDoctor addDoctor = new AddDoctor(databaseManager);

        try {
            Thread.sleep(500); // sleep for 0.5 seconds (500 milliseconds)
        } catch (InterruptedException e) {
            // handle exception
        }

        Container nestedPanel = (Container) addDoctor.getContentPane().getComponent(0);
        Component[] components = nestedPanel.getComponents();
        JButton submitButton = (JButton) components[12];
        addDoctor.insertData(
                "Pog",
                "Poggers",
                "PepeHands",
                "555999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999555555599999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999955555559999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999995555",
                "peepooHey@twitch.com",
                "64 Zoo Lane");

        submitButton.doClick();

        try {
            DatabaseResult res = addDoctor.databaseManager
                    .select("SELECT * FROM doctor WHERE did='1'");
            String doctorFirstName = res.getValue("firstname", 1);
            assertEquals("Pablo", doctorFirstName);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);

        }

    }

}
