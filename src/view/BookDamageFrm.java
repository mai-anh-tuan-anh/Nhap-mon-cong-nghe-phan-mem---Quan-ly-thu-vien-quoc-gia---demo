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
        int gap = 25;

        JLabel bookCodeLabel = new JLabel("- Book Code: " + rb.getBorrowedBook().getBook().getCode());
        bookCodeLabel.setBounds(20, y, 300, 20);
        panel.add(bookCodeLabel);
        y += gap;

        JLabel barCodeLabel = new JLabel("- BarCode: " + rb.getBorrowedBook().getBook().getBarcode());
        barCodeLabel.setBounds(20, y, 300, 20);
        panel.add(barCodeLabel);
        y += gap;

        JLabel nameLabel = new JLabel("- Name: " + rb.getBorrowedBook().getBook().getName());
        nameLabel.setBounds(20, y, 300, 20);
        panel.add(nameLabel);
        y += gap;

        JLabel authorLabel = new JLabel("- Author: " + rb.getBorrowedBook().getBook().getAuthor());
        authorLabel.setBounds(20, y, 300, 20);
        panel.add(authorLabel);
        y += gap;

        JLabel borrowDateLabel = new JLabel("- Borrowing Date: " + rb.getBorrowedBook().getBorrowDate());
        borrowDateLabel.setBounds(20, y, 300, 20);
        panel.add(borrowDateLabel);
        y += gap;

        JLabel dueDateLabel = new JLabel("- Due Date: " + rb.getBorrowedBook().getDueDate());
        dueDateLabel.setBounds(20, y, 300, 20);
        panel.add(dueDateLabel);
        y += gap;

        JLabel returnDateLabel = new JLabel("- Returning Date: " + rb.getReturnDate());
        returnDateLabel.setBounds(20, y, 300, 20);
        panel.add(returnDateLabel);
        y += gap;

        JLabel depositLabel = new JLabel("- Cover Price: " + rb.getBorrowedBook().getPrice() + " VND");
        depositLabel.setBounds(20, y, 300, 20);
        panel.add(depositLabel);
        y += gap;

        JLabel damageTypeLabel = new JLabel("- Type of Damage:");
        damageTypeLabel.setBounds(20, y, 150, 20);
        panel.add(damageTypeLabel);
        y += gap;

        tblDamage = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblDamage);
        scrollPane.setBounds(20, y, 540, 110);
        panel.add(scrollPane);
        y += 130;

        btnAdd = new JButton("Add");
        btnAdd.setBounds(20, y, 80, 25);
        panel.add(btnAdd);
        y += 40;

        btnBack = new JButton("Back");
        btnBack.setBounds(150, y, 100, 30);
        btnBack.addActionListener(e -> dispose());
        panel.add(btnBack);

        btnAccept = new JButton("Accept");
        btnAccept.setBounds(300, y, 100, 30);
        btnAccept.addActionListener(this);
        panel.add(btnAccept);

        add(panel);
        loadDamage();
    }

    private void loadDamage() {
        DamageDAO dd = new DamageDAO();
        listDamage = dd.getAllDamage();
        String[] columns = {"ID", "Name", "Fine Rate (%)", "Select"};
        Object[][] data = new Object[listDamage.size()][4];
        for (int i = 0; i < listDamage.size(); i++) {
            Damage d = listDamage.get(i);
            data[i][0] = d.getId();
            data[i][1] = d.getName();
            data[i][2] = d.getFineRate();
            
            boolean selected = false;
            for (BookDamage bd : returnedBook.getListBookDamage()) {
                if (bd.getDamage().getId() == d.getId()) {
                    selected = true;
                    break;
                }
            }
            data[i][3] = selected;
        }
        tblDamage.setModel(new DefaultTableModel(data, columns) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 3 ? Boolean.class : super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) { return column == 3; }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAccept) {
            returnedBook.getListBookDamage().clear();
            for (int i = 0; i < listDamage.size(); i++) {
                Boolean selected = (Boolean) tblDamage.getValueAt(i, 3);
                if (selected != null && selected) {
                    Damage d = listDamage.get(i);
                    BookDamage bd = new BookDamage();
                    bd.setDamage(d);
                    bd.setDetectedDate(new Date());
                    bd.setFineAmount(returnedBook.getBorrowedBook().getPrice() * d.getFineRate() / 100);
                    bd.setNote(d.getName() + " Damage");
                    returnedBook.getListBookDamage().add(bd);
                }
            }
            parent.refreshScannedTable();
            dispose();
        }
    }
}
