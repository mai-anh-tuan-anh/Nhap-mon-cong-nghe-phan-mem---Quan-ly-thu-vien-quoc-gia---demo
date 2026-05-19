package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.ReaderDAO;
import model.Reader;
import model.User;

public class SearchReaderFrm extends JFrame implements ActionListener {
    private JTextField txtSearch;
    private JButton btnSearch, btnScan;
    private JTable tblResult;
    private User user;
    private ArrayList<Reader> listReader;

    public SearchReaderFrm(User user) {
        super("Search Reader");
        this.user = user;
        setSize(700, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 240));

        JLabel title = new JLabel("SEARCH READER");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(250, 10, 200, 30);
        panel.add(title);

        panel.add(new JLabel("Search Reader:")).setBounds(20, 50, 120, 25);
        txtSearch = new JTextField();
        txtSearch.setBounds(140, 50, 200, 25);
        panel.add(txtSearch);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(140, 90, 100, 30);
        btnSearch.addActionListener(this);
        panel.add(btnSearch);

        btnScan = new JButton("Scan Card");
        btnScan.setBounds(260, 90, 130, 30);
        btnScan.addActionListener(this);
        panel.add(btnScan);

        tblResult = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblResult);
        scrollPane.setBounds(20, 140, 650, 100);
        panel.add(scrollPane);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(400, 90, 100, 30);
        btnBack.addActionListener(e -> { new LibrarianHomeFrm(user).setVisible(true); dispose(); });
        panel.add(btnBack);

        add(panel);

        tblResult.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = tblResult.getSelectedRow();
                if (row >= 0 && listReader != null && row < listReader.size()) {
                    (new ReaderDetailFrm(user, listReader.get(row))).setVisible(true);
                    dispose();
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        if (e.getSource() == btnSearch) {
            String key = txtSearch.getText().trim();
            if (!key.isEmpty()) {
                listReader = new ReaderDAO().searchReaderByName(key);
                String[] columns = {"Reader ID", "Name", "DoB", "Address", "Phone", "Barcode"};
                Object[][] data = new Object[listReader.size()][6];
                for (int i = 0; i < listReader.size(); i++) {
                    Reader r = listReader.get(i);
                    data[i][0] = r.getId();
                    data[i][1] = r.getName();
                    data[i][2] = r.getDateOfBirth() != null ? sdf.format(r.getDateOfBirth()) : "";
                    data[i][3] = r.getAddress();
                    data[i][4] = r.getPhoneNumber();
                    data[i][5] = r.getBarcode();
                }
                tblResult.setModel(new DefaultTableModel(data, columns) { @Override public boolean isCellEditable(int r, int c) { return false; } });
            }
        } else if (e.getSource() == btnScan) {
            String barcode = txtSearch.getText().trim();
            if (!barcode.isEmpty()) {
                Reader r = new ReaderDAO().searchReaderByBarCode(barcode);
                if (r != null) {
                    listReader = new ArrayList<>();
                    listReader.add(r);
                    String[] columns = {"Reader ID", "Name", "DoB", "Address", "Phone", "Barcode"};
                    Object[][] data = new Object[listReader.size()][6];
                    for (int i = 0; i < listReader.size(); i++) {
                        Reader r2 = listReader.get(i);
                        data[i][0] = r2.getId();
                        data[i][1] = r2.getName();
                        data[i][2] = r2.getDateOfBirth() != null ? sdf.format(r2.getDateOfBirth()) : "";
                        data[i][3] = r2.getAddress();
                        data[i][4] = r2.getPhoneNumber();
                        data[i][5] = r2.getBarcode();
                    }
                    tblResult.setModel(new DefaultTableModel(data, columns) { @Override public boolean isCellEditable(int r, int c) { return false; } });
                } else { JOptionPane.showMessageDialog(this, "Reader not found!"); }
            }
        }
    }
}
