package dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.Book;
import model.BookDamage;
import model.BorrowedBook;
import model.Reader;
import model.ReturnedBook;
import model.ReturningReceipt;

public class ReturningReceiptDAO extends DAO {
    public ReturningReceiptDAO() {
        super();
    }
    public ArrayList<ReturningReceipt> getReturnedBook(Reader r) {
        ArrayList<ReturningReceipt> result = new ArrayList<>();
        String sql = "SELECT rr.*, rb.id as rbId, rb.returnDate, " +
                     "bb.id as bbId, bb.borrowDate, bb.dueDate, bb.price as bbPrice, " +
                     "b.id as bId, b.name as bName, b.code, b.barcode as bBarcode, b.author as bAuthor, " +
                     "(SELECT COALESCE(SUM(fineAmount), 0) FROM tblBookDamage WHERE tblReturnedBookId = rb.id) as damageFine " +
                     "FROM tblReturningReceipt rr " +
                     "JOIN tblReturnedBook rb ON rr.id = rb.tblReturningReceiptId " +
                     "JOIN tblBorrowedBook bb ON rb.tblBorrowedBookId = bb.id " +
                     "JOIN tblBook b ON bb.tblBookId = b.id " +
                     "WHERE rr.tblReaderId = ? " +
                     "ORDER BY rr.createdDate DESC, rb.id ASC";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, r.getId());
            ResultSet rs = ps.executeQuery();

            ReturningReceipt currentRR = null;
            StringBuilder fineString = new StringBuilder();
            
            while (rs.next()) {
                int rrId = rs.getInt("id");
                if (currentRR == null || currentRR.getId() != rrId) {
                    if (currentRR != null) {
                        currentRR.setNote(currentRR.getNote() + " | Fine:" + fineString.toString());
                    }
                    
                    currentRR = new ReturningReceipt();
                    currentRR.setId(rrId);
                    currentRR.setBarcode(rs.getString("barcode"));
                    currentRR.setNote(rs.getString("note") == null ? "" : rs.getString("note"));
                    currentRR.setCreatedDate(rs.getTimestamp("createdDate"));
                    currentRR.setReader(r);
                    result.add(currentRR);
                    fineString = new StringBuilder();
                }

                ReturnedBook rb = new ReturnedBook();
                rb.setId(rs.getInt("rbId"));
                rb.setReturnDate(rs.getDate("returnDate"));
                
                BorrowedBook bb = new BorrowedBook();
                bb.setId(rs.getInt("bbId"));
                bb.setBorrowDate(rs.getDate("borrowDate"));
                bb.setDueDate(rs.getDate("dueDate"));
                bb.setPrice(rs.getFloat("bbPrice"));
                
                Book b = new Book();
                b.setId(rs.getInt("bId"));
                b.setName(rs.getString("bName"));
                b.setCode(rs.getString("code"));
                b.setBarcode(rs.getString("bBarcode"));
                b.setAuthor(rs.getString("bAuthor"));
                bb.setBook(b);
                rb.setBorrowedBook(bb);
                currentRR.getListReturnedBook().add(rb);
                float lateFine = (rb.getReturnDate().after(bb.getDueDate())) ? bb.getPrice() * 0.2f : 0;
                float totalBookFine = lateFine + rs.getFloat("damageFine");
                fineString.append(totalBookFine).append(";");
            }
            if (currentRR != null) {
                currentRR.setNote(currentRR.getNote() + " | Fine:" + fineString.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean updateReturningReceipt(ReturningReceipt rr, ArrayList<BookDamage> listBD) {
        String sqlRR = "INSERT INTO tblReturningReceipt(barcode, note, createdDate, tblReaderId, tblUserId) VALUES(?,?,?,?,?)";
        String sqlRB = "INSERT INTO tblReturnedBook(returnDate, tblReturningReceiptId, tblBorrowedBookId) VALUES(?,?,?)";
        String sqlBD = "INSERT INTO tblBookDamage(note, detectedDate, fineAmount, tblDamageId, tblReturnedBookId) VALUES(?,?,?,?,?)";

        try {
            con.setAutoCommit(false);
            PreparedStatement psRR = con.prepareStatement(sqlRR, Statement.RETURN_GENERATED_KEYS);
            psRR.setString(1, rr.getBarcode());
            psRR.setString(2, rr.getNote());
            psRR.setTimestamp(3, new java.sql.Timestamp(rr.getCreatedDate().getTime()));
            psRR.setInt(4, rr.getReader().getId());
            psRR.setInt(5, rr.getUser().getId());
            psRR.executeUpdate();
            
            ResultSet rsRR = psRR.getGeneratedKeys();
            if (rsRR.next()) rr.setId(rsRR.getInt(1));

            for (ReturnedBook rb : rr.getListReturnedBook()) {
                PreparedStatement psRB = con.prepareStatement(sqlRB, Statement.RETURN_GENERATED_KEYS);
                psRB.setDate(1, new java.sql.Date(rb.getReturnDate().getTime()));
                psRB.setInt(2, rr.getId());
                psRB.setInt(3, rb.getBorrowedBook().getId());
                psRB.executeUpdate();
                ResultSet rsRB = psRB.getGeneratedKeys();
                if (rsRB.next()) rb.setId(rsRB.getInt(1));
            }

            for (BookDamage bd : listBD) {
                PreparedStatement psBD = con.prepareStatement(sqlBD);
                psBD.setString(1, bd.getNote());
                psBD.setDate(2, new java.sql.Date(bd.getDetectedDate().getTime()));
                psBD.setFloat(3, bd.getFineAmount());
                psBD.setInt(4, bd.getDamage().getId());
                psBD.setInt(5, bd.getReturnedBook().getId());
                psBD.executeUpdate();
            }
            con.commit();
        } catch (Exception e) {
            try { con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { con.setAutoCommit(true); } catch (Exception e) { e.printStackTrace(); }
        }
        return true;
    }
}
