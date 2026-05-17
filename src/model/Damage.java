package model;

import java.io.Serializable;

public class Damage implements Serializable {
    private int id;
    private String name;
    private float fineRate;

    public Damage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getFineRate() {
        return fineRate;
    }

    public void setFineRate(float fineRate) {
        this.fineRate = fineRate;
    }
}
