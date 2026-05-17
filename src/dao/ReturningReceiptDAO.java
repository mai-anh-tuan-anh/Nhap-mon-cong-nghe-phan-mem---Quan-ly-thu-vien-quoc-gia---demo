package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.*;

public class ReturningReceiptDAO extends DAO {

    public ReturningReceiptDAO() {
        super();
    }

    public ArrayList<ReturningReceipt> getReturnedBook(Reader r) {
        ArrayList<ReturningReceipt> result = new ArrayList<>();
        String sql = "SELECT rr.*, rb.id as rbId, rb.returnDate, " +
                     "bb.id as bbId, bb.borrowDate, bb.dueDate, bb.price as bbPrice, " +
                     "b.id as bId, b.name as bName, b.code, b.barcode as bBarcode, b.author as bAuthor, " +
                     "bd.id as bdId, bd.note as bdNote, bd.detectedDate, bd.fineAmount, " +
                     "d.id as dId, d.name as dName, d.fineRate " +
                     "FROM tblReturningReceipt rr " +
                     "JOIN tblReturnedBook rb ON rr.id = rb.tblReturningReceiptId " +
                     "JOIN tblBorrowedBook bb ON rb.tblBorrowedBookId = bb.id " +
                     "JOIN tblBook b ON bb.tblBookId = b.id " +
                     "LEFT JOIN tblBookDamage bd ON rb.id = bd.tblReturnedBookId " +
                     "LEFT JOIN tblDamage d ON bd.tblDamageId = d.id " +
                     "WHERE rr.tblReaderId = ? " +
                     "ORDER BY rr.id DESC, rb.id ASC, bd.id ASC";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, r.getId());
            ResultSet rs = ps.executeQuery();

            ReturningReceipt currentRR = null;
            ReturnedBook currentRB = null;
            
            while (rs.next()) {
                int rrId = rs.getInt("id");
                if (currentRR == null || currentRR.getId() != rrId) {
                    currentRR = new ReturningReceipt();
                    currentRR.setId(rrId);
                    currentRR.setBarcode(rs.getString("barcode"));
                    currentRR.setNote(rs.getString("note"));
                    currentRR.setCreatedDate(rs.getTimestamp("createdDate"));
                    currentRR.setReader(r);
                    result.add(currentRR);
                    currentRB = null; // Reset RB tracker when RR changes
                }

                int rbId = rs.getInt("rbId");
                if (currentRB == null || currentRB.getId() != rbId) {
                    currentRB = new ReturnedBook();
                    currentRB.setId(rbId);
                    currentRB.setReturnDate(rs.getDate("returnDate"));
                    
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
                    currentRB.setBorrowedBook(bb);

                    currentRR.getListReturnedBook().add(currentRB);
                }

                int bdId = rs.getInt("bdId");
                if (bdId > 0) { // There is a damage record
                    boolean exists = false;
                    for(BookDamage existingBD : currentRB.getListBookDamage()){
                        if(existingBD.getId() == bdId){
                            exists = true;
                            break;
                        }
                    }
                    if(!exists){
                        BookDamage bd = new BookDamage();
                        bd.setId(bdId);
                        bd.setNote(rs.getString("bdNote"));
                        bd.setDetectedDate(rs.getDate("detectedDate"));
                        bd.setFineAmount(rs.getFloat("fineAmount"));
                        
                        Damage d = new Damage();
                        d.setId(rs.getInt("dId"));
                        d.setName(rs.getString("dName"));
                        d.setFineRate(rs.getFloat("fineRate"));
                        bd.setDamage(d);
                        
                        currentRB.getListBookDamage().add(bd);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean updateReturningReceipt(ReturningReceipt rr) {
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
            if (rsRR.next()) {
                rr.setId(rsRR.getInt(1));
            }

            for (ReturnedBook rb : rr.getListReturnedBook()) {
                PreparedStatement psRB = con.prepareStatement(sqlRB, Statement.RETURN_GENERATED_KEYS);
                psRB.setDate(1, new java.sql.Date(rb.getReturnDate().getTime()));
                psRB.setInt(2, rr.getId());
                psRB.setInt(3, rb.getBorrowedBook().getId());
                psRB.executeUpdate();

                ResultSet rsRB = psRB.getGeneratedKeys();
                if (rsRB.next()) {
                    rb.setId(rsRB.getInt(1));
                }

                for (BookDamage bd : rb.getListBookDamage()) {
                    PreparedStatement psBD = con.prepareStatement(sqlBD);
                    psBD.setString(1, bd.getNote());
                    psBD.setDate(2, new java.sql.Date(bd.getDetectedDate().getTime()));
                    psBD.setFloat(3, bd.getFineAmount());
                    psBD.setInt(4, bd.getDamage().getId());
                    psBD.setInt(5, rb.getId());
                    psBD.executeUpdate();
                }
            }
            con.commit();
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
