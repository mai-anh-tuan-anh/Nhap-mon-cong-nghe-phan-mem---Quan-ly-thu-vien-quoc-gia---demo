package view;

import dao.ReturningReceiptDAO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;

public class ReturningReceiptFrm extends JFrame implements ActionListener {
    private User user;
    private Reader reader;
    private BorrowingReceipt borrowingReceipt;
    private ReturningReceipt returningReceipt;
    private ArrayList<BookDamage> listBookDamage;
    private JButton btnSubmit, btnBack, btnMainPage;
    private JLabel lblTotalFine, lblDeposit;
    private JTable tblUnreturned, tblLate, tblDamaged;

    public ReturningReceiptFrm(BorrowingReceipt br, ReturningReceipt rr, ArrayList<BookDamage> listBD) {
        super("Returning Receipt");
        this.borrowingReceipt = br;
        this.returningReceipt = rr;
        this.listBookDamage = listBD;
        this.user = rr.getUser();
        this.reader = rr.getReader();
        
        setSize(900, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 240));

        JLabel mainTitle = new JLabel("RETURNING RECEIPT");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 20));
        mainTitle.setBounds(350, 10, 250, 30);
        panel.add(mainTitle);

        panel.add(new JLabel("Reader Information")).setBounds(20, 50, 150, 25);
        SimpleDateFormat sdfC = new SimpleDateFormat("yyyy/MM/dd");
        String[] readerColumns = {"Reader ID", "Name", "DoB", "Address", "Phone number", "Bar Code"};
        Object[][] readerData = {{reader.getId(), reader.getName(), sdfC.format(reader.getDateOfBirth()), reader.getAddress(), reader.getPhoneNumber(), reader.getBarcode()}};
        panel.add(new JScrollPane(new JTable(new DefaultTableModel(readerData, readerColumns)))).setBounds(20, 80, 850, 50);

        int y = 140;
        panel.add(new JLabel("List of borrowed books that have not been returned")).setBounds(20, y, 350, 25); y += 30;
        tblUnreturned = new JTable(); JScrollPane unreturnedScroll = new JScrollPane(tblUnreturned);
        unreturnedScroll.setBounds(20, y, 850, 60); panel.add(unreturnedScroll); y += 120;

        panel.add(new JLabel("List of books fined for late returning")).setBounds(20, y, 300, 25); y += 30;
        tblLate = new JTable(); JScrollPane lateScroll = new JScrollPane(tblLate);
        lateScroll.setBounds(20, y, 850, 60); panel.add(lateScroll); y += 120;

        panel.add(new JLabel("List of books fined for returning damaged book")).setBounds(20, y, 350, 25); y += 30;
        tblDamaged = new JTable(); JScrollPane damagedScroll = new JScrollPane(tblDamaged);
        damagedScroll.setBounds(20, y, 850, 60); panel.add(damagedScroll); y += 110;

        panel.add(new JLabel("Total Fine:")).setBounds(300, y, 120, 25);
        lblTotalFine = new JLabel("0 VND"); lblTotalFine.setBounds(420, y, 150, 25); panel.add(lblTotalFine); y += 50;
        panel.add(new JLabel("Deposit:")).setBounds(300, y, 120, 25);
        lblDeposit = new JLabel("0 VND"); lblDeposit.setBounds(420, y, 150, 25); panel.add(lblDeposit); y += 50;

        btnSubmit = new JButton("Submit"); btnSubmit.setBounds(200, y, 100, 30); btnSubmit.addActionListener(this); panel.add(btnSubmit);
        btnMainPage = new JButton("Back to Main Page"); btnMainPage.setBounds(320, y, 150, 30); btnMainPage.addActionListener(this); panel.add(btnMainPage);
        btnBack = new JButton("Back"); btnBack.setBounds(490, y, 100, 30); btnBack.addActionListener(this); panel.add(btnBack);

        add(panel);
        actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Init"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Init")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            // 1. Populate Unreturned Table
            ArrayList<BorrowedBook> remaining = new ArrayList<>();
            if (borrowingReceipt != null) {
                for (BorrowedBook bb : borrowingReceipt.getListBorrowedBook()) {
                    boolean scanned = false;
                    for (ReturnedBook rb : returningReceipt.getListReturnedBook()) { if (rb.getBorrowedBook().getId() == bb.getId()) { scanned = true; break; } }
                    if (!scanned) remaining.add(bb);
                }
            }
            Object[][] dataU = new Object[remaining.size()][8];
            for (int i = 0; i < remaining.size(); i++) {
                BorrowedBook bb = remaining.get(i);
                dataU[i][0]=i+1; dataU[i][1]=bb.getBook().getCode(); dataU[i][2]=bb.getBook().getBarcode(); dataU[i][3]=bb.getBook().getName();
                dataU[i][4]=bb.getBook().getAuthor(); dataU[i][5]=sdf.format(bb.getBorrowDate()); dataU[i][6]=sdf.format(bb.getDueDate()); dataU[i][7]=bb.getPrice() + " VND";
            }
            tblUnreturned.setModel(new DefaultTableModel(dataU, new String[]{"Order", "Book Code", "Bar Code", "Name", "Author", "Borrowing Date", "Due Date", "Cover Price"}));

            // 2. Calculations & Fined Tables
            float totalFine = 0, totalDeposit = 0;
            ArrayList<Object[]> lateData = new ArrayList<>(), damagedData = new ArrayList<>();
            for (ReturnedBook rb : returningReceipt.getListReturnedBook()) {
                totalDeposit += rb.getBorrowedBook().getPrice();
                if (rb.getReturnDate().after(rb.getBorrowedBook().getDueDate())) {
                    float lf = rb.getBorrowedBook().getPrice() * 0.2f;
                    lateData.add(new Object[]{lateData.size()+1, rb.getBorrowedBook().getBook().getCode(), rb.getBorrowedBook().getBook().getBarcode(), rb.getBorrowedBook().getBook().getName(), rb.getBorrowedBook().getBook().getAuthor(), sdf.format(rb.getBorrowedBook().getBorrowDate()), sdf.format(rb.getBorrowedBook().getDueDate()), sdf.format(rb.getReturnDate()), rb.getBorrowedBook().getPrice(), lf});
                    totalFine += lf;
                }
            }
            for (BookDamage bd : listBookDamage) {
                ReturnedBook rb = bd.getReturnedBook();
                boolean exists = false;
                for(Object[] row : damagedData) {
                    if(row[1].equals(rb.getBorrowedBook().getBook().getCode())) {
                        row[6] = row[6].toString() + bd.getDamage().getName() + "; "; row[7] = (float)row[7] + bd.getFineAmount(); exists = true; break;
                    }
                }
                if(!exists) damagedData.add(new Object[]{damagedData.size()+1, rb.getBorrowedBook().getBook().getCode(), rb.getBorrowedBook().getBook().getBarcode(), rb.getBorrowedBook().getBook().getName(), rb.getBorrowedBook().getBook().getAuthor(), rb.getBorrowedBook().getPrice(), bd.getDamage().getName() + "; ", bd.getFineAmount()});
                totalFine += bd.getFineAmount();
            }
            tblLate.setModel(new DefaultTableModel(lateData.toArray(new Object[0][0]), new String[]{"Order", "Book Code", "Bar Code", "Name", "Author", "Borrowing Date", "Due Date", "Returning Date", "Cover Price", "Fine Amount (20%)"}));
            tblDamaged.setModel(new DefaultTableModel(damagedData.toArray(new Object[0][0]), new String[]{"Order", "Book Code", "Bar Code", "Name", "Author", "Cover Price", "Damaged Status Now", "Fine Amount"}));
            lblTotalFine.setText(totalFine + " VND"); lblDeposit.setText(totalDeposit + " VND");

        } else if (e.getSource() == btnSubmit) {
            if (new ReturningReceiptDAO().updateReturningReceipt(returningReceipt, listBookDamage)) {
                JOptionPane.showMessageDialog(this, "Processed successfully!");
                new LibrarianHomeFrm(user).setVisible(true); dispose();
            }
        } else if (e.getSource() == btnBack) {
            new BookReturnFrm(borrowingReceipt, returningReceipt, listBookDamage).setVisible(true); dispose();
        } else if (e.getSource() == btnMainPage) {
            new LibrarianHomeFrm(user).setVisible(true); dispose();
        }
    }
}
