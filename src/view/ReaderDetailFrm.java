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

        panel.add(new JLabel("Reader Information")).setBounds(20, 45, 150, 25);
        String[] readerColumns = {"Reader ID", "Name", "DoB", "Address", "Phone number", "Bar Code"};
        Object[][] readerData = {{reader.getId(), reader.getName(), reader.getDateOfBirth(), reader.getAddress(), reader.getPhoneNumber(), reader.getBarcode()}};
        panel.add(new JScrollPane(new JTable(new DefaultTableModel(readerData, readerColumns)))).setBounds(20, 75, 850, 50);

        panel.add(new JLabel("List of borrowed books that have not been returned")).setBounds(20, 135, 350, 25);
        tblBorrowed = new JTable(); panel.add(new JScrollPane(tblBorrowed)).setBounds(20, 165, 850, 80);

        panel.add(new JLabel("List of borrowed books that have been returned")).setBounds(20, 300, 350, 25);
        tblReturned = new JTable(); panel.add(new JScrollPane(tblReturned)).setBounds(20, 330, 850, 80);

        btnScanBook = new JButton("Scan book"); btnScanBook.setBounds(20, 450, 100, 30); btnScanBook.addActionListener(this); panel.add(btnScanBook);
        btnBack = new JButton("Back"); btnBack.setBounds(20, 500, 80, 30);
        btnBack.addActionListener(e -> { new SearchReaderFrm(user).setVisible(true); dispose(); }); panel.add(btnBack);
        btnNext = new JButton("Next"); btnNext.setBounds(120, 500, 80, 30); btnNext.addActionListener(this); panel.add(btnNext);

        add(panel);

        // Data loading and table update merged here
        this.borrowingReceipt = new BorrowingReceiptDAO().getBorrowedBook(reader);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] colsB = {"Order", "Book Code", "Bar Code", "Name", "Author", "Borrowing Date", "Due Date", "Cover Price"};
        if (borrowingReceipt != null && !borrowingReceipt.getListBorrowedBook().isEmpty()) {
            Object[][] dataB = new Object[borrowingReceipt.getListBorrowedBook().size()][8];
            for (int i = 0; i < borrowingReceipt.getListBorrowedBook().size(); i++) {
                BorrowedBook bb = borrowingReceipt.getListBorrowedBook().get(i);
                dataB[i][0] = i + 1; dataB[i][1] = bb.getBook().getCode(); dataB[i][2] = bb.getBook().getBarcode(); dataB[i][3] = bb.getBook().getName();
                dataB[i][4] = bb.getBook().getAuthor(); dataB[i][5] = sdf.format(bb.getBorrowDate()); dataB[i][6] = sdf.format(bb.getDueDate()); dataB[i][7] = bb.getPrice();
            }
            tblBorrowed.setModel(new DefaultTableModel(dataB, colsB));
        } else { tblBorrowed.setModel(new DefaultTableModel(null, colsB)); }

        ReturningReceiptDAO rrd = new ReturningReceiptDAO();
        ArrayList<ReturningReceipt> listReturningReceipt = rrd.getReturnedBook(reader);
        String[] colsR = {"Order", "Book Code", "Bar Code", "Name", "Author", "Cover Price", "Due Date", "Returning Date", "Fine"};
        int totalRows = 0; for (ReturningReceipt rr : listReturningReceipt) totalRows += rr.getListReturnedBook().size();
        Object[][] dataR = new Object[totalRows][9];
        int k = 0;
        for (ReturningReceipt rr : listReturningReceipt) {
            String[] fines = {};
            if (rr.getNote().contains("| Fine:")) {
                fines = rr.getNote().split("\\| Fine:")[1].split(";");
            }
            
            for (int i = 0; i < rr.getListReturnedBook().size(); i++) {
                ReturnedBook rb = rr.getListReturnedBook().get(i);
                dataR[k][0] = k + 1; dataR[k][1] = rb.getBorrowedBook().getBook().getCode(); dataR[k][2] = rb.getBorrowedBook().getBook().getBarcode();
                dataR[k][3] = rb.getBorrowedBook().getBook().getName(); dataR[k][4] = rb.getBorrowedBook().getBook().getAuthor(); dataR[k][5] = rb.getBorrowedBook().getPrice() + " VND";
                dataR[k][6] = sdf.format(rb.getBorrowedBook().getDueDate()); dataR[k][7] = sdf.format(rb.getReturnDate());
                String fineVal = (i < fines.length) ? fines[i] : "0";
                dataR[k][8] = fineVal + " VND"; k++;
            }
        }
        tblReturned.setModel(new DefaultTableModel(dataR, colsR) { @Override public boolean isCellEditable(int r, int c) { return false; } });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnScanBook) {
            if (borrowingReceipt != null && !borrowingReceipt.getListBorrowedBook().isEmpty()) {
                ReturningReceipt rr = new ReturningReceipt(); rr.setReader(reader); rr.setUser(user); rr.setCreatedDate(new Date()); rr.setBarcode("RR" + System.currentTimeMillis());
                ReturnedBook rb = new ReturnedBook(); rb.setBorrowedBook(borrowingReceipt.getListBorrowedBook().get(0)); rb.setReturnDate(new Date());
                rr.getListReturnedBook().add(rb);
                (new BookReturnFrm(borrowingReceipt, rr)).setVisible(true); dispose();
            } else { JOptionPane.showMessageDialog(this, "No books to scan!"); }
        } else if (e.getSource() == btnNext) {
            ReturningReceipt rr = new ReturningReceipt(); rr.setReader(reader); rr.setUser(user); rr.setCreatedDate(new Date()); rr.setBarcode("RR" + System.currentTimeMillis());
            (new BookReturnFrm(borrowingReceipt, rr)).setVisible(true); dispose();
        }
    }
}
