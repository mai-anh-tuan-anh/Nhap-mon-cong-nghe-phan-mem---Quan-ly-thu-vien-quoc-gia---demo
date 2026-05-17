import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SearchReaderUI extends JFrame {

    public SearchReaderUI() {
        setTitle("Search Reader");
        setSize(700, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 240));

        JLabel title = new JLabel("SEARCH READER");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(250, 10, 200, 30);

        JLabel searchLabel = new JLabel("Search Reader");
        searchLabel.setBounds(20, 50, 120, 25);

        JTextField searchField = new JTextField();
        searchField.setBounds(140, 50, 200, 25);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBounds(140, 90, 100, 30);

        JButton scanBtn = new JButton("Scan Card");
        scanBtn.setBounds(260, 90, 130, 30);

        // Table
        String[] columns = {
             "Reader ID", "Name",
            "DateOfBirth", "Address", "Number", "Barcode"
        };

        Object[][] data = {
            { "001", "Nguyen Van A", "2000-01-01", "Ha Noi", "0123456789", "0123456788"},
            { "002", "Tran Thi B", "1999-05-10", "Nam Dinh", "0987654321", "0123456789"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 140, 650, 100);

        panel.add(title);
        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchBtn);
        panel.add(scanBtn);
        panel.add(scrollPane);

        add(panel);

        // Action Search
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText();
            JOptionPane.showMessageDialog(this, "Searching: " + keyword);
        });

        // Action Scan
        scanBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Scanning reader card...");
        });
    }

    public static void main(String[] args) {
        new SearchReaderUI().setVisible(true);
    }
}