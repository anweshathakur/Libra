import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class DnaRecordsApp extends JFrame implements ActionListener, ListSelectionListener {

    private JTextField idField;
    private JTextField organismField;
    private JTextField sequenceField;
    private JTextField chromosomeField;

    private JButton addBtn;
    private JButton searchBtn;
    private JButton updateBtn;
    private JButton deleteBtn;
    private JButton clearBtn;

    private JTable table;
    private DefaultTableModel model;

    private static final String[] COLUMNS = {
        "Sequence ID", "Organism Name", "Raw Sequence", "Chromosome", "Length", "GC Content (%)"
    };

    public DnaRecordsApp() {
        setTitle("DNA Sequence Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- Sequence Details form (GridLayout) ----
        idField = new JTextField();
        organismField = new JTextField();
        sequenceField = new JTextField();
        chromosomeField = new JTextField();

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("Sequence Details"));
        formPanel.add(new JLabel("Sequence ID"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Organism Name"));
        formPanel.add(organismField);
        formPanel.add(new JLabel("Raw Sequence"));
        formPanel.add(sequenceField);
        formPanel.add(new JLabel("Chromosome Number"));
        formPanel.add(chromosomeField);

        // ---- Buttons (FlowLayout) ----
        addBtn = new JButton("Add");
        searchBtn = new JButton("Search");
        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");
        clearBtn = new JButton("Clear");

        addBtn.addActionListener(this);
        searchBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        clearBtn.addActionListener(this);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        buttonPanel.add(addBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // ---- DNA Records table ----
        model = new DefaultTableModel(COLUMNS, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(this);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("DNA Records"));
        add(scrollPane, BorderLayout.CENTER);

        // ---- Sample data ----
        model.addRow(buildRow("SEQ-1092", "Homo sapiens", "ATGCGATCG", 17));
        model.addRow(buildRow("SEQ-2041", "Escherichia coli", "GCATTCGA", 1));
        model.addRow(buildRow("SEQ-3088", "Drosophila melanogaster", "AATTTGCGC", 3));
    }

    // ---------- Button handling ----------

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == addBtn) {
            addRecord();
        } else if (src == searchBtn) {
            searchRecord();
        } else if (src == updateBtn) {
            updateRecord();
        } else if (src == deleteBtn) {
            deleteRecord();
        } else if (src == clearBtn) {
            clearFields();
        }
    }

    // Clicking a table row fills the form fields
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        int row = table.getSelectedRow();
        if (row == -1) {
            return;
        }
        idField.setText(model.getValueAt(row, 0).toString());
        organismField.setText(model.getValueAt(row, 1).toString());
        sequenceField.setText(model.getValueAt(row, 2).toString());
        chromosomeField.setText(model.getValueAt(row, 3).toString());
    }

    private void addRecord() {
        Object[] record = readRecord();
        if (record == null) {
            return;
        }
        if (findRow(record[0].toString()) != -1) {
            showMessage("A record with that Sequence ID already exists.");
            return;
        }
        model.addRow(record);
        clearFields();
    }

    private void searchRecord() {
        String id = idField.getText().trim();
        String organism = organismField.getText().trim();
        int row = -1;

        if (!id.isEmpty()) {
            row = findRow(id);
        } else if (!organism.isEmpty()) {
            for (int i = 0; i < model.getRowCount(); i++) {
                String name = model.getValueAt(i, 1).toString().toLowerCase();
                if (name.contains(organism.toLowerCase())) {
                    row = i;
                    break;
                }
            }
        } else {
            showMessage("Enter a Sequence ID or Organism Name to search.");
            return;
        }

        if (row == -1) {
            showMessage("No matching record found.");
        } else {
            table.setRowSelectionInterval(row, row);
            table.scrollRectToVisible(table.getCellRect(row, 0, true));
        }
    }

    private void updateRecord() {
        Object[] record = readRecord();
        if (record == null) {
            return;
        }
        int row = findRow(record[0].toString());
        if (row == -1) {
            showMessage("No record with that Sequence ID. Use Add for a new record.");
            return;
        }
        for (int col = 0; col < COLUMNS.length; col++) {
            model.setValueAt(record[col], row, col);
        }
        showMessage("Record updated.");
    }

    private void deleteRecord() {
        int row = table.getSelectedRow();
        if (row == -1) {
            String id = idField.getText().trim();
            if (!id.isEmpty()) {
                row = findRow(id);
            }
        }
        if (row == -1) {
            showMessage("Select a record (or enter its Sequence ID) to delete.");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Delete record " + model.getValueAt(row, 0) + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            clearFields();
        }
    }

    private void clearFields() {
        idField.setText("");
        organismField.setText("");
        sequenceField.setText("");
        chromosomeField.setText("");
        table.clearSelection();
        idField.requestFocus();
    }

    // ---------- Helpers ----------

    // Returns the row index with the given Sequence ID, or -1
    private int findRow(String id) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equalsIgnoreCase(id)) {
                return i;
            }
        }
        return -1;
    }

    // Validates the form and returns a full table row, or null if invalid
    private Object[] readRecord() {
        String id = idField.getText().trim();
        String organism = organismField.getText().trim();
        String seq = sequenceField.getText().trim().toUpperCase();
        String chrText = chromosomeField.getText().trim();

        if (id.isEmpty() || organism.isEmpty() || seq.isEmpty() || chrText.isEmpty()) {
            showMessage("Please fill in all fields.");
            return null;
        }
        if (!isValidSequence(seq)) {
            showMessage("Raw Sequence may only contain A, C, G and T.");
            return null;
        }
        int chromosome;
        try {
            chromosome = Integer.parseInt(chrText);
        } catch (NumberFormatException ex) {
            showMessage("Chromosome Number must be a whole number.");
            return null;
        }
        if (chromosome < 1) {
            showMessage("Chromosome Number must be 1 or higher.");
            return null;
        }
        return buildRow(id, organism, seq, chromosome);
    }

    private Object[] buildRow(String id, String organism, String seq, int chromosome) {
        String gc = String.format(Locale.US, "%.2f", gcContent(seq));
        return new Object[] {
            id, organism, seq, String.valueOf(chromosome), String.valueOf(seq.length()), gc
        };
    }

    private boolean isValidSequence(String seq) {
        for (int i = 0; i < seq.length(); i++) {
            char c = seq.charAt(i);
            if (c != 'A' && c != 'C' && c != 'G' && c != 'T') {
                return false;
            }
        }
        return true;
    }

    private double gcContent(String seq) {
        int gc = 0;
        for (int i = 0; i < seq.length(); i++) {
            char c = seq.charAt(i);
            if (c == 'G' || c == 'C') {
                gc++;
            }
        }
        return gc * 100.0 / seq.length();
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DnaRecordsApp().setVisible(true);
            }
        });
    }
}
