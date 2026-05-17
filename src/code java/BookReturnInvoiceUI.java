import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BookReturnInvoiceUI extends JFrame {

    public BookReturnInvoiceUI() {
        setTitle("Book Return Invoice");
        setSize(900, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 240));

        // Main Title
        JLabel mainTitle = new JLabel("RETURNING RECEIPT");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 20));
        mainTitle.setBounds(350, 10, 250, 30);
        panel.add(mainTitle);

        // Reader Information label
        JLabel readerInfoLabel = new JLabel("Reader Information");
        readerInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        readerInfoLabel.setBounds(20, 50, 150, 25);
        panel.add(readerInfoLabel);

        // Reader info table
        String[] readerColumns = {"Reader ID", "Name", "DoB", "Address", "Phone number", "Bar Code", "Script Code"};
        Object[][] readerData = {
            {"001", "B", "01/01/2026", "Ha Noi", "0123456789", "0123456789", "1234561234"}
        };
        DefaultTableModel readerModel = new DefaultTableModel(readerData, readerColumns);
        JTable readerTable = new JTable(readerModel);
        JScrollPane readerScroll = new JScrollPane(readerTable);
        readerScroll.setBounds(20, 80, 850, 50);
        panel.add(readerScroll);

        // Unreturned books section
        int y = 140;
        JLabel unreturnedLabel = new JLabel("List of borrowed books that have not been returned");
        unreturnedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        unreturnedLabel.setBounds(20, y, 350, 25);
        panel.add(unreturnedLabel);
        y += 30;

        String[] unreturnedColumns = {
            "Order", "Book Code", "Bar Code", "Name", "Author",
            "Borrowing Date", "Due Date", "Cover Price"
        };
        Object[][] unreturnedData = {
            {"1", "00001", "12345", "Harry Potter", "J.K Rowling", "11/03/2026", "11/04/2026", "10000vnd"}
        };
        DefaultTableModel unreturnedModel = new DefaultTableModel(unreturnedData, unreturnedColumns);
        JTable unreturnedTable = new JTable(unreturnedModel);
        JScrollPane unreturnedScroll = new JScrollPane(unreturnedTable);
        unreturnedScroll.setBounds(20, y, 850, 60);
        panel.add(unreturnedScroll);
        y += 70;

        y += 50;

        // Late return fines section
        JLabel lateLabel = new JLabel("List of books fined for late returning");
        lateLabel.setFont(new Font("Arial", Font.BOLD, 12));
        lateLabel.setBounds(20, y, 300, 25);
        panel.add(lateLabel);
        y += 30;

        String[] lateColumns = {
            "Order", "Book Code", "Bar Code", "Name", "Author",
            "Borrowing Date", "Due Date", "Returning Date", "Cover Price", "Fine Amount (20%)"
        };
        Object[][] lateData = {
            {"1", "00002", "12346", "Diary of a Cricket", "Tô Hoài", "11/03/2026", "11/04/2026", "12/04/2026", "20000vnd", "4000vnd"}
        };
        DefaultTableModel lateModel = new DefaultTableModel(lateData, lateColumns);
        JTable lateTable = new JTable(lateModel);
        JScrollPane lateScroll = new JScrollPane(lateTable);
        lateScroll.setBounds(20, y, 850, 60);
        panel.add(lateScroll);
        y += 70;

        y += 50;

        // Damaged books section
        JLabel damagedLabel = new JLabel("List of books fined for returning damaged book");
        damagedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        damagedLabel.setBounds(20, y, 350, 25);
        panel.add(damagedLabel);
        y += 30;

        String[] damagedColumns = {
            "Order", "Book Code", "Bar Code", "Name", "Author",
            "Cover Price", "Damaged Status Now", "Fine Amount"
        };
        Object[][] damagedData = {
            {"1", "00002", "12346", "Diary of a Cricket", "Tô Hoài", "20000vnd", "TORN", "20000vnd"}
        };
        DefaultTableModel damagedModel = new DefaultTableModel(damagedData, damagedColumns);
        JTable damagedTable = new JTable(damagedModel);
        JScrollPane damagedScroll = new JScrollPane(damagedTable);
        damagedScroll.setBounds(20, y, 850, 60);
        panel.add(damagedScroll);
        y += 70;

        y += 40;

        // Total Amount
        JLabel totalAmountLabel = new JLabel("Total Fine:");
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalAmountLabel.setBounds(300, y, 120, 25);
        panel.add(totalAmountLabel);

        JLabel totalAmountValue = new JLabel("24000vnd");
        totalAmountValue.setFont(new Font("Arial", Font.BOLD, 14));
        totalAmountValue.setBounds(420, y, 150, 25);
        panel.add(totalAmountValue);
        y += 50;
        // Deposit
        JLabel DepositLabel = new JLabel("Deposit:");
        DepositLabel.setFont(new Font("Arial", Font.BOLD, 14));
        DepositLabel.setBounds(300, y, 120, 25);
        panel.add(DepositLabel);

        JLabel DepositValue = new JLabel("20000vnd");
        DepositValue.setFont(new Font("Arial", Font.BOLD, 14));
        DepositValue.setBounds(420, y, 150, 25);
        panel.add(DepositValue);
        y += 50;

        // Bottom buttons
        JButton submitBtn = new JButton("Submit");
        submitBtn.setBounds(200, y, 100, 30);
        panel.add(submitBtn);

        JButton mainPageBtn = new JButton("Back to Main Page");
        mainPageBtn.setBounds(320, y, 150, 30);
        panel.add(mainPageBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(490, y, 100, 30);
        panel.add(backBtn);

        // Action listeners
        submitBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Submit clicked - Invoice submitted!");
        });

        mainPageBtn.addActionListener(e -> {
            new LibrarianHome().setVisible(true);
            dispose();
        });

        backBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Back clicked");
        });

        add(panel);
    }

    public static void main(String[] args) {
        new BookReturnInvoiceUI().setVisible(true);
    }
}
