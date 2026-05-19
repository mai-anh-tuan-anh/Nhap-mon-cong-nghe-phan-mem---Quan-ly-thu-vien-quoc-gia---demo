package dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.User;
public class UserDAO extends DAO {
    public UserDAO() {
        super();
    }
    public boolean checkLogin(User u) {
        String sql = "SELECT fullName, role, id FROM tblUser WHERE username = ? AND password = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                u.setFullName(rs.getString("fullName"));
                u.setRole(rs.getString("role"));
                u.setId(rs.getInt("id"));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
