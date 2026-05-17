package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.DamageDAO;
import model.*;

public class BookDamageFrm extends JFrame implements ActionListener {
    private BorrowingReceipt borrowingReceipt;
    private ReturningReceipt returningReceipt;
    private ReturnedBook returnedBook;
    private ArrayList<BookDamage> listBookDamage;
    private ArrayList<Damage> listDamage;
    private JTable tblDamage;
    private JButton btnAdd, btnAccept, btnBack;

    public BookDamageFrm(BorrowingReceipt br, ReturningReceipt rr, ReturnedBook rb, ArrayList<BookDamage> listBD) {
        super("Select Damage Status");
        this.borrowingReceipt = br; this.returningReceipt = rr; this.returnedBook = rb; this.listBookDamage = listBD;
        setSize(600, 500); setLocationRelativeTo(null); setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(); panel.setLayout(null); panel.setBackground(new Color(240, 240, 240));
        JLabel title = new JLabel("BOOK DAMAGE SELECTION"); title.setFont(new Font("Arial", Font.BOLD, 18)); title.setBounds(180, 10, 250, 30); panel.add(title);

        int y = 50;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        panel.add(new JLabel("- Book Code: " + rb.getBorrowedBook().getBook().getCode())).setBounds(20, y, 300, 20); y += 25;
        panel.add(new JLabel("- BarCode: " + rb.getBorrowedBook().getBook().getBarcode())).setBounds(20, y, 300, 20); y += 25;
        panel.add(new JLabel("- Name: " + rb.getBorrowedBook().getBook().getName())).setBounds(20, y, 300, 20); y += 25;
        panel.add(new JLabel("- Author: " + rb.getBorrowedBook().getBook().getAuthor())).setBounds(20, y, 300, 20); y += 25;
        panel.add(new JLabel("- Borrowing Date: " + sdf.format(rb.getBorrowedBook().getBorrowDate()))).setBounds(20, y, 300, 20); y += 25;
        panel.add(new JLabel("- Due Date: " + sdf.format(rb.getBorrowedBook().getDueDate()))).setBounds(20, y, 300, 20); y += 25;
        panel.add(new JLabel("- Returning Date: " + sdf.format(rb.getReturnDate()))).setBounds(20, y, 300, 20); y += 25;
        panel.add(new JLabel("- Cover Price: " + rb.getBorrowedBook().getPrice() + " VND")).setBounds(20, y, 300, 20); y += 25;
        panel.add(new JLabel("- Type of Damage:")).setBounds(20, y, 150, 20); y += 25;

        tblDamage = new JTable(); JScrollPane scrollPane = new JScrollPane(tblDamage);
        scrollPane.setBounds(20, y, 540, 110); panel.add(scrollPane); y += 130;

        btnAdd = new JButton("Add"); btnAdd.setBounds(20, y, 80, 25); panel.add(btnAdd); y += 40;
        btnBack = new JButton("Back"); btnBack.setBounds(150, y, 100, 30); btnBack.addActionListener(this); panel.add(btnBack);
        btnAccept = new JButton("Accept"); btnAccept.setBounds(300, y, 100, 30); btnAccept.addActionListener(this); panel.add(btnAccept);

        add(panel);
        actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Init"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Init")) {
            DamageDAO dd = new DamageDAO();
            listDamage = dd.chooseDamage();
            Object[][] data = new Object[listDamage.size()][4];
            for (int i = 0; i < listDamage.size(); i++) {
                Damage d = listDamage.get(i);
                data[i][0]=d.getId(); data[i][1]=d.getName(); data[i][2]=d.getFineRate();
                boolean sel = false; for (BookDamage bd : listBookDamage) { if (bd.getReturnedBook().getId() == returnedBook.getId() && bd.getDamage().getId() == d.getId()) { sel = true; break; } }
                data[i][3] = sel;
            }
            tblDamage.setModel(new DefaultTableModel(data, new String[]{"Order", "Type", "Fine Rate", "Select"}) {
                @Override public Class<?> getColumnClass(int c) { return c == 3 ? Boolean.class : super.getColumnClass(c); }
                @Override public boolean isCellEditable(int r, int c) { return c == 3; }
            });

        } else if (e.getSource() == btnAccept) {
            ArrayList<BookDamage> toRemove = new ArrayList<>();
            for (BookDamage bd : listBookDamage) { if (bd.getReturnedBook().getId() == returnedBook.getId()) toRemove.add(bd); }
            listBookDamage.removeAll(toRemove);
            for (int i = 0; i < listDamage.size(); i++) {
                Boolean sel = (Boolean) tblDamage.getValueAt(i, 3);
                if (sel != null && sel) {
                    Damage d = listDamage.get(i); BookDamage bd = new BookDamage(); bd.setDamage(d); bd.setDetectedDate(new Date());
                    bd.setFineAmount(returnedBook.getBorrowedBook().getPrice() * d.getFineRate() / 100);
                    bd.setNote(d.getName() + " Damage"); bd.setReturnedBook(returnedBook); listBookDamage.add(bd);
                }
            }
            new BookReturnFrm(borrowingReceipt, returningReceipt, listBookDamage).setVisible(true); dispose();
        } else if (e.getSource() == btnBack) {
            new BookReturnFrm(borrowingReceipt, returningReceipt, listBookDamage).setVisible(true); dispose();
        }
    }
}
