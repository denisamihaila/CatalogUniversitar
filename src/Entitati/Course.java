package Entitati;

public class Course {
    private String codMaterie;
    private String denumire;
    private int credite;
    private Department department;

    public Course(String codMaterie, String denumire, int credite, Department department) {
        this.codMaterie = codMaterie;
        this.denumire = denumire;
        this.credite = credite;
        this.department = department;
    }

    public String getCodMaterie() {
        return codMaterie;
    }

    public String getDenumire() {
        return denumire;
    }

    public int getCredite() {
        return credite;
    }

    public Department getDepartment() {
        return department;
    }

    public void setCredite(int credite) {
        this.credite = credite;
    }

    @Override
    public String toString() {
        return denumire + " (" + codMaterie + ")";
    }
}
