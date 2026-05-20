package test;
import org.junit.Assert;
import org.junit.Test;
import dao.BookDAO;
import model.Book;

public class BookDAOTest {
    @Test
    public void testSearchBookByCodeStandard() {
        BookDAO bd = new BookDAO();
        Book b = bd.searchBookByCode("00001");
        Assert.assertNotNull(b);
        Assert.assertEquals("Harry Potter", b.getName());
        Assert.assertEquals("12345", b.getBarcode());
    }

    @Test
    public void testSearchBookByBarcodeStandard() {
        BookDAO bd = new BookDAO();
        Book b = bd.searchBookByCode("12345");
        Assert.assertNotNull(b);
        Assert.assertEquals("Harry Potter", b.getName());
        Assert.assertEquals("00001", b.getCode());
    }

    @Test
    public void testSearchBookByCodeFail() {
        BookDAO bd = new BookDAO();
        Book b = bd.searchBookByCode("non_existent_code");
        Assert.assertNull(b);
    }
}
