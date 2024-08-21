package groupproject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import javax.swing.*;

class LoginTest {
    private Login login;

    @BeforeEach
    void setUp() {
        DatabaseManager db = new DatabaseManager();
        login = new Login(db);
    }

    @Test
    void testLoginValid() {
        // Valid Login
        assertTrue(login.checkLogin("admin", "admin123"));
    }

    @Test
    void testLoginInvalid() {
        // Invalid Login
        assertFalse(login.checkLogin("user", "password"));
    }

    @Test
    void testLoginNull() {
        // Sets Login to null and checks valid behaviour
        assertFalse(login.checkLogin("", ""));
    }

    @Test
    void testLoginInvalidEdge() {
        // Edge Case
        assertFalse(
                login.checkLogin("99999999999999999999999999999999999999999999999999999999999999999999999999999999999",
                        "999999999999999999999999999999999999999999999999999999999999999999999999999"));
    }

    @Test
    void testLoginForm() {
        // Tests UI elements

        // Gets the panel with all the info
        Container nestedPanel = (Container) login.getContentPane().getComponent(0);
        // Checks if there are 5 components
        Component[] components = nestedPanel.getComponents();
        assertEquals(5, components.length);
        // Checks what the components are
        assertTrue(components[0] instanceof JLabel);
        assertTrue(components[1] instanceof JTextField);
        assertTrue(components[2] instanceof JLabel);
        assertTrue(components[3] instanceof JPasswordField);
        assertTrue(components[4] instanceof JButton);
        // Simulates a login in the form
        JTextField usernameField = (JTextField) components[1];
        usernameField.setText("admin");

        JPasswordField passwordField = (JPasswordField) components[3];
        passwordField.setText("admin123");

        JButton loginButton = (JButton) components[4];
        loginButton.doClick();
        // Checks if the login was successful
        assertFalse(login.isVisible());

    }
}
