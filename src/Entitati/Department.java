package Entitati;

public class Department {
    private int idDepartament;
    private final String numeDepartament;
    private final String facultate;

    public Department(String numeDepartament, String facultate) {
        this.numeDepartament = numeDepartament;
        this.facultate = facultate;
    }

    public Department(int idDepartament, String numeDepartament, String facultate) {
        this(numeDepartament, facultate);
        this.idDepartament = idDepartament;
    }

    public int getIdDepartament() {
        return idDepartament;
    }

    public void setIdDepartament(int idDepartament) {
        this.idDepartament = idDepartament;
    }

    public String getNumeDepartament() {
        return numeDepartament;
    }

    public String getFacultate() {
        return facultate;
    }
}
