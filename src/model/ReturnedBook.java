package model;

import java.io.Serializable;
import java.util.Date;

public class ReturnedBook implements Serializable {
    private int id;
    private Date returnDate;
    private BorrowedBook borrowedBook;

    public ReturnedBook() {
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
}
