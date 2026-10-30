package frontend;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentPanelApp extends JFrame implements ActionListener {

    // input components
    private JTextField studentIdField, studentNameField;
    private JTextField bookIdField, searchTitleField;

    // buttons for actions
    private JButton searchButton;
    private JButton borrowButton;
    private JButton returnButton;
    private JButton reserveButton;
    private JButton saveButton;
    private JButton clearButton;

    // tables and table models to display data
    private JTable catalogueTable;
    private DefaultTableModel catalogueModel;
    private JTable borrowTable;
    private DefaultTableModel borrowModel;

    // list to store book objects in memory
    private ArrayList<Book> bookList = new ArrayList<>();

    private Color blue = new Color(45, 110, 190);
    private Color green = new Color(40, 150, 80);
    private Color red = new Color(210, 65, 65);
    private Color orange = new Color(220, 135, 40);

    // constructor to build the interface
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
        searchButton = createButton("Search", blue);
        borrowButton = createButton("Borrow", green);
        returnButton = createButton("Return", blue);
        reserveButton = createButton("Reserve", orange);
        saveButton = createButton("Save", green);
        clearButton = createButton("Clear", red);

        // register action listeners
        searchButton.addActionListener(this);
        borrowButton.addActionListener(this);
        returnButton.addActionListener(this);
        reserveButton.addActionListener(this);
        saveButton.addActionListener(this);
        clearButton.addActionListener(this);

        JPanel buttonPanel = new JPanel(new FlowLayout(
                FlowLayout.CENTER, 12, 5));
        buttonPanel.setBackground(Color.WHITE);

        buttonPanel.add(searchButton);
        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(reserveButton);
        buttonPanel.add(saveButton);
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

        // load saved records from text file at startup
        loadDataFromFile();

        // click on catalogue row to fill Book ID and Title
        catalogueTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = catalogueTable.getSelectedRow();
                if (row != -1) {
                    bookIdField.setText(catalogueModel.getValueAt(row, 0).toString());
                    searchTitleField.setText(catalogueModel.getValueAt(row, 1).toString());
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

    // handle button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            searchBook();
        } else if (e.getSource() == borrowButton) {
            borrowBook();
        } else if (e.getSource() == returnButton) {
            returnBook();
        } else if (e.getSource() == reserveButton) {
            reserveBook();
        } else if (e.getSource() == saveButton) {
            saveDataToFile(true);
        } else if (e.getSource() == clearButton) {
            clearFields();
        }
    }

    // load records from text file at the start of project
    private void loadDataFromFile() {
        File file = new File("books.txt");
        if (!file.exists()) {
            return;
        }

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.length() > 0) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        String id = parts[0].trim();
                        String title = parts[1].trim();
                        String author = parts[2].trim();
                        String category = parts[3].trim();
                        int copies = Integer.parseInt(parts[4].trim());
                        Book book = new Book(id, title, author, category, copies);
                        addRow(book);
                    }
                }
            }
            scanner.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error reading from text file");
        }
    }

    // save current catalogue list into text file
    private void saveDataToFile(boolean showMessage) {
        try {
            FileWriter fw = new FileWriter("books.txt");
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < bookList.size(); i++) {
                Book book = bookList.get(i);
                pw.println(book.getBookId() + "," + book.getTitle() + "," + book.getAuthor() + "," + book.getCategory() + "," + book.getCopies());
            }
            pw.close();
            if (showMessage) {
                JOptionPane.showMessageDialog(this, "Table data saved to books.txt");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving data to text file");
        }
    }

    // add one row into the list and catalogue table
    private void addRow(Book book) {
        bookList.add(book);
        catalogueModel.addRow(new Object[]{
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getCopies(),
                book.getStatus()
        });
    }

    // Find book index in bookList by Book ID
    private int findBookById(String id) {
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getBookId().equalsIgnoreCase(id.trim())) {
                return i;
            }
        }
        return -1;
    }

    // Search catalogue by Book ID or Title
    private void searchBook() {
        String id = bookIdField.getText().trim();
        String title = searchTitleField.getText().trim();

        if (id.length() == 0 && title.length() == 0) {
            JOptionPane.showMessageDialog(this, "Enter a Book ID or Title to search");
            return;
        }

        for (int i = 0; i < bookList.size(); i++) {
            Book b = bookList.get(i);
            boolean match = id.length() > 0
                    ? b.getBookId().equalsIgnoreCase(id)
                    : b.getTitle().equalsIgnoreCase(title);

            if (match) {
                catalogueTable.setRowSelectionInterval(i, i);
                catalogueTable.scrollRectToVisible(catalogueTable.getCellRect(i, 0, true));
                bookIdField.setText(b.getBookId());
                searchTitleField.setText(b.getTitle());
                JOptionPane.showMessageDialog(this, "Record found");
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Book not found");
    }

    // Borrow a book
    private void borrowBook() {
        String stuId = studentIdField.getText().trim();
        String stuName = studentNameField.getText().trim();
        String bookId = bookIdField.getText().trim();

        if (stuId.length() == 0 || stuName.length() == 0 || bookId.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please fill in Student ID, Name, and Book ID");
            return;
        }

        if (stuId.length() > 10) {
            JOptionPane.showMessageDialog(this, "Student ID cannot exceed 10 characters");
            return;
        }

        int row = findBookById(bookId);
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Book not found");
            return;
        }

        Book book = bookList.get(row);
        if (book.getCopies() <= 0) {
            JOptionPane.showMessageDialog(this, "Book is out of stock. Use Reserve instead");
            return;
        }

        book.setCopies(book.getCopies() - 1);
        catalogueModel.setValueAt(book.getCopies(), row, 4);
        catalogueModel.setValueAt(book.getStatus(), row, 5);

        String recId = "BR-" + (borrowModel.getRowCount() + 1);
        borrowModel.addRow(new Object[]{
                recId, stuId, stuName, book.getBookId(), book.getTitle(), "Borrowed"
        });

        saveDataToFile(false);
        JOptionPane.showMessageDialog(this, "Book borrowed successfully");
    }

    // Return a borrowed book
    private void returnBook() {
        int row = borrowTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a borrowed book from the bottom table first");
            return;
        }

        String status = borrowModel.getValueAt(row, 5).toString();
        if (!status.equals("Borrowed")) {
            JOptionPane.showMessageDialog(this, "This book is not currently borrowed");
            return;
        }

        String bookId = borrowModel.getValueAt(row, 3).toString();
        int bookRow = findBookById(bookId);

        if (bookRow != -1) {
            Book book = bookList.get(bookRow);
            book.setCopies(book.getCopies() + 1);
            catalogueModel.setValueAt(book.getCopies(), bookRow, 4);
            catalogueModel.setValueAt(book.getStatus(), bookRow, 5);
        }

        borrowModel.setValueAt("Returned", row, 5);
        saveDataToFile(false);
        JOptionPane.showMessageDialog(this, "Book returned successfully");
    }

    // Reserve a book
    private void reserveBook() {
        String stuId = studentIdField.getText().trim();
        String stuName = studentNameField.getText().trim();
        String bookId = bookIdField.getText().trim();

        if (stuId.length() == 0 || stuName.length() == 0 || bookId.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please fill in Student ID, Name, and Book ID");
            return;
        }

        if (stuId.length() > 10) {
            JOptionPane.showMessageDialog(this, "Student ID cannot exceed 10 characters");
            return;
        }

        int row = findBookById(bookId);
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Book not found");
            return;
        }

        Book book = bookList.get(row);
        String recId = "RES-" + (borrowModel.getRowCount() + 1);

        borrowModel.addRow(new Object[]{
                recId, stuId, stuName, book.getBookId(), book.getTitle(), "Reserved"
        });

        JOptionPane.showMessageDialog(this, "Book reserved successfully");
    }

    // Clear fields
    private void clearFields() {
        catalogueTable.clearSelection();
        borrowTable.clearSelection();
        bookIdField.setText("");
        searchTitleField.setText("");
    }

    // main method to run the program
    public static void main(String[] args) {
        StudentPanelApp app = new StudentPanelApp();
        app.setVisible(true);
    }
}
