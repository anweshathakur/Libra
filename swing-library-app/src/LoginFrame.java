import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class LoginFrame extends JFrame {

    // Mock directories — replace with a real DB/service lookup later
    private static final Map<String, String> STUDENTS = new HashMap<>();
    private static final Map<String, String> LIBRARIANS = new HashMap<>();
    static {
        STUDENTS.put("STU-2291", "Amara Chen");
        STUDENTS.put("STU-1187", "Rohan Mehta");
        STUDENTS.put("STU-3305", "Priya Nair");
        STUDENTS.put("STU-0942", "Leo Fontaine");
        LIBRARIANS.put("LIB-001", "Maya Osei");
    }

    private final JRadioButton studentRadio = new JRadioButton("Student", true);
    private final JRadioButton librarianRadio = new JRadioButton("Librarian");
    private final JLabel idLabel = new JLabel("Registration number:");
    private final JTextField idField = new JTextField(16);
    private final JLabel errorLabel = new JLabel(" ");

    public LoginFrame() {
        setTitle("Library Management System — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel title = new JLabel("Library Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Sign in as a student or librarian");
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(2, 0, 16, 0));

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(studentRadio);
        roleGroup.add(librarianRadio);

        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rolePanel.add(studentRadio);
        rolePanel.add(librarianRadio);
        rolePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        studentRadio.addActionListener(e -> idLabel.setText("Registration number:"));
        librarianRadio.addActionListener(e -> idLabel.setText("Staff ID:"));

        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        idLabel.setBorder(BorderFactory.createEmptyBorder(14, 0, 4, 0));

        idField.setAlignmentX(Component.LEFT_ALIGNMENT);
        idField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        errorLabel.setForeground(new Color(0xB2, 0x3A, 0x2E));
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        errorLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JButton loginButton = new JButton("Enter");
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.addActionListener(this::handleLogin);

        JLabel hint = new JLabel("Try: STU-2291 (student) or LIB-001 (librarian)");
        hint.setFont(new Font("Monospaced", Font.PLAIN, 11));
        hint.setForeground(Color.GRAY);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        hint.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        root.add(title);
        root.add(subtitle);
        root.add(rolePanel);
        root.add(idLabel);
        root.add(idField);
        root.add(errorLabel);
        root.add(Box.createRigidArea(new Dimension(0, 14)));
        root.add(loginButton);
        root.add(hint);

        getRootPane().setDefaultButton(loginButton);
        setContentPane(root);
    }

    private void handleLogin(ActionEvent e) {
        String id = idField.getText().trim().toUpperCase();
        boolean isStudent = studentRadio.isSelected();
        Map<String, String> directory = isStudent ? STUDENTS : LIBRARIANS;
        String name = directory.get(id);

        if (name == null) {
            errorLabel.setText(isStudent
                    ? "That registration number isn't on file."
                    : "That staff ID isn't on file.");
            return;
        }

        errorLabel.setText(" ");
        String role = isStudent ? "Student" : "Librarian";
        dispose();
        SwingUtilities.invokeLater(() -> new BookCrudFrame(name, id, role).setVisible(true));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
