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

    public double getValoareNota() {
        return valoareNota;
    }
}
