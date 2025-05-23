package Entitati;

import java.util.ArrayList;
import java.util.List;

public class CourseOffering {
    private int idOferta;
    private final Course curs;
    private final int semestru;
    private final int an;
    private Professor professor;
    private final List<Classroom> sali = new ArrayList<>();

    public CourseOffering(Course curs, int semestru, int an) {
        this.curs = curs;
        this.semestru = semestru;
        this.an = an;
    }

    public CourseOffering(int idOferta, Course curs, int semestru, int an) {
        this(curs, semestru, an);
        this.idOferta = idOferta;
    }

    public int getIdOferta() {
        return idOferta;
    }

    public void setIdOferta(int idOferta) {
        this.idOferta = idOferta;
    }

    public Course getCurs() {
        return curs;
    }

    public int getSemestru() {
        return semestru;
    }

    public int getAn() {
        return an;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public void adaugaSala(Classroom sala) {
        sali.add(sala);
    }

    @Override
    public String toString() {
        return "Oferta " + idOferta + " - " + curs.getDenumire() + " (s" + semestru + ", " + an + ")";
    }

}
