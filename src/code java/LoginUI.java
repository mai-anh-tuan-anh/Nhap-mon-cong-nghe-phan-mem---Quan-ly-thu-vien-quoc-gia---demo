import java.awt.*;
import javax.swing.*;

public class LoginUI extends JFrame {

    public LoginUI() {
        setTitle("Librarian Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("LOGIN");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(160, 20, 100, 30);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 80, 100, 25);

        JTextField userField = new JTextField();
        userField.setBounds(150, 80, 180, 25);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 120, 100, 25);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(150, 120, 180, 25);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(80, 180, 100, 30);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(200, 180, 100, 30);

        panel.add(title);
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(loginBtn);
        panel.add(cancelBtn);

        add(panel);

        // Action Login
        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if(user.equals("admin") && pass.equals("123")) {
                JOptionPane.showMessageDialog(this, "Login success!");
                new LibrarianHome().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Wrong username or password!");
            }
        });

        // Action Cancel
        cancelBtn.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        new LoginUI().setVisible(true);
    }
}