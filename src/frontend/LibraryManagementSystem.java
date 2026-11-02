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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class LibraryManagementSystem extends JFrame implements ActionListener {

    // input components
    private JTextField idField, titleField, authorField;
    private JTextField categoryField, copiesField;

    // buttons for actions
    private JButton addButton;
    private JButton searchButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton saveButton;
    private JButton clearButton;

    // table and table model to display data
    private JTable bookTable;
    private DefaultTableModel tableModel;

    // list to store book objects in memory
    private ArrayList<Book> bookList = new ArrayList<>();

    private Color blue = new Color(45, 110, 190);
    private Color green = new Color(40, 150, 80);
    private Color red = new Color(210, 65, 65);

    // constructor to build the interface
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
        addButton = createButton("Add", green);
        searchButton = createButton("Search", blue);
        updateButton = createButton("Update", blue);
        deleteButton = createButton("Delete", red);
        saveButton = createButton("Save", green);
        clearButton = createButton("Clear", blue);

        // register action listeners
        addButton.addActionListener(this);
        searchButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        saveButton.addActionListener(this);
        clearButton.addActionListener(this);

        JPanel buttonPanel = new JPanel(new FlowLayout(
                FlowLayout.CENTER, 12, 5));
        buttonPanel.setBackground(Color.WHITE);

        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
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

        // Add panels to card
        card.add(topPanel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        background.add(card, BorderLayout.CENTER);
        add(background);

        // load saved records from text file at startup
        loadDataFromFile();

        // click on table row to fill inputs
        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bookTable.getSelectedRow();
                if (row != -1) {
                    idField.setText(tableModel.getValueAt(row, 0).toString());
                    titleField.setText(tableModel.getValueAt(row, 1).toString());
                    authorField.setText(tableModel.getValueAt(row, 2).toString());
                    categoryField.setText(tableModel.getValueAt(row, 3).toString());
                    copiesField.setText(tableModel.getValueAt(row, 4).toString());
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
        if (e.getSource() == addButton) {
            addBook();
        } else if (e.getSource() == searchButton) {
            searchBook();
        } else if (e.getSource() == updateButton) {
            updateBook();
        } else if (e.getSource() == deleteButton) {
            deleteBook();
        } else if (e.getSource() == saveButton) {
            saveDataToFile(true);
        } else if (e.getSource() == clearButton) {
            clearFields();
        }
    }

    // load records from text file at the start of project with exception handling
    private void loadDataFromFile() {
        File file = new File("books.txt");
        if (!file.exists()) {
            return;
        }

        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.length() > 0) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        try {
                            String id = parts[0].trim();
                            String title = parts[1].trim();
                            String author = parts[2].trim();
                            String category = parts[3].trim();
                            int copies = Integer.parseInt(parts[4].trim());
                            Book book = new Book(id, title, author, category, copies);
                            addRow(book);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Skipping invalid number in books.txt: " + ex.getMessage());
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(this, "Skipping invalid record in books.txt: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "File books.txt not found: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error reading from text file: " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    // save current book list into text file with exception handling
    private void saveDataToFile(boolean showMessage) {
        PrintWriter pw = null;
        try {
            FileWriter fw = new FileWriter("books.txt");
            pw = new PrintWriter(fw);
            for (int i = 0; i < bookList.size(); i++) {
                Book book = bookList.get(i);
                pw.println(book.getBookId() + "," + book.getTitle() + "," + book.getAuthor() + "," + book.getCategory() + "," + book.getCopies());
            }
            if (showMessage) {
                JOptionPane.showMessageDialog(this, "Table data saved to books.txt");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data to text file: " + e.getMessage());
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    // add one row into the list and table
    private void addRow(Book book) {
        bookList.add(book);
        tableModel.addRow(new Object[]{
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getCopies(),
                book.getStatus()
        });
    }

    // function to add new book with exception handling
    private void addBook() {
        try {
            String id = idField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String category = categoryField.getText().trim();
            String copiesText = copiesField.getText().trim();

            // validation for empty fields
            if (id.length() == 0 || title.length() == 0 || author.length() == 0
                    || category.length() == 0 || copiesText.length() == 0) {
                throw new IllegalArgumentException("Please fill in all text fields");
            }

            // validation for book id length
            if (id.length() > 10) {
                throw new IllegalArgumentException("Book ID cannot exceed 10 characters");
            }

            // check if book id already exists in object list
            for (int i = 0; i < bookList.size(); i++) {
                if (bookList.get(i).getBookId().equalsIgnoreCase(id)) {
                    throw new IllegalArgumentException("Book ID must be unique");
                }
            }

            if (copiesText.length() > 7) {
                throw new IllegalArgumentException("Copies cannot exceed 7 digits");
            }

            // parse copies and validate via Book constructor
            int copies = Integer.parseInt(copiesText);
            Book book = new Book(id, title, author, category, copies);

            addRow(book);
            saveDataToFile(false);
            clearFields();
            JOptionPane.showMessageDialog(this, "Record added to table successfully");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Copies must be a valid whole number");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // function to search record by Book ID or Title
    private void searchBook() {
        try {
            String searchId = idField.getText().trim();
            String searchTitle = titleField.getText().trim();

            if (searchId.length() == 0 && searchTitle.length() == 0) {
                throw new IllegalArgumentException("Enter a Book ID or Title to search");
            }

            boolean found = false;
            for (int i = 0; i < bookList.size(); i++) {
                Book b = bookList.get(i);
                boolean match = searchId.length() > 0
                        ? b.getBookId().equalsIgnoreCase(searchId)
                        : b.getTitle().equalsIgnoreCase(searchTitle);

                if (match) {
                    bookTable.setRowSelectionInterval(i, i);
                    bookTable.scrollRectToVisible(bookTable.getCellRect(i, 0, true));
                    idField.setText(b.getBookId());
                    titleField.setText(b.getTitle());
                    authorField.setText(b.getAuthor());
                    categoryField.setText(b.getCategory());
                    copiesField.setText(String.valueOf(b.getCopies()));
                    JOptionPane.showMessageDialog(this, "Record found");
                    found = true;
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "Record not found");
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // function to update selected record with exception handling
    private void updateBook() {
        try {
            int row = bookTable.getSelectedRow();
            if (row == -1) {
                throw new IllegalArgumentException("Please select a row from the table first");
            }

            String id = idField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String category = categoryField.getText().trim();
            String copiesText = copiesField.getText().trim();

            // validation for empty fields
            if (id.length() == 0 || title.length() == 0 || author.length() == 0
                    || category.length() == 0 || copiesText.length() == 0) {
                throw new IllegalArgumentException("Please fill in all text fields");
            }

            // validation for book id length
            if (id.length() > 10) {
                throw new IllegalArgumentException("Book ID cannot exceed 10 characters");
            }

            // check if book id already exists for another record in object list
            for (int i = 0; i < bookList.size(); i++) {
                if (i != row && bookList.get(i).getBookId().equalsIgnoreCase(id)) {
                    throw new IllegalArgumentException("Book ID already exists for another record");
                }
            }

            if (copiesText.length() > 7) {
                throw new IllegalArgumentException("Copies cannot exceed 7 digits");
            }

            int copies = Integer.parseInt(copiesText);
            Book book = new Book(id, title, author, category, copies);

            // update both object list and table display
            bookList.set(row, book);
            tableModel.setValueAt(book.getBookId(), row, 0);
            tableModel.setValueAt(book.getTitle(), row, 1);
            tableModel.setValueAt(book.getAuthor(), row, 2);
            tableModel.setValueAt(book.getCategory(), row, 3);
            tableModel.setValueAt(book.getCopies(), row, 4);
            tableModel.setValueAt(book.getStatus(), row, 5);

            saveDataToFile(false);
            clearFields();
            JOptionPane.showMessageDialog(this, "Record updated successfully");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Copies must be a valid whole number");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // function to delete selected record with confirmation and exception handling
    private void deleteBook() {
        try {
            int row = bookTable.getSelectedRow();
            if (row == -1) {
                throw new IllegalArgumentException("Please select a row from the table first");
            }

            // confirmation dialog before deleting
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this book?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                // remove from both object list and table display
                bookList.remove(row);
                tableModel.removeRow(row);
                saveDataToFile(false);
                clearFields();
                JOptionPane.showMessageDialog(this, "Record deleted successfully");
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
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

    // main method to run the program
    public static void main(String[] args) {
        LibraryManagementSystem app = new LibraryManagementSystem();
        app.setVisible(true);
    }
}
