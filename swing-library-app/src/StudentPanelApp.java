import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentPanelApp extends JFrame {

    private JTextField studentIdField, studentNameField;
    private JTextField bookIdField, searchTitleField;
    private JTable catalogueTable;
    private DefaultTableModel catalogueModel;
    private JTable borrowTable;
    private DefaultTableModel borrowModel;

    private Color blue = new Color(45, 110, 190);
    private Color green = new Color(40, 150, 80);
    private Color red = new Color(210, 65, 65);
    private Color orange = new Color(220, 135, 40);

    public StudentPanelApp() {
        setTitle("Libra - Student Panel");
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

        // Student details form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(190, 200, 215)),
                "Student Details & Book Selection"
        ));

        studentIdField = new JTextField("STU-101");
        studentNameField = new JTextField("Amara Chen");
        bookIdField = new JTextField();
        searchTitleField = new JTextField();

        addFormRow(formPanel, 0, "Student ID", studentIdField);
        addFormRow(formPanel, 1, "Student Name", studentNameField);
        addFormRow(formPanel, 2, "Book ID", bookIdField);
        addFormRow(formPanel, 3, "Search Title", searchTitleField);

        // Buttons
        JButton searchButton = createButton("Search", blue);
        JButton borrowButton = createButton("Borrow", green);
        JButton returnButton = createButton("Return", blue);
        JButton reserveButton = createButton("Reserve", orange);
        JButton clearButton = createButton("Clear", red);

        JPanel buttonPanel = new JPanel(new FlowLayout(
                FlowLayout.CENTER, 12, 5));
        buttonPanel.setBackground(Color.WHITE);

        buttonPanel.add(searchButton);
        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(reserveButton);
        buttonPanel.add(clearButton);

        // Form and buttons together
        JPanel topPanel = new JPanel(new BorderLayout(5, 8));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Catalogue Table
        String[] bookColumns = {
                "Book ID", "Title", "Author", "Category",
                "Copies", "Status"
        };

        catalogueModel = new DefaultTableModel(bookColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        catalogueTable = new JTable(catalogueModel);
        catalogueTable.setRowHeight(25);
        catalogueTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        catalogueTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane catalogueScroll = new JScrollPane(catalogueTable);
        catalogueScroll.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(190, 200, 215)),
                "Available Books Catalogue"
        ));

        // Add sample records
        addBookToCatalogue("B-1001", "The Overstory",
                "Richard Powers", "Fiction", 3);
        addBookToCatalogue("B-1002", "Sapiens",
                "Yuval Noah Harari", "History", 4);
        addBookToCatalogue("B-1003", "Braiding Sweetgrass",
                "Robin Wall Kimmerer", "Nature", 0);
        addBookToCatalogue("B-1004", "The Design of Everyday Things",
                "Don Norman", "Design", 2);

        // Student Borrow / Reservation Table
        String[] borrowColumns = {
                "Record ID", "Student ID", "Student Name",
                "Book ID", "Title", "Status"
        };

        borrowModel = new DefaultTableModel(borrowColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        borrowTable = new JTable(borrowModel);
        borrowTable.setRowHeight(25);
        borrowTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        borrowTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane borrowScroll = new JScrollPane(borrowTable);
        borrowScroll.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(190, 200, 215)),
                "My Borrowed & Reserved Books"
        ));

        JPanel tablesPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        tablesPanel.setBackground(Color.WHITE);
        tablesPanel.add(catalogueScroll);
        tablesPanel.add(borrowScroll);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(tablesPanel, BorderLayout.CENTER);

        background.add(card, BorderLayout.CENTER);
        add(background);

        // Button action listeners
        searchButton.addActionListener(e -> searchBook());
        borrowButton.addActionListener(e -> borrowBook());
        returnButton.addActionListener(e -> returnBook());
        reserveButton.addActionListener(e -> reserveBook());
        clearButton.addActionListener(e -> clearFields());

        // Select a row in catalogue to fill Book ID
        catalogueTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = catalogueTable.getSelectedRow();
                if (row != -1) {
                    bookIdField.setText(
                            catalogueModel.getValueAt(row, 0).toString());
                    searchTitleField.setText(
                            catalogueModel.getValueAt(row, 1).toString());
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

    // Insert a sample book into the catalogue table
    private void addBookToCatalogue(String id, String title,
                                    String author, String category,
                                    int copies) {

        String status = copies > 0 ? "Available" : "Out of stock";

        catalogueModel.addRow(new Object[]{
                id, title, author, category, copies, status
        });
    }

    // Find book row index by Book ID
    private int findBookById(String id) {
        for (int i = 0; i < catalogueModel.getRowCount(); i++) {
            String existingId =
                    catalogueModel.getValueAt(i, 0).toString();

            if (existingId.equalsIgnoreCase(id.trim())) {
                return i;
            }
        }

        return -1;
    }

    // Search catalogue by Book ID or Title
    private void searchBook() {
        String id = bookIdField.getText().trim();
        String title = searchTitleField.getText().trim();

        if (id.isEmpty() && title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Enter a Book ID or Title to search.");
            return;
        }

        for (int i = 0; i < catalogueModel.getRowCount(); i++) {
            String existingId =
                    catalogueModel.getValueAt(i, 0).toString();
            String existingTitle =
                    catalogueModel.getValueAt(i, 1).toString();

            boolean match = !id.isEmpty()
                    ? existingId.equalsIgnoreCase(id)
                    : existingTitle.equalsIgnoreCase(title);

            if (match) {
                catalogueTable.setRowSelectionInterval(i, i);
                catalogueTable.scrollRectToVisible(
                        catalogueTable.getCellRect(i, 0, true));
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Book not found.");
    }

    // Borrow a book
    private void borrowBook() {
        String stuId = studentIdField.getText().trim();
        String stuName = studentNameField.getText().trim();
        String bookId = bookIdField.getText().trim();

        if (stuId.isEmpty() || stuName.isEmpty() || bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in Student ID, Name, and Book ID.");
            return;
        }

        int row = findBookById(bookId);
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Book not found.");
            return;
        }

        int copies = Integer.parseInt(
                catalogueModel.getValueAt(row, 4).toString());
        String title = catalogueModel.getValueAt(row, 1).toString();

        if (copies <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Book is out of stock. Use Reserve instead.");
            return;
        }

        copies--;
        catalogueModel.setValueAt(copies, row, 4);
        catalogueModel.setValueAt(
                copies > 0 ? "Available" : "Out of stock", row, 5);

        String recId = "BR-" + (borrowModel.getRowCount() + 1);
        borrowModel.addRow(new Object[]{
                recId, stuId, stuName, bookId, title, "Borrowed"
        });
    }

    // Return a borrowed book
    private void returnBook() {
        int row = borrowTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a borrowed book from the bottom table first.");
            return;
        }

        String status = borrowModel.getValueAt(row, 5).toString();
        if (!status.equals("Borrowed")) {
            JOptionPane.showMessageDialog(this,
                    "This book is not currently borrowed.");
            return;
        }

        String bookId = borrowModel.getValueAt(row, 3).toString();
        int bookRow = findBookById(bookId);

        if (bookRow != -1) {
            int copies = Integer.parseInt(
                    catalogueModel.getValueAt(bookRow, 4).toString()) + 1;
            catalogueModel.setValueAt(copies, bookRow, 4);
            catalogueModel.setValueAt("Available", bookRow, 5);
        }

        borrowModel.setValueAt("Returned", row, 5);
    }

    // Reserve a book
    private void reserveBook() {
        String stuId = studentIdField.getText().trim();
        String stuName = studentNameField.getText().trim();
        String bookId = bookIdField.getText().trim();

        if (stuId.isEmpty() || stuName.isEmpty() || bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in Student ID, Name, and Book ID.");
            return;
        }

        int row = findBookById(bookId);
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Book not found.");
            return;
        }

        String title = catalogueModel.getValueAt(row, 1).toString();
        String recId = "RES-" + (borrowModel.getRowCount() + 1);

        borrowModel.addRow(new Object[]{
                recId, stuId, stuName, bookId, title, "Reserved"
        });
    }

    // Clear fields
    private void clearFields() {
        catalogueTable.clearSelection();
        borrowTable.clearSelection();
        bookIdField.setText("");
        searchTitleField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentPanelApp app = new StudentPanelApp();
            app.setVisible(true);
        });
    }
}
