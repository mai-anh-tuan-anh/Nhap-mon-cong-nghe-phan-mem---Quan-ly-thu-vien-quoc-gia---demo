package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Damage;

public class DamageDAO extends DAO {

    public DamageDAO() {
        super();
    }

    public ArrayList<Damage> getAllDamage() {
        ArrayList<Damage> result = new ArrayList<>();
        String sql = "SELECT * FROM tblDamage";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Damage d = new Damage();
                d.setId(rs.getInt("id"));
                d.setName(rs.getString("name"));
                d.setFineRate(rs.getFloat("fineRate"));
                result.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
