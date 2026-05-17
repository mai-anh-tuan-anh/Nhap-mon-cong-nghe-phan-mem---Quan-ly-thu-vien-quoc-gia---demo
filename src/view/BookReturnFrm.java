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
    private BorrowingReceipt borrowingReceipt;
    private ReturningReceipt returningReceipt;
    private ArrayList<ReturningReceipt> listReturningHistory;
    private JTable tblBorrowed, tblScanned, tblReturnedHistory;
    private JButton btnScan, btnNext, btnBack;

    public BookReturnFrm(BorrowingReceipt br, ReturningReceipt rr) {
        super("Book Return");
        this.returningReceipt = rr;
        this.user = rr.getUser();
        this.reader = rr.getReader();
        this.borrowingReceipt = br;

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

        JLabel readerInfoLabel = new JLabel("Reader Information");
        readerInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        readerInfoLabel.setBounds(20, 45, 150, 25);
        panel.add(readerInfoLabel);

        String[] readerColumns = {"Reader ID", "Name", "DoB", "Address", "Phone number", "Bar Code"};
        Object[][] readerData = {{reader.getId(), reader.getName(), reader.getDateOfBirth(), reader.getAddress(), reader.getPhoneNumber(), reader.getBarcode()}};
        JTable readerTable = new JTable(new DefaultTableModel(readerData, readerColumns));
        JScrollPane readerScroll = new JScrollPane(readerTable);
        readerScroll.setBounds(20, 75, 950, 50);
        panel.add(readerScroll);

        JLabel unreturnedLabel = new JLabel("List of borrowed books that have not been returned");
        unreturnedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        unreturnedLabel.setBounds(20, 135, 350, 25);
        panel.add(unreturnedLabel);

        tblBorrowed = new JTable();
        JScrollPane unreturnedScroll = new JScrollPane(tblBorrowed);
        unreturnedScroll.setBounds(20, 165, 950, 60);
        panel.add(unreturnedScroll);

        JLabel scannedLabel = new JLabel("List of scanned books");
        scannedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        scannedLabel.setBounds(20, 280, 350, 25);
        panel.add(scannedLabel);

        tblScanned = new JTable();
        JScrollPane scannedScroll = new JScrollPane(tblScanned);
        scannedScroll.setBounds(20, 310, 950, 60);
        panel.add(scannedScroll);

        JLabel returnedLabel = new JLabel("List of borrowed books that have been returned");
        returnedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        returnedLabel.setBounds(20, 405, 350, 25);
        panel.add(returnedLabel);

        tblReturnedHistory = new JTable();
        JScrollPane returnedScroll = new JScrollPane(tblReturnedHistory);
        returnedScroll.setBounds(20, 435, 950, 80);
        panel.add(returnedScroll);

        btnScan = new JButton("Scan book");
        btnScan.setBounds(20, 560, 100, 30);
        btnScan.addActionListener(this);
        panel.add(btnScan);

        btnBack = new JButton("Back");
        btnBack.setBounds(20, 610, 80, 30);
        btnBack.addActionListener(e -> {
            new ReaderDetailFrm(user, reader).setVisible(true);
            dispose();
        });
        panel.add(btnBack);

        btnNext = new JButton("Next");
        btnNext.setBounds(120, 610, 80, 30);
        btnNext.addActionListener(this);
        panel.add(btnNext);

        add(panel);

        loadHistory();
        updateBorrowedTable();
        updateScannedTable();
    }

    private void loadHistory() {
        ReturningReceiptDAO rrd = new ReturningReceiptDAO();
        listReturningHistory = rrd.getReturnedBook(reader);
        updateHistoryTable();
    }

    private void updateHistoryTable() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] columns = {"Order", "Book Code", "Bar Code", "Name", "Author", "Cover Price", "Due Date", "Returning Date", "Fine Amount"};
        int totalRows = 0;
        for (ReturningReceipt rr : listReturningHistory) totalRows += rr.getListReturnedBook().size();
        
        Object[][] data = new Object[totalRows][9];
        int k = 0;
        for (ReturningReceipt rr : listReturningHistory) {
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
                for (BookDamage bd : rb.getListBookDamage()) fine += bd.getFineAmount();
                data[k][8] = fine + " VND";
                k++;
            }
        }
        tblReturnedHistory.setModel(new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });
    }

    private void updateBorrowedTable() {
        String[] columns = {"Order", "Book Code", "Bar Code", "Name", "Author", "Borrowing Date", "Due Date", "Cover Price"};
        if (borrowingReceipt == null) return;
        ArrayList<BorrowedBook> remaining = new ArrayList<>();
        for (BorrowedBook bb : borrowingReceipt.getListBorrowedBook()) {
            boolean alreadyScanned = false;
            for (ReturnedBook rb : returningReceipt.getListReturnedBook()) {
                if (rb.getBorrowedBook().getId() == bb.getId()) {
                    alreadyScanned = true;
                    break;
                }
            }
            if (!alreadyScanned) remaining.add(bb);
        }

        Object[][] data = new Object[remaining.size()][8];
        for (int i = 0; i < remaining.size(); i++) {
            BorrowedBook bb = remaining.get(i);
            data[i][0] = i + 1;
            data[i][1] = bb.getBook().getCode();
            data[i][2] = bb.getBook().getBarcode();
            data[i][3] = bb.getBook().getName();
            data[i][4] = bb.getBook().getAuthor();
            data[i][5] = bb.getBorrowDate();
            data[i][6] = bb.getDueDate();
            data[i][7] = bb.getPrice() + " VND";
        }
        tblBorrowed.setModel(new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });
    }

    private void updateScannedTable() {
        String[] columns = {"Order", "Book Code", "Bar Code", "Name", "Author", "Due Date", "Returning Date", "Cover Price", "Damage Status Now"};
        Object[][] data = new Object[returningReceipt.getListReturnedBook().size()][9];
        for (int i = 0; i < returningReceipt.getListReturnedBook().size(); i++) {
            ReturnedBook rb = returningReceipt.getListReturnedBook().get(i);
            data[i][0] = i + 1;
            data[i][1] = rb.getBorrowedBook().getBook().getCode();
            data[i][2] = rb.getBorrowedBook().getBook().getBarcode();
            data[i][3] = rb.getBorrowedBook().getBook().getName();
            data[i][4] = rb.getBorrowedBook().getBook().getAuthor();
            data[i][5] = rb.getBorrowedBook().getDueDate();
            data[i][6] = rb.getReturnDate();
            data[i][7] = rb.getBorrowedBook().getPrice() + " VND";
            data[i][8] = rb.getListBookDamage().isEmpty() ? "OK" : rb.getListBookDamage().size() + " damage(s)";
        }
        tblScanned.setModel(new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return column == 8; }
        });
        
        tblScanned.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
        tblScanned.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox(), this));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnScan) {
            BorrowedBook target = null;
            if (borrowingReceipt != null) {
                for (BorrowedBook bb : borrowingReceipt.getListBorrowedBook()) {
                    boolean alreadyScanned = false;
                    for (ReturnedBook rb : returningReceipt.getListReturnedBook()) {
                        if (rb.getBorrowedBook().getId() == bb.getId()) {
                            alreadyScanned = true;
                            break;
                        }
                    }
                    if (!alreadyScanned) {
                        target = bb;
                        break;
                    }
                }
            }
            
            if (target != null) {
                ReturnedBook rb = new ReturnedBook();
                rb.setBorrowedBook(target);
                rb.setReturnDate(new Date());
                returningReceipt.getListReturnedBook().add(rb);
                updateBorrowedTable();
                updateScannedTable();
            } else {
                JOptionPane.showMessageDialog(this, "All books have been scanned!");
            }
        } else if (e.getSource() == btnNext) {
            if (returningReceipt.getListReturnedBook().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No books scanned!");
                return;
            }
            (new ReturningReceiptFrm(returningReceipt)).setVisible(true);
            dispose();
        }
    }

    public void editDamage(int row) {
        ReturnedBook rb = returningReceipt.getListReturnedBook().get(row);
        (new BookDamageFrm(this, rb)).setVisible(true);
    }

    public void refreshScannedTable() {
        updateScannedTable();
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() { setOpaque(true); }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value != null ? value.toString() : "");
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean isPushed;
    private BookReturnFrm parent;
    private int row;

    public ButtonEditor(JCheckBox checkBox, BookReturnFrm parent) {
        super(checkBox);
        this.parent = parent;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = value != null ? value.toString() : "";
        button.setText(label);
        this.row = row;
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) parent.editDamage(row);
        isPushed = false;
        return label;
    }
}
