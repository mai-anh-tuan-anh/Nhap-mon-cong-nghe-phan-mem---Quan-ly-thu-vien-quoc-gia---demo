package view;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.User;
public class LibrarianHomeFrm extends JFrame implements ActionListener {
    private User user;
    private JButton btnReturnBook;
    private JButton btnLogout;
    public LibrarianHomeFrm(User user) {
        super("Librarian Home");
        this.user = user;
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(220, 220, 220));

        JLabel title = new JLabel("LIBRARIAN HOME");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(180, 20, 200, 30);
        panel.add(title);

        JLabel name = new JLabel(user.getFullName());
        name.setBounds(200, 60, 150, 20);
        panel.add(name);

        btnReturnBook = new JButton("Return Book");
        btnReturnBook.setBounds(180, 100, 140, 30);
        btnReturnBook.addActionListener(this);
        panel.add(btnReturnBook);

        btnLogout = new JButton("Logout");
        btnLogout.setBounds(180, 150, 140, 30);
        btnLogout.addActionListener(e -> {
            new LoginFrm().setVisible(true);
            this.dispose();
        });
        panel.add(btnLogout);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnReturnBook) {
            (new SearchReaderFrm(user)).setVisible(true);
            this.dispose();
        }
    }
}
