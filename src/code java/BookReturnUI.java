import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class BookReturnUI extends JFrame {

    public BookReturnUI() {
        setTitle("Book Return");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel title = new JLabel("BOOK RETURN");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(400, 10, 200, 30);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 240));

        // Top buttons
        JButton backBtn = new JButton("Back");
        backBtn.setBounds(20, 10, 80, 25);

        JButton scanBookBtn = new JButton("Scan Book");
        scanBookBtn.setBounds(110, 10, 100, 25);

        JButton nextBtn = new JButton("Next");
        nextBtn.setBounds(220, 10, 80, 25);

        panel.add(title);

        // Reader Information label
        JLabel readerInfoLabel = new JLabel("Reader Information");
        readerInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        readerInfoLabel.setBounds(20, 45, 150, 25);
        panel.add(readerInfoLabel);

        // Reader info table
        String[] readerColumns = {"Reader ID", "Name", "DoB", "Address", "Phone number", "Bar Code"};
        Object[][] readerData = {
            {"001", "B", "01/01/2026", "Ha Noi", "0123456789", "0123456789"}
        };
        DefaultTableModel readerModel = new DefaultTableModel(readerData, readerColumns);
        JTable readerTable = new JTable(readerModel);
        JScrollPane readerScroll = new JScrollPane(readerTable);
        readerScroll.setBounds(20, 75, 950, 50);
        panel.add(readerScroll);

        // Unreturned books section
        JLabel unreturnedLabel = new JLabel("List of borrowed books that have not been returned");
        unreturnedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        unreturnedLabel.setBounds(20, 135, 350, 25);
        panel.add(unreturnedLabel);

        String[] unreturnedColumns = {
            "Order", "Book Code", "Bar Code", "Name", "Author",
            "Borrowing Date", "Due Date", "Cover Price"
        };
        Object[][] unreturnedData = {
            {"1", "00001", "12345", "Harry Potter", "J.K Rowling", "11/03/2026", "11/04/2026", "10000vnd"}
        };
        DefaultTableModel unreturnedModel = new DefaultTableModel(unreturnedData, unreturnedColumns);
        JTable unreturnedTable = new JTable(unreturnedModel);
        JScrollPane unreturnedScroll = new JScrollPane(unreturnedTable);
        unreturnedScroll.setBounds(20, 165, 950, 60);
        panel.add(unreturnedScroll);

        // Scanned books section (NEW)
        JLabel scannedLabel = new JLabel("List of scanned books");
        scannedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        scannedLabel.setBounds(20, 280, 350, 25);
        panel.add(scannedLabel);

        String[] scannedColumns = {
            "Order", "Book Code", "Bar Code", "Name", "Author",
            "Due Date", "Returning Date", "Cover Price", "Damage Status Before Borrowing", "Damage Status Now"
        };
        Object[][] scannedData = {
            {"1", "00002", "12346", "Diary of a Cricket", "To Hoai", "11/04/2026", "12/04/2026", "20000vnd", "OK", "TORN"}
        };
        DefaultTableModel scannedModel = new DefaultTableModel(scannedData, scannedColumns);
        JTable scannedTable = new JTable(scannedModel);
        scannedTable.getColumnModel().getColumn(9).setCellRenderer(new ButtonRenderer());
        scannedTable.getColumnModel().getColumn(9).setCellEditor(new ButtonEditor(new JCheckBox()));
        JScrollPane scannedScroll = new JScrollPane(scannedTable);
        scannedScroll.setBounds(20, 310, 950, 60);
        panel.add(scannedScroll);

        // Returned books section
        JLabel returnedLabel = new JLabel("List of borrowed books that have been returned");
        returnedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        returnedLabel.setBounds(20, 405, 350, 25);
        panel.add(returnedLabel);

        String[] returnedColumns = {
            "Order", "Book Code", "Bar Code", "Name", "Author",
            "Cover Price", "Due Date", "Returning Date", "Fine Amount"
        };
        Object[][] returnedData = {
            {"1", "00003", "12347", "Tam Cam", "Unknown", "30000vnd", "01/04/2026", "20/03/2026", "0vnd"},
            {"2", "00004", "12348", "The lotus shoes", "Jane Yang", "40000vnd", "01/04/2026", "21/03/2026", "40000vnd"}
        };
        DefaultTableModel returnedModel = new DefaultTableModel(returnedData, returnedColumns);
        JTable returnedTable = new JTable(returnedModel);
        JScrollPane returnedScroll = new JScrollPane(returnedTable);
        returnedScroll.setBounds(20, 435, 950, 80);
        panel.add(returnedScroll);

        // Bottom buttons
        JButton scanBtn = new JButton("Scan book");
        scanBtn.setBounds(20, 560, 100, 30);
        panel.add(scanBtn);

        JButton bottomBackBtn = new JButton("Back");
        bottomBackBtn.setBounds(20, 610, 80, 30);
        panel.add(bottomBackBtn);

        JButton bottomNextBtn = new JButton("Next");
        bottomNextBtn.setBounds(120, 610, 80, 30);
        panel.add(bottomNextBtn);

        // Add action listeners
        backBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Back clicked");
        });

        scanBookBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Scan Book clicked");
        });

        nextBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Next clicked");
        });

        scanBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Scan book clicked");
        });

        bottomBackBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Back clicked");
        });

        bottomNextBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Next clicked");
        });

        add(panel);
    }

    public static void main(String[] args) {
        new BookReturnUI().setVisible(true);
    }
}

// Button Renderer class
class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value != null ? value.toString() : "");
        return this;
    }
}

// Button Editor class
class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean isPushed;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        label = value != null ? value.toString() : "";
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            new BookDamageUI().setVisible(true);
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}
