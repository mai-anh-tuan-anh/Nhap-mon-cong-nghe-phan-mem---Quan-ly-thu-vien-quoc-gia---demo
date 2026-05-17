package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.DamageDAO;
import model.*;

public class BookDamageFrm extends JDialog implements ActionListener {
    private ReturnedBook returnedBook;
    private BookReturnFrm parent;
    private ArrayList<Damage> listDamage;
    private JTable tblDamage;
    private JButton btnAdd, btnAccept, btnBack;

    public BookDamageFrm(BookReturnFrm parent, ReturnedBook rb) {
        super(parent, "Select Damage Status", true);
        this.parent = parent;
        this.returnedBook = rb;
        setSize(600, 500);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 240));

        JLabel title = new JLabel("BOOK DAMAGE SELECTION");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(180, 10, 250, 30);
        panel.add(title);

        int y = 50;
        panel.add(new JLabel("- Book Code: " + rb.getBorrowedBook().getBook().getCode())).setBounds(20, y, 300, 20); y += 25;
        panel.add(new JLabel("- BarCode: " + rb.getBorrowedBook().getBook().getBarcode())).setBounds(20, y, 300, 20); y += 25;
        panel.add(new JLabel("- Name: " + rb.getBorrowedBook().getBook().getName())).setBounds(20, y, 300, 20); y += 25;
        panel.add(new JLabel("- Author: " + rb.getBorrowedBook().getBook().getAuthor())).setBounds(20, y, 300, 20); y += 25;
        panel.add(new JLabel("- Borrowing Date: " + rb.getBorrowedBook().getBorrowDate())).setBounds(20, y, 300, 20); y += 25;
        panel.add(new JLabel("- Due Date: " + rb.getBorrowedBook().getDueDate())).setBounds(20, y, 300, 20); y += 25;
        panel.add(new JLabel("- Returning Date: " + rb.getReturnDate())).setBounds(20, y, 300, 20); y += 25;
        panel.add(new JLabel("- Cover Price: " + rb.getBorrowedBook().getPrice() + " VND")).setBounds(20, y, 300, 20); y += 25;
        panel.add(new JLabel("- Type of Damage:")).setBounds(20, y, 150, 20); y += 25;

        tblDamage = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblDamage);
        scrollPane.setBounds(20, y, 540, 110);
        panel.add(scrollPane);
        y += 130;

        btnAdd = new JButton("Add"); btnAdd.setBounds(20, y, 80, 25); panel.add(btnAdd); y += 40;
        btnBack = new JButton("Back"); btnBack.setBounds(150, y, 100, 30); btnBack.addActionListener(e -> dispose()); panel.add(btnBack);
        btnAccept = new JButton("Accept"); btnAccept.setBounds(300, y, 100, 30); btnAccept.addActionListener(this); panel.add(btnAccept);

        add(panel);

        // Load Damage logic merged here
        DamageDAO dd = new DamageDAO();
        listDamage = dd.chooseDamage();
        String[] columns = {"Order", "Type", "Fine Rate", "Select"};
        Object[][] data = new Object[listDamage.size()][4];
        for (int i = 0; i < listDamage.size(); i++) {
            Damage d = listDamage.get(i);
            data[i][0] = d.getId(); data[i][1] = d.getName(); data[i][2] = d.getFineRate();
            boolean selected = false;
            for (BookDamage bd : parent.listBookDamage) {
                if (bd.getReturnedBook() == rb && bd.getDamage().getId() == d.getId()) { selected = true; break; }
            }
            data[i][3] = selected;
        }
        tblDamage.setModel(new DefaultTableModel(data, columns) {
            @Override public Class<?> getColumnClass(int c) { return c == 3 ? Boolean.class : super.getColumnClass(c); }
            @Override public boolean isCellEditable(int r, int c) { return c == 3; }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAccept) {
            // Remove old damages for this book from parent's separate list
            ArrayList<BookDamage> toRemove = new ArrayList<>();
            for (BookDamage bd : parent.listBookDamage) {
                if (bd.getReturnedBook() == returnedBook) toRemove.add(bd);
            }
            parent.listBookDamage.removeAll(toRemove);

            // Add newly selected damages
            for (int i = 0; i < listDamage.size(); i++) {
                Boolean selected = (Boolean) tblDamage.getValueAt(i, 3);
                if (selected != null && selected) {
                    Damage d = listDamage.get(i);
                    BookDamage bd = new BookDamage();
                    bd.setDamage(d); bd.setDetectedDate(new Date());
                    bd.setFineAmount(returnedBook.getBorrowedBook().getPrice() * d.getFineRate() / 100);
                    bd.setNote(d.getName() + " Damage");
                    bd.setReturnedBook(returnedBook);
                    parent.listBookDamage.add(bd);
                }
            }
            
            // updateScannedTable inline for parent
            String[] columnsS = {"Order", "Book Code", "Bar Code", "Name", "Author", "Due Date", "Returning Date", "Cover Price", "Damage Status Now"};
            Object[][] dataS = new Object[parent.returningReceipt.getListReturnedBook().size()][9];
            for (int i = 0; i < parent.returningReceipt.getListReturnedBook().size(); i++) {
                ReturnedBook rb = parent.returningReceipt.getListReturnedBook().get(i);
                dataS[i][0] = i + 1; dataS[i][1] = rb.getBorrowedBook().getBook().getCode(); dataS[i][2] = rb.getBorrowedBook().getBook().getBarcode();
                dataS[i][3] = rb.getBorrowedBook().getBook().getName(); dataS[i][4] = rb.getBorrowedBook().getBook().getAuthor();
                dataS[i][5] = rb.getBorrowedBook().getDueDate(); dataS[i][6] = rb.getReturnDate(); dataS[i][7] = rb.getBorrowedBook().getPrice() + " VND";
                int damageCount = 0;
                for (BookDamage bd : parent.listBookDamage) { if (bd.getReturnedBook() == rb) damageCount++; }
                dataS[i][8] = damageCount == 0 ? "OK" : damageCount + " damage(s)";
            }
            parent.tblScanned.setModel(new DefaultTableModel(dataS, columnsS) { @Override public boolean isCellEditable(int row, int col) { return col == 8; } });
            parent.tblScanned.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
            parent.tblScanned.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox(), parent));
            
            dispose();
        }
    }
}
