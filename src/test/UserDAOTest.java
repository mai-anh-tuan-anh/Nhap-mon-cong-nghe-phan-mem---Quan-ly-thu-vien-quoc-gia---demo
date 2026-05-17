package test;

import org.junit.Assert;
import org.junit.Test;
import dao.UserDAO;
import model.User;

public class UserDAOTest {
    @Test
    public void testCheckLoginStandard() {
        User u = new User();
        u.setUsername("a");
        u.setPassword("a@123");
        UserDAO ud = new UserDAO();
        boolean result = ud.checkLogin(u);
        Assert.assertTrue(result);
        Assert.assertEquals("Nguyen Van A", u.getFullName());
        Assert.assertEquals("Librarian", u.getRole());
    }

    @Test
    public void testCheckLoginFail() {
        User u = new User();
        u.setUsername("a");
        u.setPassword("wrongpass");
        UserDAO ud = new UserDAO();
        boolean result = ud.checkLogin(u);
        Assert.assertFalse(result);
    }
}
