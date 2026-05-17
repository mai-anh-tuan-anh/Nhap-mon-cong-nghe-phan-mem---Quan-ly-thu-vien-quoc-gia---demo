package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.*;

public class BorrowingReceiptDAO extends DAO {

    public BorrowingReceiptDAO() {
        super();
    }

    public BorrowingReceipt getBorrowedBook(Reader r) {
        BorrowingReceipt br = null;
        String sql = "SELECT br.*, bb.id as bbId, bb.borrowDate, bb.dueDate, bb.price as bbPrice, " +
                     "b.id as bId, b.code, b.name as bName, b.author, b.barcode as bBarcode, b.price as bPrice " +
                     "FROM tblBorrowingReceipt br " +
                     "JOIN tblBorrowedBook bb ON br.id = bb.tblBorrowingReceiptId " +
                     "JOIN tblBook b ON bb.tblBookId = b.id " +
                     "WHERE br.tblReaderId = ? " +
                     "AND bb.id NOT IN (SELECT tblBorrowedBookId FROM tblReturnedBook)";
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, r.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (br == null) {
                    br = new BorrowingReceipt();
                    br.setId(rs.getInt("id"));
                    br.setBarcode(rs.getString("barcode"));
                    br.setNote(rs.getString("note"));
                    br.setCreatedDate(rs.getTimestamp("createdDate"));
                    br.setDepositAmount(rs.getFloat("depositAmount"));
                    br.setReader(r);
                }

                BorrowedBook bb = new BorrowedBook();
                bb.setId(rs.getInt("bbId"));
                bb.setBorrowDate(rs.getDate("borrowDate"));
                bb.setDueDate(rs.getDate("dueDate"));
                bb.setPrice(rs.getFloat("bbPrice"));

                Book b = new Book();
                b.setId(rs.getInt("bId"));
                b.setCode(rs.getString("code"));
                b.setName(rs.getString("bName"));
                b.setAuthor(rs.getString("author"));
                b.setBarcode(rs.getString("bBarcode"));
                b.setPrice(rs.getFloat("bPrice"));
                bb.setBook(b);

                br.getListBorrowedBook().add(bb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return br;
    }
}
