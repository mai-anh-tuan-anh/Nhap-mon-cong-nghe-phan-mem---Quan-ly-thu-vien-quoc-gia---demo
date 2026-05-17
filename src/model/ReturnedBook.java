package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ReturnedBook implements Serializable {
    private int id;
    private Date returnDate;
    private BorrowedBook borrowedBook;
    private ArrayList<BookDamage> listBookDamage;

    public ReturnedBook() {
        listBookDamage = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public BorrowedBook getBorrowedBook() {
        return borrowedBook;
    }

    public void setBorrowedBook(BorrowedBook borrowedBook) {
        this.borrowedBook = borrowedBook;
    }

    public ArrayList<BookDamage> getListBookDamage() {
        return listBookDamage;
    }

    public void setListBookDamage(ArrayList<BookDamage> listBookDamage) {
        this.listBookDamage = listBookDamage;
    }
}
