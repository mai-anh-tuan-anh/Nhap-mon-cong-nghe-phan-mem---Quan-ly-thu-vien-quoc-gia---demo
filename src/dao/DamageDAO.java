package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.Damage;

public class DamageDAO extends DAO {

    public DamageDAO() {
        super();
    }

    public ArrayList<Damage> chooseDamage() {
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

    public boolean addDamage(Damage d) {
        String sql = "INSERT INTO tblDamage(name, fineRate) VALUES(?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, d.getName());
            ps.setFloat(2, d.getFineRate());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                d.setId(rs.getInt(1));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    }

