package test;
import org.junit.Assert;
import org.junit.Test;
import dao.DamageDAO;
import model.Damage;
import java.util.ArrayList;

public class DamageDAOTest {
    @Test
    public void testChooseDamageStandard() {
        DamageDAO dd = new DamageDAO();
        ArrayList<Damage> result = dd.chooseDamage();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() >= 3);
        Assert.assertEquals("Torn", result.get(0).getName());
    }

    @Test
    public void testAddDamageStandard() {
        DamageDAO dd = new DamageDAO();
        Damage d = new Damage();
        String uniqueName = "Damage" + System.currentTimeMillis();
        d.setName(uniqueName);
        d.setFineRate(25.0f);
        boolean result = dd.addDamage(d);
        Assert.assertTrue(result);
        Assert.assertTrue(d.getId() > 0);
    }
}
