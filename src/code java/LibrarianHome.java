import java.awt.*;
import javax.swing.*;

public class LibrarianHome extends JFrame {

    public LibrarianHome() {
        setTitle("Librarian Home");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(220, 220, 220));

        JLabel title = new JLabel("LIBRARIAN HOME");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(180, 20, 200, 30);

        JLabel name = new JLabel("Librarian FullName");
        name.setBounds(200, 60, 150, 20);

        JButton returnBtn = new JButton("Return Book");
        returnBtn.setBounds(180, 100, 140, 30);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(180, 150, 140, 30);

        panel.add(title);
        panel.add(name);
        panel.add(returnBtn);
        panel.add(cancelBtn);

        add(panel);
    }

    public static void main(String[] args) {
        new LibrarianHome().setVisible(true);
    }
}