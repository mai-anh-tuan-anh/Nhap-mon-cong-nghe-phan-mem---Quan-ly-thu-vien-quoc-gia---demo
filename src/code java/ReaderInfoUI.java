import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReaderInfoUI extends JFrame {

    public ReaderInfoUI() {
        setTitle("Reader Information");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JLabel title = new JLabel("READER DETAIL");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(350, 10, 200, 30);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 240));

        // Top buttons
        JButton backBtn = new JButton("Back");
        backBtn.setBounds(20, 10, 80, 25);

        JButton scanBookBtn = new JButton("Scan Book");
        scanBookBtn.setBounds(110, 10, 100, 25);

        JButton nextBtn = new JButton("Next");
        nextBtn.setBounds(220, 10, 80, 25);

        
        panel.add(title);
        // Reader Information label
        JLabel readerInfoLabel = new JLabel("Reader Information");
        readerInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        readerInfoLabel.setBounds(20, 45, 150, 25);
        panel.add(readerInfoLabel);

        // Reader info table
        String[] readerColumns = {"Reader ID", "Name", "DoB", "Address", "Phone number", "Bar Code"};
        Object[][] readerData = {
            {"001", "B", "01/01/2026", "Ha Noi", "0123456789", "0123456789"}
        };
        DefaultTableModel readerModel = new DefaultTableModel(readerData, readerColumns);
        JTable readerTable = new JTable(readerModel);
        JScrollPane readerScroll = new JScrollPane(readerTable);
        readerScroll.setBounds(20, 75, 850, 50);
        panel.add(readerScroll);

        // Unreturned books section
        JLabel unreturnedLabel = new JLabel("List of borrowed books that have not been returned");
        unreturnedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        unreturnedLabel.setBounds(20, 135, 350, 25);
        panel.add(unreturnedLabel);

        String[] unreturnedColumns = {
            "Order", "Book Code", "Bar Code", "Name", "Author",
            "Borrowing Date", "Due Date", "Cover Price"
        };
        Object[][] unreturnedData = {
            {"1", "00001", "12345", "Harry Potter", "J.K Rowling", "11/03/2026", "11/04/2026", "10000vnd"},
            {"2", "00002", "12346", "Diary of a Cricket", "To Hoai", "11/03/2026", "11/04/2026", "20000vnd"}
        };
        DefaultTableModel unreturnedModel = new DefaultTableModel(unreturnedData, unreturnedColumns);
        JTable unreturnedTable = new JTable(unreturnedModel);
        JScrollPane unreturnedScroll = new JScrollPane(unreturnedTable);
        unreturnedScroll.setBounds(20, 165, 850, 80);
        panel.add(unreturnedScroll);

        // Returned books section
        JLabel returnedLabel = new JLabel("List of borrowed books that have been returned");
        returnedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        returnedLabel.setBounds(20, 300, 350, 25);
        panel.add(returnedLabel);

        String[] returnedColumns = {
            "Order", "Book Code", "Bar Code", "Name", "Author",
            "Cover Price", "Due Date", "Returning Date", "Fine Amount"
        };
        Object[][] returnedData = {
            {"1", "00003", "12347", "Tam Cam", "Unknown", "30000vnd", "01/04/2026", "20/03/2026", "0vnd"},
            {"2", "00004", "12348", "The lotus shoes", "Jane Yang", "40000vnd", "01/04/2026", "21/03/2026", "40000vnd"}
        };
        DefaultTableModel returnedModel = new DefaultTableModel(returnedData, returnedColumns);
        JTable returnedTable = new JTable(returnedModel);
        JScrollPane returnedScroll = new JScrollPane(returnedTable);
        returnedScroll.setBounds(20, 330, 850, 80);
        panel.add(returnedScroll);

        // Bottom buttons
        JButton scanBtn = new JButton("Scan book");
        scanBtn.setBounds(20, 450, 100, 30);
        panel.add(scanBtn);

        JButton bottomBackBtn = new JButton("Back");
        bottomBackBtn.setBounds(20, 500, 80, 30);
        panel.add(bottomBackBtn);

        JButton bottomNextBtn = new JButton("Next");
        bottomNextBtn.setBounds(120, 500, 80, 30);
        panel.add(bottomNextBtn);

        // Add action listeners
        backBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Back clicked");
        });

        scanBookBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Scan Book clicked");
        });

        nextBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Next clicked");
        });

        scanBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Scan book clicked");
        });

        bottomBackBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Back clicked");
        });

        bottomNextBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Next clicked");
        });

        add(panel);
    }

    public static void main(String[] args) {
        new ReaderInfoUI().setVisible(true);
    }
}
