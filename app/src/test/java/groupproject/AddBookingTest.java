package groupproject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

import java.util.Date;
import java.util.TimeZone;

import javax.swing.*;

class AddBookingTest {
    private DatabaseManager databaseManager;

    @Test
    public void checkValidInputs() {
        DatabaseManager databaseManager = new DatabaseManager();
        AddBooking addBooking = new AddBooking(databaseManager);
        addBooking.addDummyText("2023-02-17", "23:43", 0, 0);
        assertTrue(addBooking.checkValidInputs());
    }

    @Test
    public void checkEdgeCase() {
        AddBooking addBooking = new AddBooking(databaseManager);
        addBooking.addDummyText("2099-12-17", "23:59", 0, 0);
        assertTrue(addBooking.checkValidInputs());
    }

    @Test
    public void checkInvalidTime() {

        AddBooking addBooking = new AddBooking(databaseManager);
        addBooking.addDummyText("2023-02-17", "24:43", 0, 0);
        assertTrue(addBooking.checkValidInputs());
    }

    @Test
    public void checkInvalidDate() {
        AddBooking addBooking = new AddBooking(databaseManager);
        addBooking.addDummyText("2010-02-17", "23:43", 0, 0);
        assertTrue(addBooking.checkValidInputs());
    }

    @Test
    public void testSubmitEdgeCase() {
        DatabaseManager databaseManager = new DatabaseManager();
        // Connects to the database, can't do it in BeforeEach method
        databaseManager.connect();
        AddBooking addBooking = new AddBooking(databaseManager);

        try {
            Thread.sleep(500); // sleep for 0.5 seconds (500 milliseconds)
        } catch (InterruptedException e) {
            // handle exception
        }

        // extracts the ContentPane from the JFrame as well as extracts the button from
        // ContentPane
        Container nestedPanel = (Container) addBooking.getContentPane().getComponent(0);
        Component[] components = nestedPanel.getComponents();
        JButton submitButton = (JButton) components[8];
        addBooking.addDummyText("2099-12-17", "23:59", 0, 0);

        // Simulates a click on the submit button
        submitButton.doClick();

        // checks if the input is valid
        boolean result = addBooking.checkValidInputs();
        assertTrue(result);

        // Selects the booking from the database
        DatabaseResult res = addBooking.databaseManager
                .select("SELECT * FROM booking WHERE bid='1'");
        String timetoCompare = res.getValue("time");
        String datetoCompare = res.getValue("date");

        // Checks if the booking is in the database
        assertEquals("23:11", timetoCompare);
        assertEquals("2024-02-24", datetoCompare);
    }

    @Test
    public void testSubmitValid() {
        DatabaseManager databaseManager = new DatabaseManager();
        // Connects to the database, can't do it in BeforeEach method
        databaseManager.connect();
        AddBooking addBooking = new AddBooking(databaseManager);

        try {
            Thread.sleep(500); // sleep for 0.5 seconds (500 milliseconds)
        } catch (InterruptedException e) {
            // handle exception
        }

        // extracts the ContentPane from the JFrame as well as extracts the button from
        // ContentPane
        Container nestedPanel = (Container) addBooking.getContentPane().getComponent(0);
        Component[] components = nestedPanel.getComponents();
        JButton submitButton = (JButton) components[8];
        addBooking.addDummyText("2023-02-17", "23:43", 0, 0);

        // Simulates a click on the submit button
        submitButton.doClick();

        // checks if the input is valid
        boolean result = addBooking.checkValidInputs();
        assertTrue(result);

        // Selects the booking from the database
        DatabaseResult res = addBooking.databaseManager
                .select("SELECT * FROM booking WHERE bid='1'");
        String timetoCompare = res.getValue("time");
        String datetoCompare = res.getValue("date");

        // Checks if the booking is in the database
        assertEquals("23:11", timetoCompare);
        assertEquals("2024-02-24", datetoCompare);
    }


}
