package dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Book;
public class BookDAO extends DAO {
    public BookDAO() {
        super();
    }
    public Book searchBookByCode(String code) {
        Book b = null;
        String sql = "SELECT * FROM tblBook WHERE code = ? OR barcode = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, code);
            ps.setString(2, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                b = new Book();
                b.setId(rs.getInt("id"));
                b.setCode(rs.getString("code"));
                b.setName(rs.getString("name"));
                b.setAuthor(rs.getString("author"));
                b.setPublicationYear(rs.getInt("publicationYear"));
                b.setDescription(rs.getString("description"));
                b.setBarcode(rs.getString("barcode"));
                b.setPrice(rs.getFloat("price"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
}
