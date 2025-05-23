package Entitati;

public class Grade {
    private int idNota;
    private final Enrollment inscriere;
    private final double valoareNota;

    public Grade(Enrollment inscriere, double valoareNota) {
        this.inscriere = inscriere;
        this.valoareNota = valoareNota;
    }

    public Grade(int idNota, Enrollment inscriere, double valoareNota) {
        this(inscriere, valoareNota);
        this.idNota = idNota;
    }

    public void setIdNota(int idNota) {
        this.idNota = idNota;
    }

    public int getIdNota() {
        return idNota;
    }

    public Enrollment getInscriere() {
        return inscriere;
    }

    public double getValoareNota() {
        return valoareNota;
    }
}
