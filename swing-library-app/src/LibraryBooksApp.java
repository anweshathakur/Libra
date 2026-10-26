import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryBooksApp extends JFrame implements ActionListener, ListSelectionListener {

    private JTextField idField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField categoryField;
    private JTextField copiesField;

    private JButton addBtn;
    private JButton searchBtn;
    private JButton updateBtn;
    private JButton deleteBtn;
    private JButton clearBtn;

    private JTable table;
    private DefaultTableModel model;

    private static final String[] COLUMNS = {
        "Book ID", "Title", "Author", "Category", "Copies", "Status"
    };

    public LibraryBooksApp() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 540);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- Book Details form (GridLayout) ----
        idField = new JTextField();
        titleField = new JTextField();
        authorField = new JTextField();
        categoryField = new JTextField();
        copiesField = new JTextField();

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));
        formPanel.add(new JLabel("Book ID"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Title"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Author"));
        formPanel.add(authorField);
        formPanel.add(new JLabel("Category"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Copies"));
        formPanel.add(copiesField);

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

        // ---- Book Records table ----
        model = new DefaultTableModel(COLUMNS, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(this);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Book Records"));
        add(scrollPane, BorderLayout.CENTER);

        // ---- Sample data ----
        model.addRow(buildRow("B-1001", "The Overstory", "Richard Powers", "Fiction", 3));
        model.addRow(buildRow("B-1002", "Sapiens", "Yuval Noah Harari", "History", 4));
        model.addRow(buildRow("B-1003", "Braiding Sweetgrass", "Robin Wall Kimmerer", "Nature", 0));
        model.addRow(buildRow("B-1004", "The Design of Everyday Things", "Don Norman", "Design", 2));
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
        titleField.setText(model.getValueAt(row, 1).toString());
        authorField.setText(model.getValueAt(row, 2).toString());
        categoryField.setText(model.getValueAt(row, 3).toString());
        copiesField.setText(model.getValueAt(row, 4).toString());
    }

    private void addRecord() {
        Object[] record = readRecord();
        if (record == null) {
            return;
        }
        if (findRow(record[0].toString()) != -1) {
            showMessage("A book with that Book ID already exists.");
            return;
        }
        model.addRow(record);
        clearFields();
    }

    private void searchRecord() {
        String id = idField.getText().trim();
        String title = titleField.getText().trim();
        int row = -1;

        if (!id.isEmpty()) {
            row = findRow(id);
        } else if (!title.isEmpty()) {
            for (int i = 0; i < model.getRowCount(); i++) {
                String name = model.getValueAt(i, 1).toString().toLowerCase();
                if (name.contains(title.toLowerCase())) {
                    row = i;
                    break;
                }
            }
        } else {
            showMessage("Enter a Book ID or Title to search.");
            return;
        }

        if (row == -1) {
            showMessage("No matching book found.");
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
            showMessage("No book with that Book ID. Use Add for a new book.");
            return;
        }
        for (int col = 0; col < COLUMNS.length; col++) {
            model.setValueAt(record[col], row, col);
        }
        showMessage("Book updated.");
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
            showMessage("Select a book (or enter its Book ID) to delete.");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Delete book " + model.getValueAt(row, 0) + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            clearFields();
        }
    }

    private void clearFields() {
        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        categoryField.setText("");
        copiesField.setText("");
        table.clearSelection();
        idField.requestFocus();
    }

    // ---------- Helpers ----------

    // Returns the row index with the given Book ID, or -1
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
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String category = categoryField.getText().trim();
        String copiesText = copiesField.getText().trim();

        if (id.isEmpty() || title.isEmpty() || author.isEmpty()
                || category.isEmpty() || copiesText.isEmpty()) {
            showMessage("Please fill in all fields.");
            return null;
        }
        int copies;
        try {
            copies = Integer.parseInt(copiesText);
        } catch (NumberFormatException ex) {
            showMessage("Copies must be a whole number.");
            return null;
        }
        if (copies < 0) {
            showMessage("Copies cannot be negative.");
            return null;
        }
        return buildRow(id, title, author, category, copies);
    }

    private Object[] buildRow(String id, String title, String author, String category, int copies) {
        String status = copies > 0 ? "Available" : "Out of stock";
        return new Object[] { id, title, author, category, String.valueOf(copies), status };
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LibraryBooksApp().setVisible(true);
            }
        });
    }
}
