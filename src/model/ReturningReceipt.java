package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ReturningReceipt implements Serializable {
    private int id;
    private String barcode;
    private String note;
    private Date createdDate;
    private Reader reader;
    private User user;
    private ArrayList<ReturnedBook> listReturnedBook;

    public ReturningReceipt() {
        listReturnedBook = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<ReturnedBook> getListReturnedBook() {
        return listReturnedBook;
    }

    public void setListReturnedBook(ArrayList<ReturnedBook> listReturnedBook) {
        this.listReturnedBook = listReturnedBook;
    }
}
