package dao;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Reader;
public class ReaderDAO extends DAO {
    public ReaderDAO() {
        super();
    }
    public ArrayList<Reader> searchReaderByName(String key) {
        ArrayList<Reader> result = new ArrayList<>();
        String sql = "SELECT * FROM tblReader WHERE name LIKE ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + key + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reader r = new Reader();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setDateOfBirth(rs.getDate("dateOfBirth"));
                r.setAddress(rs.getString("address"));
                r.setPhoneNumber(rs.getString("phoneNumber"));
                r.setBarcode(rs.getString("barcode"));
                result.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Reader searchReaderByBarCode(String barcode) {
        Reader r = null;
        String sql = "SELECT * FROM tblReader WHERE barcode = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, barcode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                r = new Reader();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setDateOfBirth(rs.getDate("dateOfBirth"));
                r.setAddress(rs.getString("address"));
                r.setPhoneNumber(rs.getString("phoneNumber"));
                r.setBarcode(rs.getString("barcode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
}