package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import dao.ReturningReceiptDAO;
import model.*;

public class BookReturnFrm extends JFrame implements ActionListener {
    private User user;
    private Reader reader;
    BorrowingReceipt borrowingReceipt;
    ReturningReceipt returningReceipt;
    ArrayList<BookDamage> listBookDamage;
    JTable tblScanned;
    private JTable tblBorrowed, tblReturnedHistory;
    private JButton btnScan, btnNext, btnBack;

    public BookReturnFrm(BorrowingReceipt br, ReturningReceipt rr, ArrayList<BookDamage> listBD) {
        super("Book Return");
        this.borrowingReceipt = br;
        this.returningReceipt = rr;
        this.listBookDamage = listBD;
        this.user = rr.getUser();
        this.reader = rr.getReader();

        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 240));

        JLabel title = new JLabel("BOOK RETURN");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(400, 10, 200, 30);
        panel.add(title);

        panel.add(new JLabel("Reader Information")).setBounds(20, 45, 150, 25);
        SimpleDateFormat sdfC = new SimpleDateFormat("yyyy/MM/dd");
        String[] readerColumns = {"Reader ID", "Name", "DoB", "Address", "Phone number", "Bar Code"};
        Object[][] readerData = {{reader.getId(), reader.getName(), sdfC.format(reader.getDateOfBirth()), reader.getAddress(), reader.getPhoneNumber(), reader.getBarcode()}};
        panel.add(new JScrollPane(new JTable(new DefaultTableModel(readerData, readerColumns)))).setBounds(20, 75, 950, 50);

        panel.add(new JLabel("List of borrowed books that have not been returned")).setBounds(20, 135, 350, 25);
        tblBorrowed = new JTable(); panel.add(new JScrollPane(tblBorrowed)).setBounds(20, 165, 950, 60);

        panel.add(new JLabel("List of scanned books")).setBounds(20, 280, 350, 25);
        tblScanned = new JTable(); panel.add(new JScrollPane(tblScanned)).setBounds(20, 310, 950, 60);

        panel.add(new JLabel("List of borrowed books that have been returned")).setBounds(20, 405, 350, 25);
        tblReturnedHistory = new JTable(); panel.add(new JScrollPane(tblReturnedHistory)).setBounds(20, 435, 950, 80);

        btnScan = new JButton("Scan book"); btnScan.setBounds(20, 560, 100, 30); btnScan.addActionListener(this); panel.add(btnScan);
        btnBack = new JButton("Back"); btnBack.setBounds(20, 610, 80, 30); btnBack.addActionListener(e -> { new ReaderDetailFrm(user, reader).setVisible(true); dispose(); }); panel.add(btnBack);
        btnNext = new JButton("Next"); btnNext.setBounds(120, 610, 80, 30); btnNext.addActionListener(this); panel.add(btnNext);

        add(panel);
        
        // Trigger initial data load
        actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Init"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Init")) {
            // 1. Load History
            ReturningReceiptDAO rrd = new ReturningReceiptDAO();
            ArrayList<ReturningReceipt> listReturningHistory = rrd.getReturnedBook(reader);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            int totalRows = 0; for (ReturningReceipt rrt : listReturningHistory) totalRows += rrt.getListReturnedBook().size();
            Object[][] dataHistory = new Object[totalRows][9];
            int k = 0;
            for (ReturningReceipt rrt : listReturningHistory) {
                String[] fines = (rrt.getNote() != null && rrt.getNote().contains("| Fine:")) ? rrt.getNote().split("\\| Fine:")[1].split(";") : new String[0];
                for (int i = 0; i < rrt.getListReturnedBook().size(); i++) {
                    ReturnedBook rb = rrt.getListReturnedBook().get(i);
                    dataHistory[k][0]=k+1; dataHistory[k][1]=rb.getBorrowedBook().getBook().getCode(); dataHistory[k][2]=rb.getBorrowedBook().getBook().getBarcode();
                    dataHistory[k][3]=rb.getBorrowedBook().getBook().getName(); dataHistory[k][4]=rb.getBorrowedBook().getBook().getAuthor();
                    dataHistory[k][5]=rb.getBorrowedBook().getPrice() + " VND"; dataHistory[k][6]=sdf.format(rb.getBorrowedBook().getDueDate());
                    dataHistory[k][7]=sdf.format(rb.getReturnDate()); dataHistory[k][8]=(i < fines.length ? fines[i] : "0") + " VND"; k++;
                }
            }
            tblReturnedHistory.setModel(new DefaultTableModel(dataHistory, new String[]{"Order", "Book Code", "Bar Code", "Name", "Author", "Cover Price", "Due Date", "Returning Date", "Fine Amount"}));

            // 2. Update Borrowed Table
            if (borrowingReceipt != null) {
                ArrayList<BorrowedBook> remaining = new ArrayList<>();
                for (BorrowedBook bb : borrowingReceipt.getListBorrowedBook()) {
                    boolean scanned = false;
                    for (ReturnedBook rb : returningReceipt.getListReturnedBook()) { if (rb.getBorrowedBook().getId() == bb.getId()) { scanned = true; break; } }
                    if (!scanned) remaining.add(bb);
                }
                Object[][] dataB = new Object[remaining.size()][8];
                for (int i = 0; i < remaining.size(); i++) {
                    BorrowedBook bb = remaining.get(i);
                    dataB[i][0]=i+1; dataB[i][1]=bb.getBook().getCode(); dataB[i][2]=bb.getBook().getBarcode(); dataB[i][3]=bb.getBook().getName();
                    dataB[i][4]=bb.getBook().getAuthor(); dataB[i][5]=sdf.format(bb.getBorrowDate()); dataB[i][6]=sdf.format(bb.getDueDate()); dataB[i][7]=bb.getPrice() + " VND";
                }
                tblBorrowed.setModel(new DefaultTableModel(dataB, new String[]{"Order", "Book Code", "Bar Code", "Name", "Author", "Borrowing Date", "Due Date", "Cover Price"}));
            }

            // 3. Update Scanned Table
            Object[][] dataS = new Object[returningReceipt.getListReturnedBook().size()][9];
            for (int i = 0; i < returningReceipt.getListReturnedBook().size(); i++) {
                ReturnedBook rb = returningReceipt.getListReturnedBook().get(i);
                dataS[i][0]=i+1; dataS[i][1]=rb.getBorrowedBook().getBook().getCode(); dataS[i][2]=rb.getBorrowedBook().getBook().getBarcode();
                dataS[i][3]=rb.getBorrowedBook().getBook().getName(); dataS[i][4]=rb.getBorrowedBook().getBook().getAuthor();
                dataS[i][5]=sdf.format(rb.getBorrowedBook().getDueDate()); dataS[i][6]=sdf.format(rb.getReturnDate()); dataS[i][7]=rb.getBorrowedBook().getPrice() + " VND";
                int dc = 0; for (BookDamage bd : listBookDamage) { if (bd.getReturnedBook().getId() == rb.getId()) dc++; }
                dataS[i][8] = dc == 0 ? "OK" : dc + " damage(s)";
            }
            tblScanned.setModel(new DefaultTableModel(dataS, new String[]{"Order", "Book Code", "Bar Code", "Name", "Author", "Due Date", "Returning Date", "Cover Price", "Damage Status Now"}) { @Override public boolean isCellEditable(int r, int c) { return c == 8; } });
            tblScanned.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
            tblScanned.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox(), this));

        } else if (e.getSource() == btnScan) {
            BorrowedBook target = null;
            if (borrowingReceipt != null) {
                for (BorrowedBook bb : borrowingReceipt.getListBorrowedBook()) {
                    boolean scanned = false;
                    for (ReturnedBook rb : returningReceipt.getListReturnedBook()) { if (rb.getBorrowedBook().getId() == bb.getId()) { scanned = true; break; } }
                    if (!scanned) { target = bb; break; }
                }
            }
            if (target != null) {
                ReturnedBook rb = new ReturnedBook(); rb.setBorrowedBook(target); rb.setReturnDate(new Date());
                returningReceipt.getListReturnedBook().add(rb);
                actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Init"));
            } else { JOptionPane.showMessageDialog(this, "All books scanned!"); }
        } else if (e.getSource() == btnNext) {
            if (returningReceipt.getListReturnedBook().isEmpty()) { JOptionPane.showMessageDialog(this, "No books scanned!"); return; }
            (new ReturningReceiptFrm(borrowingReceipt, returningReceipt, listBookDamage)).setVisible(true); dispose();
        }
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() { setOpaque(true); }
    @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean isS, boolean hasF, int r, int c) { setText(v != null ? v.toString() : ""); return this; }
}

class ButtonEditor extends DefaultCellEditor {
    private JButton button; private String label; private boolean pushed; private BookReturnFrm parent; private int row;
    public ButtonEditor(JCheckBox checkBox, BookReturnFrm parent) { super(checkBox); this.parent = parent; button = new JButton(); button.setOpaque(true); button.addActionListener(e -> fireEditingStopped()); }
    @Override public Component getTableCellEditorComponent(JTable t, Object v, boolean isS, int r, int c) { label = v != null ? v.toString() : ""; button.setText(label); this.row = r; pushed = true; return button; }
    @Override public Object getCellEditorValue() {
        if (pushed) { (new BookDamageFrm(parent.borrowingReceipt, parent.returningReceipt, parent.returningReceipt.getListReturnedBook().get(row), parent.listBookDamage)).setVisible(true); parent.dispose(); }
        pushed = false; return label;
    }
}
