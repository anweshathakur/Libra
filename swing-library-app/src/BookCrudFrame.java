import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BookCrudFrame extends JFrame {

    private final List<Book> books = new ArrayList<>();
    private final DefaultTableModel tableModel;
    private final JTable table;
    private int nextId = 1;

    private static final String[] COLUMNS = { "ID", "ISBN", "Title", "Author", "Category", "Copies" };

    public BookCrudFrame(String userName, String userId, String role) {
        setTitle("Library Management System — Book Inventory");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 460);
        setLocationRelativeTo(null);

        seedData();

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshTable();

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JLabel header = new JLabel("Signed in as " + userName + " (" + role + " · " + userId + ")");
        header.setForeground(Color.GRAY);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        addBtn.addActionListener(e -> openForm(null));
        editBtn.addActionListener(e -> {
            Book selected = getSelectedBook();
            if (selected == null) {
                showInfo("Select a row to edit.");
                return;
            }
            openForm(selected);
        });
        deleteBtn.addActionListener(e -> {
            Book selected = getSelectedBook();
            if (selected == null) {
                showInfo("Select a row to delete.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete \"" + selected.getTitle() + "\"?", "Confirm delete",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                books.remove(selected);
                refreshTable();
            }
        });
        refreshBtn.addActionListener(e -> refreshTable());

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel top = new JPanel(new BorderLayout());
        top.add(header, BorderLayout.NORTH);
        top.add(buttonPanel, BorderLayout.SOUTH);
        top.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        root.add(top, BorderLayout.NORTH);
        root.add(scrollPane, BorderLayout.CENTER);

        setContentPane(root);
    }

    private void seedData() {
        books.add(new Book(nextId("B"), "978-0393635522", "The Overstory", "Richard Powers", "Fiction", 3));
        books.add(new Book(nextId("B"), "978-0062316097", "Sapiens", "Yuval Noah Harari", "History", 4));
        books.add(new Book(nextId("B"), "978-1571313560", "Braiding Sweetgrass", "Robin Wall Kimmerer", "Nature", 2));
        books.add(new Book(nextId("B"), "978-0465050659", "The Design of Everyday Things", "Don Norman", "Design", 3));
    }

    private String nextId(String prefix) {
        return prefix + (nextId++);
    }

    private Book getSelectedBook() {
        int row = table.getSelectedRow();
        if (row == -1) return null;
        String id = (String) tableModel.getValueAt(row, 0);
        return books.stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Book b : books) {
            tableModel.addRow(new Object[] { b.getId(), b.getIsbn(), b.getTitle(), b.getAuthor(), b.getCategory(), b.getCopies() });
        }
    }

    private void openForm(Book existing) {
        boolean isEdit = existing != null;

        JTextField isbnField = new JTextField(existing != null ? existing.getIsbn() : "", 18);
        JTextField titleField = new JTextField(existing != null ? existing.getTitle() : "", 18);
        JTextField authorField = new JTextField(existing != null ? existing.getAuthor() : "", 18);
        JTextField categoryField = new JTextField(existing != null ? existing.getCategory() : "", 18);
        JTextField copiesField = new JTextField(existing != null ? String.valueOf(existing.getCopies()) : "", 18);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("ISBN:"));
        form.add(isbnField);
        form.add(new JLabel("Title:"));
        form.add(titleField);
        form.add(new JLabel("Author:"));
        form.add(authorField);
        form.add(new JLabel("Category:"));
        form.add(categoryField);
        form.add(new JLabel("Copies:"));
        form.add(copiesField);

        int result = JOptionPane.showConfirmDialog(this, form,
                isEdit ? "Edit book" : "Add book",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        if (titleField.getText().trim().isEmpty()) {
            showInfo("Title is required.");
            return;
        }

        int copies;
        try {
            copies = Integer.parseInt(copiesField.getText().trim());
        } catch (NumberFormatException ex) {
            showInfo("Copies must be a whole number.");
            return;
        }

        if (isEdit) {
            existing.setIsbn(isbnField.getText().trim());
            existing.setTitle(titleField.getText().trim());
            existing.setAuthor(authorField.getText().trim());
            existing.setCategory(categoryField.getText().trim());
            existing.setCopies(copies);
        } else {
            books.add(new Book(nextId("B"), isbnField.getText().trim(), titleField.getText().trim(),
                    authorField.getText().trim(), categoryField.getText().trim(), copies));
        }
        refreshTable();
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Notice", JOptionPane.INFORMATION_MESSAGE);
    }
}
