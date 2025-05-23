import Repositories.DBInitializer;
import Repositories.DataLoader;
import Servicii.CatalogService;
import Entitati.*;
import Entitati.CourseOffering;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1) Reinitialize DB schema and tables
        DBInitializer.initialize();
        // 2) Load initial data from text files
        new DataLoader().loadAll();
        // 3) Start interactive menu
        runMenu();
    }

    private static void runMenu() {
        Scanner sc = new Scanner(System.in);
        CatalogService svc = CatalogService.getInstance();
        while (true) {
            System.out.println("\nCe vreti sa faceti?");
            System.out.println("1. Inscriere student la curs");
            System.out.println("2. Retragere student din curs");
            System.out.println("3. Listare cursuri pentru semestru");
            System.out.println("4. Atribuire profesor la curs");
            System.out.println("5. Adaugare materie in catalog");
            System.out.println("6. Modificare informatii materie");
            System.out.println("7. Vizualizare studenti inscrisi la curs");
            System.out.println("8. Inregistrare nota pentru student");
            System.out.println("9. Listare note si medie student");
            System.out.println("10. Cautare cursuri dupa departament");
            System.out.println("0. Iesire");
            System.out.print("Optiunea: ");
            int opt = sc.nextInt(); sc.nextLine();
            switch (opt) {
                case 1:
                    System.out.print("Id student: ");
                    int idS = sc.nextInt(); sc.nextLine();
                    System.out.print("Id oferta curs: ");
                    int idO = sc.nextInt(); sc.nextLine();
                    System.out.print("Data inscrierii (YYYY-MM-DD): ");
                    String dataStr = sc.nextLine();
                    LocalDate data = LocalDate.parse(dataStr);
                    Enrollment e = svc.inscriereStudentLaCursCuData(idS, idO, data);
                    System.out.println("Inscriere realizata: id=" + e.getIdInscriere());
                    break;
                case 2:
                    System.out.print("Id inscriere: ");
                    int idIns = sc.nextInt(); sc.nextLine();
                    svc.retragereStudentDinCurs(idIns);
                    System.out.println("Retragere efectuata pentru inscrierea " + idIns);
                    break;
                case 3:
                    System.out.print("Semestru: ");
                    int sem = sc.nextInt(); sc.nextLine();

                    System.out.print("An: ");
                    int an  = sc.nextInt(); sc.nextLine();

                    List<CourseOffering> oferte = svc.listareCursuri(sem, an);

                    if (oferte.isEmpty()) {
                        System.out.printf("Nu există oferte pentru semestrul %d din %d.%n", sem, an);
                    } else {
                        System.out.printf("Oferte pentru semestrul %d din %d:%n", sem, an);
                        for (CourseOffering o : oferte) {
                            System.out.printf("  Oferta %d - %s (s%d, %d)%n",
                                    o.getIdOferta(),
                                    o.getCurs().getDenumire(),
                                    o.getSemestru(),
                                    o.getAn());
                        }
                    }
                    break;
                case 4:
                    System.out.print("Id profesor: ");
                    int idP = sc.nextInt(); sc.nextLine();
                    System.out.print("Id oferta: ");
                    int idOf = sc.nextInt(); sc.nextLine();
                    svc.atribuireProfesorLaCurs(idP, idOf);
                    System.out.println("Profesor asignat ofertei " + idOf);
                    break;
                case 5:
                    System.out.print("Cod materie: ");
                    String cod = sc.nextLine();
                    System.out.print("Denumire: ");
                    String den = sc.nextLine();
                    System.out.print("Credite: ");
                    int cr = sc.nextInt(); sc.nextLine();
                    System.out.print("Departament: ");
                    String dept = sc.nextLine();
                    Department d = new Department(dept, dept); // facultate same as dept for simplicity
                    Course c = new Course(cod, den, cr, d);
                    svc.adaugaMaterie(c);
                    System.out.println("Materie adaugata: " + c);
                    break;
                case 6:
                    System.out.print("Cod materie: ");
                    String codM = sc.nextLine();
                    System.out.print("Credite noi: ");
                    int credNoi = sc.nextInt(); sc.nextLine();
                    svc.modificaMaterie(codM, credNoi);
                    System.out.println("Credite modificate pentru " + codM);
                    break;
                case 7:
                    System.out.print("Id oferta curs: ");
                    int idOffer = sc.nextInt(); sc.nextLine();
                    List<Student> studs = svc.vizualizareStudentiCurs(idOffer);
                    studs.forEach(System.out::println);
                    break;
                case 8:
                    System.out.print("Id inscriere: ");
                    int idEn = sc.nextInt(); sc.nextLine();
                    System.out.print("Valoare nota: ");
                    double val = sc.nextDouble(); sc.nextLine();
                    svc.inregistrareNota(idEn, val);
                    System.out.println("Nota inregistrata pentru inscrierea " + idEn);
                    break;
                case 9:
                    System.out.print("Id student: ");
                    int idStu = sc.nextInt(); sc.nextLine();
                    List<Grade> grades = svc.noteStudent(idStu);
                    grades.forEach(gd -> System.out.println(gd.getValoareNota()));
                    System.out.println("Media: " + svc.medieStudent(idStu));
                    break;
                case 10:
                    System.out.print("Nume departament: ");
                    String numeDep = sc.nextLine();
                    List<Course> cours = svc.cautaCursuriDepartament(numeDep);
                    cours.forEach(System.out::println);
                    break;
                case 0:
                    System.out.println("La revedere!");
                    sc.close();
                    return;
                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }
}
