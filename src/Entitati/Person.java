package Entitati;

public abstract class Person {
    protected int id;
    protected final String nume;

    protected Person(String nume) {
        this.nume = nume;
    }

    protected Person(int id, String nume) {
        this(nume);
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    @Override
    public String toString() {
        return nume + " (id=" + id + ")";
    }
}
