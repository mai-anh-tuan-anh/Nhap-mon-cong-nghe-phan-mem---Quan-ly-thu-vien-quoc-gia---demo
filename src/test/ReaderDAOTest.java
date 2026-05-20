package test;
import org.junit.Assert;
import org.junit.Test;
import dao.ReaderDAO;
import model.Reader;
import java.util.ArrayList;

public class ReaderDAOTest {
    @Test
    public void testSearchReaderByNameStandard() {
        ReaderDAO rd = new ReaderDAO();
        ArrayList<Reader> result = rd.searchReaderByName("b");
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() == 2);
        Assert.assertEquals("B", result.get(0).getName());
    }

    @Test
    public void testSearchReaderByNameFail() {
        ReaderDAO rd = new ReaderDAO();
        ArrayList<Reader> result = rd.searchReaderByName("xyz");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testSearchReaderByBarCodeStandard() {
        ReaderDAO rd = new ReaderDAO();
        Reader r = rd.searchReaderByBarCode("1111111111");
        Assert.assertNotNull(r);
        Assert.assertEquals("B", r.getName());
        Assert.assertEquals("1111111111", r.getBarcode());
    }

    @Test
    public void testSearchReaderByBarCodeFail() {
        ReaderDAO rd = new ReaderDAO();
        Reader r = rd.searchReaderByBarCode("6767676767");
        Assert.assertNull(r);
    }
}
