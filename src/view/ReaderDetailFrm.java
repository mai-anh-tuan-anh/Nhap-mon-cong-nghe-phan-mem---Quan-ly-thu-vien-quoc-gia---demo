package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.BorrowingReceiptDAO;
import dao.ReturningReceiptDAO;
import model.*;

public class ReaderDetailFrm extends JFrame implements ActionListener {
    private User user;
    private Reader reader;
    private BorrowingReceipt borrowingReceipt;
    private ArrayList<ReturningReceipt> listReturningReceipt;
    private JTable tblBorrowed, tblReturned;
    private JButton btnScanBook, btnNext, btnBack;

    public ReaderDetailFrm(User user, Reader reader) {
        super("Reader Detail");
        this.user = user;
        this.reader = reader;
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 240));

        JLabel title = new JLabel("READER DETAIL");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(350, 10, 200, 30);
        panel.add(title);

        JLabel readerInfoLabel = new JLabel("Reader Information");
        readerInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        readerInfoLabel.setBounds(20, 45, 150, 25);
        panel.add(readerInfoLabel);

        String[] readerColumns = {"Reader ID", "Name", "DoB", "Address", "Phone number", "Bar Code"};
        Object[][] readerData = {
            {reader.getId(), reader.getName(), reader.getDateOfBirth(), reader.getAddress(), reader.getPhoneNumber(), reader.getBarcode()}
        };
        JTable readerTable = new JTable(new DefaultTableModel(readerData, readerColumns));
        JScrollPane readerScroll = new JScrollPane(readerTable);
        readerScroll.setBounds(20, 75, 850, 50);
        panel.add(readerScroll);

        JLabel unreturnedLabel = new JLabel("List of borrowed books that have not been returned");
        unreturnedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        unreturnedLabel.setBounds(20, 135, 350, 25);
        panel.add(unreturnedLabel);

        tblBorrowed = new JTable();
        JScrollPane unreturnedScroll = new JScrollPane(tblBorrowed);
        unreturnedScroll.setBounds(20, 165, 850, 80);
        panel.add(unreturnedScroll);

        JLabel returnedLabel = new JLabel("List of borrowed books that have been returned");
        returnedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        returnedLabel.setBounds(20, 300, 350, 25);
        panel.add(returnedLabel);

        tblReturned = new JTable();
        JScrollPane returnedScroll = new JScrollPane(tblReturned);
        returnedScroll.setBounds(20, 330, 850, 80);
        panel.add(returnedScroll);

        btnScanBook = new JButton("Scan book");
        btnScanBook.setBounds(20, 450, 100, 30);
        btnScanBook.addActionListener(this);
        panel.add(btnScanBook);

        btnBack = new JButton("Back");
        btnBack.setBounds(20, 500, 80, 30);
        btnBack.addActionListener(e -> {
            new SearchReaderFrm(user).setVisible(true);
            dispose();
        });
        panel.add(btnBack);

        btnNext = new JButton("Next");
        btnNext.setBounds(120, 500, 80, 30);
        btnNext.addActionListener(this);
        panel.add(btnNext);

        add(panel);
        loadData();
    }

    private void loadData() {
        BorrowingReceiptDAO brd = new BorrowingReceiptDAO();
        borrowingReceipt = brd.getBorrowedBook(reader);
        updateBorrowedTable();

        ReturningReceiptDAO rrd = new ReturningReceiptDAO();
        listReturningReceipt = rrd.getReturnedBook(reader);
        updateReturnedTable();
    }

    private void updateBorrowedTable() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] columns = {"Order", "Book Code", "Bar Code", "Name", "Author", "Borrowing Date", "Due Date", "Cover Price"};
        if (borrowingReceipt == null || borrowingReceipt.getListBorrowedBook().isEmpty()) {
            tblBorrowed.setModel(new DefaultTableModel(null, columns));
            return;
        }
        Object[][] data = new Object[borrowingReceipt.getListBorrowedBook().size()][8];
        for (int i = 0; i < borrowingReceipt.getListBorrowedBook().size(); i++) {
            BorrowedBook bb = borrowingReceipt.getListBorrowedBook().get(i);
            data[i][0] = i + 1;
            data[i][1] = bb.getBook().getCode();
            data[i][2] = bb.getBook().getBarcode();
            data[i][3] = bb.getBook().getName();
            data[i][4] = bb.getBook().getAuthor();
            data[i][5] = sdf.format(bb.getBorrowDate());
            data[i][6] = sdf.format(bb.getDueDate());
            data[i][7] = bb.getPrice();
        }
        tblBorrowed.setModel(new DefaultTableModel(data, columns));
    }

    private void updateReturnedTable() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] columns = {"Order", "Book Code", "Bar Code", "Name", "Author", "Cover Price", "Due Date", "Returning Date", "Fine"};
        int totalRows = 0;
        for (ReturningReceipt rr : listReturningReceipt) totalRows += rr.getListReturnedBook().size();
        
        Object[][] data = new Object[totalRows][9];
        int k = 0;
        for (ReturningReceipt rr : listReturningReceipt) {
            for (ReturnedBook rb : rr.getListReturnedBook()) {
                data[k][0] = k + 1;
                data[k][1] = rb.getBorrowedBook().getBook().getCode();
                data[k][2] = rb.getBorrowedBook().getBook().getBarcode();
                data[k][3] = rb.getBorrowedBook().getBook().getName();
                data[k][4] = rb.getBorrowedBook().getBook().getAuthor();
                data[k][5] = rb.getBorrowedBook().getPrice() + " VND";
                data[k][6] = sdf.format(rb.getBorrowedBook().getDueDate());
                data[k][7] = sdf.format(rb.getReturnDate());
                
                float fine = 0;
                if (rb.getReturnDate().after(rb.getBorrowedBook().getDueDate())) {
                    fine += rb.getBorrowedBook().getPrice() * 0.2f;
                }
                for (BookDamage bd : rb.getListBookDamage()) {
                    fine += bd.getFineAmount();
                }
                data[k][8] = fine + " VND";
                k++;
            }
        }
        tblReturned.setModel(new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnScanBook) {
            if (borrowingReceipt != null && !borrowingReceipt.getListBorrowedBook().isEmpty()) {
                ReturningReceipt demoRR = new ReturningReceipt();
                demoRR.setReader(reader);
                demoRR.setUser(user);
                demoRR.setCreatedDate(new Date());
                demoRR.setBarcode("RR" + System.currentTimeMillis());
                
                BorrowedBook firstBook = borrowingReceipt.getListBorrowedBook().get(0);
                ReturnedBook rb = new ReturnedBook();
                rb.setBorrowedBook(firstBook);
                rb.setReturnDate(new Date());
                demoRR.getListReturnedBook().add(rb);
                
                (new BookReturnFrm(borrowingReceipt, demoRR)).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No books to scan!");
            }
        } else if (e.getSource() == btnNext) {
            ReturningReceipt rr = new ReturningReceipt();
            rr.setReader(reader);
            rr.setUser(user);
            rr.setCreatedDate(new Date());
            rr.setBarcode("RR" + System.currentTimeMillis());
            (new BookReturnFrm(borrowingReceipt, rr)).setVisible(true);
            dispose();
        }
    }
}
