package groupproject;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

public class ViewPatients extends JFrame implements ActionListener {
    private JLabel searchLabel;
    private JTextField search;
    private JButton updateButton;
    private JButton refreshButton;

    private JTable table;
    private DefaultTableModel model;
    private int selectedTableRow;

    public DatabaseManager databaseManager;

    private DatabaseResult result;

    private String[][] databaseValues;
    private String[] databaseColumns;

    public ViewPatients(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

        setTitle("View Patients");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 750);
        setLocationRelativeTo(null);

        searchLabel = new JLabel("Search:");
        search = new JTextField(20);
        search.getDocument().addDocumentListener(new MyDocumentListener());

        updateButton = new JButton("Update Selected Patient");
        updateButton.addActionListener(e -> updateElement());
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTable());

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;

        add(searchLabel, c);
        c.gridx = 1;
        add(search, c);
        c.gridx = 2;
        add(updateButton, c);
        c.gridx = 3;
        add(refreshButton, c);

        result = databaseManager.select("SELECT * FROM Patient");
        databaseValues = new String[result.numberOfResources()][result.numberOfLabels()];
        databaseColumns = result.getLabels();

        for (int i = 0; i < result.numberOfResources(); i++) {
            for (int j = 0; j < databaseColumns.length; j++) {
                databaseValues[i][j] = result.getValue(result.getLabel(j, i), i);
            }
        }

        table = new JTable();
        table.setBounds(30, 40, 850, 300);
        model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int mColIndex) {
                // Cells aren't editable
                return false;
            }
        };
        model.setColumnIdentifiers(databaseColumns);
        table.setModel(model);
        selectedTableRow = -1;
        refreshTable();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // set SelectedTableRow to the row index that the user clicked
                selectedTableRow = table.getSelectedRow();
            }
        });

        JScrollPane sp = new JScrollPane(table);
        c.gridwidth = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        add(sp, c);

        pack();

        setVisible(true);
    }

    /*
     * Refreshes the table with updated data from the Patient table
     */
    public void refreshTable() {
        result = databaseManager.select("SELECT * FROM Patient");
        databaseValues = new String[result.numberOfResources()][result.numberOfLabels()];
        databaseColumns = result.getLabels();
        for (int i = 0; i < result.numberOfResources(); i++) {
            for (int j = 0; j < databaseColumns.length; j++) {
                databaseValues[i][j] = result.getValue(result.getLabel(j, i), i);
            }
        }
        model.setRowCount(0);
        for (int i = 0; i < databaseValues.length; i++) {
            model.addRow(databaseValues[i]);
        }
    }

    /*
     * Creates a new AddPatient and insert already-existing data to simulate an
     * update
     */
    private void updateElement() {
        if (selectedTableRow < 0 || selectedTableRow > model.getRowCount()) {
            JOptionPane.showMessageDialog(this, "Please select a patient to edit");
            return;
        }
        AddPatient updater = new AddPatient(databaseManager, "UPDATE");

        String[] modelValues = new String[13];

        // Get the values from the rows and store to a string array
        for (int i = 0; i < 13; i++) {
            modelValues[i] = model.getValueAt(selectedTableRow, i).toString();
        }

        updater.setPatientID(Integer.parseInt(modelValues[0].toString()));
        updater.insertData(
                modelValues[2].toString(),
                modelValues[3].toString(),
                modelValues[4].toString(),
                modelValues[5].toString(),
                modelValues[6].toString(),
                modelValues[7].toString(),
                modelValues[8].toString(),
                modelValues[9].toString(),
                modelValues[10].toString(),
                modelValues[11].toString(),
                modelValues[12].toString(),
                Integer.parseInt(modelValues[1].toString()));

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    class MyDocumentListener implements DocumentListener {
        final String newline = "\n";

        public void insertUpdate(DocumentEvent e) {
            updateLog(e);
        }

        public void removeUpdate(DocumentEvent e) {
            updateLog(e);
        }

        public void changedUpdate(DocumentEvent e) {
        }

        /*
         * On every key event for the search, update the table with results that contain
         * the search value in its firstname+lastname
         */
        public void updateLog(DocumentEvent e) {
            String searchString = search.getText().replace(" ", "").trim().toLowerCase();
            String fullName = "";
            model.setRowCount(0);
            for (int i = 0; i < databaseValues.length; i++) {
                fullName = databaseValues[i][1] + databaseValues[i][2].trim().toLowerCase();
                if (fullName.contains(searchString)) {
                    model.addRow(databaseValues[i]);
                }
            }
            table.repaint();
        }

    }
}