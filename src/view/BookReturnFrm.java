package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import dao.BookDAO;
import model.*;

public class BookReturnFrm extends JFrame implements ActionListener {
    private User user;
    private Reader reader;
    private BorrowingReceipt borrowingReceipt;
    private ReturningReceipt returningReceipt;
    private JTable tblBorrowed, tblScanned;
    private JTextField txtBookCode;
    private JButton btnScan, btnNext, btnBack;

    public BookReturnFrm(User user, Reader reader, BorrowingReceipt br) {
        super("Book Return");
        this.user = user;
        this.reader = reader;
        this.borrowingReceipt = br;
        this.returningReceipt = new ReturningReceipt();
        returningReceipt.setReader(reader);
        returningReceipt.setUser(user);
        returningReceipt.setCreatedDate(new Date());
        returningReceipt.setBarcode("RR" + System.currentTimeMillis());

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

        btnBack = new JButton("Back");
        btnBack.setBounds(20, 10, 80, 25);
        btnBack.addActionListener(e -> {
            new ReaderDetailFrm(user, reader).setVisible(true);
            dispose();
        });
        panel.add(btnBack);

        JButton scanBookBtn = new JButton("Scan Book");
        scanBookBtn.setBounds(110, 10, 100, 25);
        panel.add(scanBookBtn); // Design only

        btnNext = new JButton("Next");
        btnNext.setBounds(220, 10, 80, 25);
        btnNext.addActionListener(this);
        panel.add(btnNext);

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

        JTable tblReturnedHistory = new JTable(); // History table from design
        JScrollPane returnedScroll = new JScrollPane(tblReturnedHistory);
        returnedScroll.setBounds(20, 435, 950, 80);
        panel.add(returnedScroll);

        btnScan = new JButton("Scan book");
        btnScan.setBounds(20, 560, 100, 30);
        btnScan.addActionListener(this);
        panel.add(btnScan);

        txtBookCode = new JTextField(); // Added for scanning functionality
        txtBookCode.setBounds(130, 560, 150, 30);
        panel.add(txtBookCode);

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

        updateBorrowedTable();
        updateScannedTable();
    }

    private void updateBorrowedTable() {
        String[] columns = {"Order", "Book Code", "Bar Code", "Name", "Due Date"};
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

        Object[][] data = new Object[remaining.size()][5];
        for (int i = 0; i < remaining.size(); i++) {
            BorrowedBook bb = remaining.get(i);
            data[i][0] = i + 1;
            data[i][1] = bb.getBook().getCode();
            data[i][2] = bb.getBook().getBarcode();
            data[i][3] = bb.getBook().getName();
            data[i][4] = bb.getDueDate();
        }
        tblBorrowed.setModel(new DefaultTableModel(data, columns));
    }

    private void updateScannedTable() {
        String[] columns = {"Order", "Book Code", "Name", "Due Date", "Return Date", "Damage Status", "Action"};
        Object[][] data = new Object[returningReceipt.getListReturnedBook().size()][7];
        for (int i = 0; i < returningReceipt.getListReturnedBook().size(); i++) {
            ReturnedBook rb = returningReceipt.getListReturnedBook().get(i);
            data[i][0] = i + 1;
            data[i][1] = rb.getBorrowedBook().getBook().getCode();
            data[i][2] = rb.getBorrowedBook().getBook().getName();
            data[i][3] = rb.getBorrowedBook().getDueDate();
            data[i][4] = rb.getReturnDate();
            data[i][5] = rb.getListBookDamage().isEmpty() ? "OK" : rb.getListBookDamage().size() + " damage(s)";
            data[i][6] = "Edit Damage";
        }
        tblScanned.setModel(new DefaultTableModel(data, columns));
        
        // Add button to Damage Status/Action column
        tblScanned.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        tblScanned.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox(), this));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnScan) {
            String code = txtBookCode.getText().trim();
            if (code.isEmpty()) return;
            
            BorrowedBook target = null;
            for (BorrowedBook bb : borrowingReceipt.getListBorrowedBook()) {
                if (bb.getBook().getCode().equals(code) || bb.getBook().getBarcode().equals(code)) {
                    target = bb;
                    break;
                }
            }
            
            if (target != null) {
                // Check if already scanned
                for (ReturnedBook rb : returningReceipt.getListReturnedBook()) {
                    if (rb.getBorrowedBook().getId() == target.getId()) {
                        JOptionPane.showMessageDialog(this, "Book already scanned!");
                        return;
                    }
                }
                
                ReturnedBook rb = new ReturnedBook();
                rb.setBorrowedBook(target);
                rb.setReturnDate(new Date());
                returningReceipt.getListReturnedBook().add(rb);
                updateBorrowedTable();
                updateScannedTable();
                txtBookCode.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "This book is not in the reader's borrowed list!");
            }
        } else if (e.getSource() == btnNext) {
            if (returningReceipt.getListReturnedBook().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No books scanned!");
                return;
            }
            (new ReturningReceiptFrm(user, reader, returningReceipt)).setVisible(true);
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
