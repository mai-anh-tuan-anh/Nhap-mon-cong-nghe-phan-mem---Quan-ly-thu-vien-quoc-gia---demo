package view;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import dao.UserDAO;
import model.User;
public class LoginFrm extends JFrame implements ActionListener {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    public LoginFrm() {
        super("Librarian Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("LOGIN");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(160, 20, 100, 30);
        panel.add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 80, 100, 25);
        panel.add(userLabel);
        txtUsername = new JTextField();
        txtUsername.setBounds(150, 80, 180, 25);
        panel.add(txtUsername);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 120, 100, 25);
        panel.add(passLabel);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 120, 180, 25);
        panel.add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(80, 180, 100, 30);
        btnLogin.addActionListener(this);
        panel.add(btnLogin);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(200, 180, 100, 30);
        btnCancel.addActionListener(e -> System.exit(0));
        panel.add(btnCancel);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            User u = new User();
            u.setUsername(txtUsername.getText());
            u.setPassword(new String(txtPassword.getPassword()));
            UserDAO ud = new UserDAO();
            if (ud.checkLogin(u)) {
                if (u.getRole().equals("Librarian")) {
                    (new LibrarianHomeFrm(u)).setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Only Librarians can access this module!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect username or password!");
            }
        }
    }

    public static void main(String[] args) {
        new LoginFrm().setVisible(true);
    }
}
