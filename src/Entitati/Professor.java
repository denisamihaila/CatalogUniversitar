package Entitati;

public class Professor extends Person {
    private String titluDidactic;
    private Department department;

    public Professor(String nume, String titluDidactic, Department department) {
        super(nume);
        this.titluDidactic = titluDidactic;
        this.department = department;
    }

    public Professor(int idProfesor, String nume, String titluDidactic, Department department) {
        super(idProfesor, nume);
        this.titluDidactic = titluDidactic;
        this.department = department;
    }

    public void setIdProfesor(int id) {
        setId(id);
    }

    public String getTitluDidactic() {
        return titluDidactic;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
