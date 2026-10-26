import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class LibraryManagementSystem extends JFrame {

    private JTextField idField, titleField, authorField;
    private JTextField categoryField, copiesField;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    private Color blue = new Color(45, 110, 190);
    private Color green = new Color(40, 150, 80);
    private Color red = new Color(210, 65, 65);

    public LibraryManagementSystem() {
        setTitle("Library Management System");
        setSize(1150, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main background with space around the card
        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(new Color(240, 243, 247));
        background.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Main card
        JPanel card = new JPanel(new BorderLayout(10, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(215, 220, 228), 1),
                new EmptyBorder(18, 18, 18, 18)
        ));

        // Book details form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(190, 200, 215)),
                "Book Details"
        ));

        idField = new JTextField();
        titleField = new JTextField();
        authorField = new JTextField();
        categoryField = new JTextField();
        copiesField = new JTextField();

        addFormRow(formPanel, 0, "Book ID", idField);
        addFormRow(formPanel, 1, "Title", titleField);
        addFormRow(formPanel, 2, "Author", authorField);
        addFormRow(formPanel, 3, "Category", categoryField);
        addFormRow(formPanel, 4, "Copies", copiesField);

        // Buttons
        JButton addButton = createButton("Add", green);
        JButton searchButton = createButton("Search", blue);
        JButton updateButton = createButton("Update", blue);
        JButton deleteButton = createButton("Delete", red);
        JButton clearButton = createButton("Clear", blue);

        JPanel buttonPanel = new JPanel(new FlowLayout(
                FlowLayout.CENTER, 12, 5));
        buttonPanel.setBackground(Color.WHITE);

        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Form and buttons together
        JPanel topPanel = new JPanel(new BorderLayout(5, 8));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Table
        String[] columns = {
                "Book ID", "Title", "Author", "Category",
                "Copies", "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(25);
        bookTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        bookTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(190, 200, 215)),
                "Book Records"
        ));

        // Add sample records
        addBookToTable("B-1001", "The Overstory",
                "Richard Powers", "Fiction", 3);
        addBookToTable("B-1002", "Sapiens",
                "Yuval Noah Harari", "History", 4);
        addBookToTable("B-1003", "Braiding Sweetgrass",
                "Robin Wall Kimmerer", "Nature", 0);
        addBookToTable("B-1004", "The Design of Everyday Things",
                "Don Norman", "Design", 2);

        // Add panels to card
        card.add(topPanel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        background.add(card, BorderLayout.CENTER);
        add(background);

        // Button action listeners
        addButton.addActionListener(e -> addBook());

        searchButton.addActionListener(e -> searchBook());

        updateButton.addActionListener(e -> updateBook());

        deleteButton.addActionListener(e -> deleteBook());

        clearButton.addActionListener(e -> clearFields());

        // Select a row to fill the form
        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = bookTable.getSelectedRow();

                if (row != -1) {
                    idField.setText(
                            tableModel.getValueAt(row, 0).toString());
                    titleField.setText(
                            tableModel.getValueAt(row, 1).toString());
                    authorField.setText(
                            tableModel.getValueAt(row, 2).toString());
                    categoryField.setText(
                            tableModel.getValueAt(row, 3).toString());
                    copiesField.setText(
                            tableModel.getValueAt(row, 4).toString());
                }
            }
        });
    }

    // Add a label and input field to one form row
    private void addFormRow(JPanel panel, int row,
                            String label, JTextField field) {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.15;
        gbc.anchor = GridBagConstraints.WEST;

        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.85;

        panel.add(field, gbc);
    }

    // Create a colored button
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(95, 32));

        return button;
    }

    // Calculate status and insert a book into the table
    private void addBookToTable(String id, String title,
                                String author, String category,
                                int copies) {

        String status = copies > 0 ? "Available" : "Out of stock";

        tableModel.addRow(new Object[]{
                id, title, author, category, copies, status
        });
    }

    // Read and validate the form
    private boolean validateFields() {
        if (idField.getText().trim().isEmpty()
                || titleField.getText().trim().isEmpty()
                || authorField.getText().trim().isEmpty()
                || categoryField.getText().trim().isEmpty()
                || copiesField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.");
            return false;
        }

        try {
            int copies = Integer.parseInt(
                    copiesField.getText().trim());

            if (copies < 0) {
                JOptionPane.showMessageDialog(this,
                        "Copies cannot be negative.");
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Copies must be a valid whole number.");
            return false;
        }

        return true;
    }

    // Check whether the entered ID already exists
    private int findBookById(String id) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String existingId =
                    tableModel.getValueAt(i, 0).toString();

            if (existingId.equalsIgnoreCase(id.trim())) {
                return i;
            }
        }

        return -1;
    }

    // Add a new book
    private void addBook() {
        if (!validateFields()) {
            return;
        }

        String id = idField.getText().trim();

        if (findBookById(id) != -1) {
            JOptionPane.showMessageDialog(this,
                    "A book with this ID already exists.");
            return;
        }

        int copies = Integer.parseInt(copiesField.getText().trim());

        addBookToTable(
                id,
                titleField.getText().trim(),
                authorField.getText().trim(),
                categoryField.getText().trim(),
                copies
        );

        clearFields();
    }

    // Search using Book ID or Title
    private void searchBook() {
        String id = idField.getText().trim();
        String title = titleField.getText().trim();

        if (id.isEmpty() && title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Enter a Book ID or Title to search.");
            return;
        }

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String existingId =
                    tableModel.getValueAt(i, 0).toString();
            String existingTitle =
                    tableModel.getValueAt(i, 1).toString();

            boolean match;

            if (!id.isEmpty()) {
                match = existingId.equalsIgnoreCase(id);
            } else {
                match = existingTitle.equalsIgnoreCase(title);
            }

            if (match) {
                bookTable.setRowSelectionInterval(i, i);
                bookTable.scrollRectToVisible(
                        bookTable.getCellRect(i, 0, true));
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Book not found.");
    }

    // Update the selected book
    private void updateBook() {
        int row = bookTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a book from the table first.");
            return;
        }

        if (!validateFields()) {
            return;
        }

        String id = idField.getText().trim();
        int existingRow = findBookById(id);

        if (existingRow != -1 && existingRow != row) {
            JOptionPane.showMessageDialog(this,
                    "Another book already uses this ID.");
            return;
        }

        int copies = Integer.parseInt(copiesField.getText().trim());

        tableModel.setValueAt(id, row, 0);
        tableModel.setValueAt(titleField.getText().trim(), row, 1);
        tableModel.setValueAt(authorField.getText().trim(), row, 2);
        tableModel.setValueAt(categoryField.getText().trim(), row, 3);
        tableModel.setValueAt(copies, row, 4);
        tableModel.setValueAt(
                copies > 0 ? "Available" : "Out of stock", row, 5);

        clearFields();
    }

    // Delete the selected book
    private void deleteBook() {
        int row = bookTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a book from the table first.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this book?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
            clearFields();
        }
    }

    // Clear form fields and table selection
    private void clearFields() {
        bookTable.clearSelection();

        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        categoryField.setText("");
        copiesField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryManagementSystem app =
                    new LibraryManagementSystem();

            app.setVisible(true);
        });
    }
}
