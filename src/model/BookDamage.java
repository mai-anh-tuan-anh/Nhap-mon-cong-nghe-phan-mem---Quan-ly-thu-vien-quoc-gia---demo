package model;

import java.io.Serializable;
import java.util.Date;

public class BookDamage implements Serializable {
    private int id;
    private String note;
    private Date detectedDate;
    private float fineAmount;
    private Damage damage;

    public BookDamage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDetectedDate() {
        return detectedDate;
    }

    public void setDetectedDate(Date detectedDate) {
        this.detectedDate = detectedDate;
    }

    public float getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(float fineAmount) {
        this.fineAmount = fineAmount;
    }

    public Damage getDamage() {
        return damage;
    }

    public void setDamage(Damage damage) {
        this.damage = damage;
    }
}
