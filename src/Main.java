import Repositories.DBInitializer;
import Repositories.DataLoader;
import Servicii.CatalogService;
import Entitati.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DBInitializer.initialize();
        new DataLoader().loadAll();
        runMenu();
    }

    private static void runMenu() {
        Scanner sc = new Scanner(System.in);
        CatalogService svc = CatalogService.getInstance();

        while (true) {
            System.out.println("\n----------MENIU----------");
            System.out.println("1. Inscriere student la curs");
            System.out.println("2. Retragere student din curs");
            System.out.println("3. Listare cursuri pentru semestru și an");
            System.out.println("4. Atribuire profesor la curs");
            System.out.println("5. Adaugare materie in catalog");
            System.out.println("6. Modificare credite materie");
            System.out.println("7. Vizualizare studenti inscrisi la curs");
            System.out.println("8. Inregistrare nota pentru student");
            System.out.println("9. Listare note si medie student");
            System.out.println("10. Cautare cursuri dupa departament");
            System.out.println("0. Iesire");

            int opt;
            try {
                System.out.print("Optiunea: ");
                opt = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Eroare: introduceți un număr între 0 și 10.");
                continue;
            }

            try {
                switch (opt) {
                    case 1 -> {
                        // 1. Inscriere student la curs
                        System.out.print("Id student: ");
                        int idS = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Id oferta curs: ");
                        int idO = sc.nextInt();
                        sc.nextLine();

                        LocalDate data;
                        while (true) {
                            System.out.print("Data inscrierii (YYYY-MM-DD): ");
                            String dataStr = sc.nextLine();
                            try {
                                data = LocalDate.parse(dataStr);
                                break;
                            } catch (DateTimeParseException ex) {
                                System.out.println("Eroare: format invalid. Exemplu valid: 2025-09-01");
                            }
                        }

                        Enrollment e = svc.inscriereStudentLaCursCuData(idS, idO, data);
                        System.out.println("Inscriere realizata: id=" + e.getIdInscriere());
                    }
                    case 2 -> {
                        // 2. Retragere student din curs
                        System.out.print("Id inscriere: ");
                        int idIns = sc.nextInt();
                        sc.nextLine();

                        svc.retragereStudentDinCurs(idIns);
                        System.out.println("Retragere efectuata pentru inscrierea " + idIns);
                    }
                    case 3 -> {
                        // 3. Listare cursuri pentru semestru
                        System.out.print("Semestru: ");
                        int sem = sc.nextInt();
                        sc.nextLine();

                        System.out.print("An: ");
                        int an = sc.nextInt();
                        sc.nextLine();

                        List<CourseOffering> oferte = svc.listareCursuri(sem, an);
                        if (oferte.isEmpty()) {
                            System.out.printf("Nu există oferte pentru semestrul %d din anul %d.%n", sem, an);
                        } else {
                            System.out.printf("Oferte pentru semestrul %d din anul %d:%n", sem, an);
                            for (CourseOffering o : oferte) {
                                System.out.printf("  Oferta %d - %s (sem %d, an %d)%n",
                                        o.getIdOferta(),
                                        o.getCurs().getDenumire(),
                                        o.getSemestru(),
                                        o.getAn());
                            }
                        }
                    }
                    case 4 -> {
                        // 4. Atribuire profesor la curs
                        System.out.print("Id profesor: ");
                        int idP = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Id oferta: ");
                        int idOf = sc.nextInt();
                        sc.nextLine();

                        svc.atribuireProfesorLaCurs(idP, idOf);
                        System.out.println("Profesor asignat ofertei " + idOf);
                    }
                    case 5 -> {
                        // 5. Adaugare materie in catalog
                        System.out.print("Cod materie: ");
                        String cod = sc.nextLine();

                        System.out.print("Denumire: ");
                        String den = sc.nextLine();

                        System.out.print("Credite: ");
                        int cred = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Departament (nume exact): ");
                        String numeDept = sc.nextLine();

                        Department dept = svc.getDepartmentByName(numeDept);
                        Course c = new Course(cod, den, cred, dept);
                        svc.adaugaMaterie(c);
                        System.out.println("Materie adăugată: " + c.getDenumire() + " (" + c.getCodMaterie() + ")");
                    }
                    case 6 -> {
                        // 6. Modificare credite materie
                        System.out.print("Cod materie: ");
                        String codM = sc.nextLine();

                        System.out.print("Credite noi: ");
                        int credNoi = sc.nextInt();
                        sc.nextLine();

                        svc.modificaMaterie(codM, credNoi);
                        System.out.println("Credite modificate pentru " + codM);
                    }
                    case 7 -> {
                        // 7. Vizualizare studenti inscrisi la curs
                        System.out.print("Id oferta curs: ");
                        int idOffer = sc.nextInt();
                        sc.nextLine();

                        List<Student> studs = svc.vizualizareStudentiCurs(idOffer);
                        if (studs.isEmpty()) {
                            System.out.println("Nu există studenți înscriși pe oferta " + idOffer);
                        } else {
                            studs.forEach(System.out::println);
                        }
                    }
                    case 8 -> {
                        // 8. Inregistrare nota pentru student
                        System.out.print("Id inscriere: ");
                        int idEn = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Valoare nota: ");
                        double val = sc.nextDouble();
                        sc.nextLine();

                        int idNota = svc.inregistrareNota(idEn, val);
                        System.out.println("Nota înregistrată cu id = " + idNota);
                    }
                    case 9 -> {
                        // 9. Listare note și medie student
                        System.out.print("Id student: ");
                        int idStu = sc.nextInt();
                        sc.nextLine();

                        List<Grade> grades = svc.noteStudent(idStu);
                        if (grades.isEmpty()) {
                            System.out.println("Nu există note pentru studentul cu id=" + idStu);
                        } else {
                            System.out.println("Notele obținute:");
                            grades.forEach(gd ->System.out.printf("%.2f%n", gd.getValoareNota()));
                            System.out.printf("Media: %.2f%n", svc.medieStudent(idStu));
                        }
                    }
                    case 10 -> {
                        // 10. Cautare cursuri după departament
                        System.out.print("Nume departament: ");
                        String numeDep = sc.nextLine();

                        List<Course> cours = svc.cautaCursuriDepartament(numeDep);
                        if (cours.isEmpty()) {
                            System.out.println("Nu există cursuri în departamentul '" + numeDep + "'");
                        } else {
                            cours.forEach(System.out::println);
                        }
                    }
                    case 0 -> {
                        System.out.println("La revedere!");
                        sc.close();
                        return;
                    }
                    default -> System.out.println("Opțiune invalidă. Alege un număr între 0 și 10.");
                }
            } catch (RuntimeException ex) {
                System.out.println("Eroare: " + ex.getMessage());
            }
        }
    }
}
