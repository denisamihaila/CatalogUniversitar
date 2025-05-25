package Entitati;

public class Classroom {
    private int idSala;
    private final String numarSala;
    private final int capacitate;
    private final TipSala tip;

    public Classroom(int idSala, String numarSala, int capacitate, TipSala tip) {
        this.idSala = idSala;
        this.numarSala = numarSala;
        this.capacitate = capacitate;
        this.tip = tip;
    }

    public Classroom(String numarSala, int capacitate, TipSala tip) {
        this.numarSala = numarSala;
        this.capacitate = capacitate;
        this.tip = tip;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public String getNumarSala() {
        return numarSala;
    }

    public int getCapacitate() {
        return capacitate;
    }

    public TipSala getTip() {
        return tip;
    }
}
