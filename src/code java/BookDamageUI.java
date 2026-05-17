import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BookDamageUI extends JFrame {

    public BookDamageUI() {
        setTitle("Select Damage State");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 240));

        // Title
        JLabel title = new JLabel("BOOK DAMAGE SELECTION");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(180, 10, 250, 30);
        panel.add(title);

        // Book information
        int y = 50;
        int gap = 25;

        JLabel bookCodeLabel = new JLabel("- Book Code: 000002");
        bookCodeLabel.setBounds(20, y, 300, 20);
        panel.add(bookCodeLabel);
        y += gap;

        JLabel barCodeLabel = new JLabel("- BarCode: 12346");
        barCodeLabel.setBounds(20, y, 300, 20);
        panel.add(barCodeLabel);
        y += gap;

        JLabel nameLabel = new JLabel("- Name: Diary of a Cricket");
        nameLabel.setBounds(20, y, 300, 20);
        panel.add(nameLabel);
        y += gap;

        JLabel authorLabel = new JLabel("- Author: Tô Hoài");
        authorLabel.setBounds(20, y, 300, 20);
        panel.add(authorLabel);
        y += gap;

        JLabel borrowDateLabel = new JLabel("- Borrowing Date: 11/03/2026");
        borrowDateLabel.setBounds(20, y, 300, 20);
        panel.add(borrowDateLabel);
        y += gap;

        JLabel dueDateLabel = new JLabel("- Due Date: 11/04/2026");
        dueDateLabel.setBounds(20, y, 300, 20);
        panel.add(dueDateLabel);
        y += gap;

        JLabel returnDateLabel = new JLabel("- Returning Date: 12/04/2026");
        returnDateLabel.setBounds(20, y, 300, 20);
        panel.add(returnDateLabel);
        y += gap;

        JLabel depositLabel = new JLabel("- Cover Price: 20000vnd");
        depositLabel.setBounds(20, y, 300, 20);
        panel.add(depositLabel);
        y += gap;

        // Type of Damage label
        JLabel damageTypeLabel = new JLabel("- Type of Damage:");
        damageTypeLabel.setBounds(20, y, 150, 20);
        panel.add(damageTypeLabel);
        y += gap;

        // Damage table
        String[] columns = {"Type", "Fine Amount (Based on Cover Price)", "Select"};
        Object[][] data = {
            {"Torn", "100%", false},
            {"Graffiti", "50%", false},
            {"Mold", "100%", false},
            {"Crumpled", "100%", false},
            {"Bent", "80%", false}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 2) return Boolean.class;
                return String.class;
            }
        };

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, y, 540, 110);
        panel.add(scrollPane);
        y += 130;

        // Add button (next to table)
        JButton addBtn = new JButton("Add");
        addBtn.setBounds(20, y, 80, 25);
        panel.add(addBtn);
        y += 40;

        // Bottom buttons
        JButton backBtn = new JButton("Back");
        backBtn.setBounds(150, y, 100, 30);
        panel.add(backBtn);

        JButton acceptBtn = new JButton("Accept");
        acceptBtn.setBounds(300, y, 100, 30);
        panel.add(acceptBtn);

        // Action listeners
        addBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Add damage type clicked");
        });

        backBtn.addActionListener(e -> {
            dispose();
        });

        acceptBtn.addActionListener(e -> {
            new BookReturnInvoiceUI().setVisible(true);
            dispose();
        });

        add(panel);
    }

    public static void main(String[] args) {
        new BookDamageUI().setVisible(true);
    }
}
