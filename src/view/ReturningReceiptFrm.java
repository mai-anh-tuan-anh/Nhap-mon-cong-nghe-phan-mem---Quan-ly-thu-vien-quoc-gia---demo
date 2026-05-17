package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.ReturningReceiptDAO;
import model.*;

public class ReturningReceiptFrm extends JFrame implements ActionListener {
    private User user;
    private Reader reader;
    private ReturningReceipt returningReceipt;
    private JButton btnSubmit, btnBack, btnMainPage;
    private JLabel lblTotalFine, lblDeposit;

    public ReturningReceiptFrm(ReturningReceipt rr) {
        super("Returning Receipt");
        this.returningReceipt = rr;
        this.user = rr.getUser();
        this.reader = rr.getReader();
        
        setSize(900, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 240));

        JLabel mainTitle = new JLabel("RETURNING RECEIPT");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 20));
        mainTitle.setBounds(350, 10, 250, 30);
        panel.add(mainTitle);

        JLabel readerInfoLabel = new JLabel("Reader Information");
        readerInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        readerInfoLabel.setBounds(20, 50, 150, 25);
        panel.add(readerInfoLabel);

        String[] readerColumns = {"Reader ID", "Name", "DoB", "Address", "Phone number", "Bar Code"};
        Object[][] readerData = {{reader.getId(), reader.getName(), reader.getDateOfBirth(), reader.getAddress(), reader.getPhoneNumber(), reader.getBarcode()}};
        JTable readerTable = new JTable(new DefaultTableModel(readerData, readerColumns));
        JScrollPane readerScroll = new JScrollPane(readerTable);
        readerScroll.setBounds(20, 80, 850, 50);
        panel.add(readerScroll);

        int y = 140;
        JLabel unreturnedLabel = new JLabel("List of borrowed books that have not been returned");
        unreturnedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        unreturnedLabel.setBounds(20, y, 350, 25);
        panel.add(unreturnedLabel);
        y += 30;

        JTable tblUnreturned = new JTable();
        JScrollPane unreturnedScroll = new JScrollPane(tblUnreturned);
        unreturnedScroll.setBounds(20, y, 850, 60);
        panel.add(unreturnedScroll);
        y += 120;

        JLabel lateLabel = new JLabel("List of books fined for late returning");
        lateLabel.setFont(new Font("Arial", Font.BOLD, 12));
        lateLabel.setBounds(20, y, 300, 25);
        panel.add(lateLabel);
        y += 30;

        JTable tblLate = new JTable();
        JScrollPane lateScroll = new JScrollPane(tblLate);
        lateScroll.setBounds(20, y, 850, 60);
        panel.add(lateScroll);
        y += 120;

        JLabel damagedLabel = new JLabel("List of books fined for returning damaged book");
        damagedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        damagedLabel.setBounds(20, y, 350, 25);
        panel.add(damagedLabel);
        y += 30;

        JTable tblDamaged = new JTable();
        JScrollPane damagedScroll = new JScrollPane(tblDamaged);
        damagedScroll.setBounds(20, y, 850, 60);
        panel.add(damagedScroll);
        y += 110;

        JLabel totalAmountLabel = new JLabel("Total Fine:");
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalAmountLabel.setBounds(300, y, 120, 25);
        panel.add(totalAmountLabel);

        lblTotalFine = new JLabel("0 VND");
        lblTotalFine.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalFine.setBounds(420, y, 150, 25);
        panel.add(lblTotalFine);
        y += 50;

        JLabel depositLabel = new JLabel("Deposit:");
        depositLabel.setFont(new Font("Arial", Font.BOLD, 14));
        depositLabel.setBounds(300, y, 120, 25);
        panel.add(depositLabel);

        lblDeposit = new JLabel("0 VND");
        lblDeposit.setFont(new Font("Arial", Font.BOLD, 14));
        lblDeposit.setBounds(420, y, 150, 25);
        panel.add(lblDeposit);
        y += 50;

        btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(200, y, 100, 30);
        btnSubmit.addActionListener(this);
        panel.add(btnSubmit);

        btnMainPage = new JButton("Back to Main Page");
        btnMainPage.setBounds(320, y, 150, 30);
        btnMainPage.addActionListener(e -> {
            new LibrarianHomeFrm(user).setVisible(true);
            dispose();
        });
        panel.add(btnMainPage);

        btnBack = new JButton("Back");
        btnBack.setBounds(490, y, 100, 30);
        btnBack.addActionListener(e -> {
            new BookReturnFrm(null, returningReceipt).setVisible(true);
            dispose();
        });
        panel.add(btnBack);

        add(panel);
        calculateFines(tblLate, tblDamaged);
    }

    private void calculateFines(JTable tblLate, JTable tblDamaged) {
        float totalFine = 0;
        float totalDeposit = 0;
        
        ArrayList<Object[]> lateData = new ArrayList<>();
        ArrayList<Object[]> damagedData = new ArrayList<>();

        for (int i = 0; i < returningReceipt.getListReturnedBook().size(); i++) {
            ReturnedBook rb = returningReceipt.getListReturnedBook().get(i);
            totalDeposit += rb.getBorrowedBook().getPrice();
            
            float lateFine = 0;
            if (rb.getReturnDate().after(rb.getBorrowedBook().getDueDate())) {
                lateFine = rb.getBorrowedBook().getPrice() * 0.2f;
                lateData.add(new Object[]{lateData.size() + 1, rb.getBorrowedBook().getBook().getCode(), rb.getBorrowedBook().getBook().getBarcode(), rb.getBorrowedBook().getBook().getName(), rb.getBorrowedBook().getBook().getAuthor(), rb.getBorrowedBook().getBorrowDate(), rb.getBorrowedBook().getDueDate(), rb.getReturnDate(), rb.getBorrowedBook().getPrice(), lateFine});
            }
            
            float damageFine = 0;
            if (!rb.getListBookDamage().isEmpty()) {
                StringBuilder damages = new StringBuilder();
                for (BookDamage bd : rb.getListBookDamage()) {
                    damageFine += bd.getFineAmount();
                    damages.append(bd.getDamage().getName()).append("; ");
                }
                damagedData.add(new Object[]{damagedData.size() + 1, rb.getBorrowedBook().getBook().getCode(), rb.getBorrowedBook().getBook().getBarcode(), rb.getBorrowedBook().getBook().getName(), rb.getBorrowedBook().getBook().getAuthor(), rb.getBorrowedBook().getPrice(), damages.toString(), damageFine});
            }
            
            totalFine += lateFine + damageFine;
        }

        String[] lateColumns = {"Order", "Book Code", "Bar Code", "Name", "Author", "Borrowing Date", "Due Date", "Returning Date", "Cover Price", "Fine Amount (20%)"};
        tblLate.setModel(new DefaultTableModel(lateData.toArray(new Object[0][0]), lateColumns));

        String[] damagedColumns = {"Order", "Book Code", "Bar Code", "Name", "Author", "Cover Price", "Damaged Status Now", "Fine Amount"};
        tblDamaged.setModel(new DefaultTableModel(damagedData.toArray(new Object[0][0]), damagedColumns));

        lblTotalFine.setText(totalFine + " VND");
        lblDeposit.setText(totalDeposit + " VND");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSubmit) {
            ReturningReceiptDAO rrd = new ReturningReceiptDAO();
            if (rrd.updateReturningReceipt(returningReceipt)) {
                JOptionPane.showMessageDialog(this, "Book return processed successfully!");
                new LibrarianHomeFrm(user).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to process book return!");
            }
        }
    }
}
