package Entitati;

import java.time.LocalDate;
import java.util.*;

public class Student extends Person implements Comparable<Student> {
    private final LocalDate dataNasterii;
    private final String specializare;
    private final Set<Enrollment> inscrieri = new HashSet<>();

    public Student(String nume, LocalDate dataNasterii, String specializare) {
        super(nume);
        this.dataNasterii = dataNasterii;
        this.specializare = specializare;
    }

    public Student(int idStudent, String nume, LocalDate dataNasterii, String specializare) {
        super(idStudent, nume);
        this.dataNasterii = dataNasterii;
        this.specializare = specializare;
    }

    public void setIdStudent(int id) {
        setId(id);
    }

    public void adaugaInscriere(Enrollment e) {
        inscrieri.add(e);
    }

    public void stergeInscriere(Enrollment e) {
        inscrieri.remove(e);
    }

    public List<Grade> getNote() {
        List<Grade> note = new ArrayList<>();
        inscrieri.forEach(e -> note.addAll(e.getNote()));
        return note;
    }

    public double calculeazaMedie() {
        return getNote().stream().mapToDouble(Grade::getValoareNota).average().orElse(0.0);
    }

    public LocalDate getDataNasterii() {
        return dataNasterii;
    }

    public String getSpecializare() {
        return specializare;
    }

    public Set<Enrollment> getInscrieri() {
        return inscrieri;
    }

    @Override
    public int compareTo(Student o) {
        return this.nume.compareTo(o.nume);
    }
}
