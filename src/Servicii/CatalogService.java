package Servicii;

import Entitati.*;
import Repositories.*;
import Utile.AuditService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public final class CatalogService {
    private static CatalogService instance;

    private final CourseOfferingRepo offeringRepo = CourseOfferingRepo.getInstance();
    private final Map<Integer, Student> studenti = new HashMap<>();
    private final Map<Integer, Professor> profesori = new HashMap<>();
    private final Map<Integer, Enrollment> inscrieri = new HashMap<>();

    private final StudentDao studentDao = new StudentDao();
    private final CourseDao courseDao = new CourseDao();
    private final ProfessorDao professorDao = new ProfessorDao();
    private final CourseOfferingDao offeringDao = new CourseOfferingDao();
    private final EnrollmentDao enrollmentDao = new EnrollmentDao();
    private final DepartmentDao departmentDao = new DepartmentDao();

    private final AuditService audit = AuditService.getInstance();

    private CatalogService() {}

    public static CatalogService getInstance() {
        if (instance == null) instance = new CatalogService();
        return instance;
    }

    // 1. Înscriere student la curs
    public Enrollment inscriereStudentLaCursCuData(int idStudent, int idOferta, LocalDate data) {
        Student s = studenti.get(idStudent);
        CourseOffering o = offeringRepo.get(idOferta);
        Enrollment e = new Enrollment(0, s, o, data);
        try {
            int newId = enrollmentDao.insert(e);
            e = new Enrollment(newId, s, o, data);
            inscrieri.put(newId, e);
            s.adaugaInscriere(e);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        audit.log("inscriere_student_la_curs_cu_data");
        return e;
    }

    // 2. Retragere student din curs
    public void retragereStudentDinCurs(int idInscriere) {
        try {
            enrollmentDao.delete(idInscriere);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        audit.log("retragere_student_din_curs");
    }

    // 3. Listare cursuri disponibile pentru un semestru
    public List<CourseOffering> listareCursuri(int sem, int an) {
        audit.log("listare_cursuri_semestru");
        try {
            return offeringDao.findBySemestruAndAn(sem, an);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    // 4. Atribuire profesor la curs
    public void atribuireProfesorLaCurs(int idProfesor, int idOferta) {
        try {
            CourseOffering o = offeringRepo.get(idOferta);
            Professor p = professorDao.findById(idProfesor);
            o.setProfessor(p);
            offeringDao.update(o);
            audit.log("atribuire_profesor_la_curs");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 5. Adăugare materie în catalog
    public void adaugaMaterie(Course c) {
        try {
            courseDao.insert(c);
            audit.log("adaugare_materie");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 6. Modificare informații materie
    public void modificaMaterie(String cod, int crediteNoi) {
        try {
            Course c = courseDao.findByCod(cod);
            if (c != null) {
                c.setCredite(crediteNoi);
                courseDao.update(c);
            }
            audit.log("modificare_materie");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 7. Vizualizare studenți înscriși la un curs
    public List<Student> vizualizareStudentiCurs(int idOferta) {
        audit.log("vizualizare_studenti_curs");
        try {
            return enrollmentDao.findAll().stream()
                    .filter(e -> e.getOferta().getIdOferta() == idOferta)
                    .map(Enrollment::getStudent)
                    .toList();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    // 8. Înregistrare notă pentru student
    public int inregistrareNota(int idInscriere, double valoareNota) {
        try {
            int idNota = enrollmentDao.insertGrade(idInscriere, valoareNota);

            Enrollment e = enrollmentDao.findById(idInscriere);
            if (e != null) {
                Grade g = new Grade(idNota, e, valoareNota);
                e.adaugaNota(g);
            }

            audit.log("inregistrare_nota");
            System.out.println("Nota inregistrata cu id = " + idNota);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return idInscriere;
    }

    // 9. Listare note & medie student
    public List<Grade> noteStudent(int idStudent) {
        audit.log("listare_note_student");
        try {
            return enrollmentDao.findGradesByStudentId(idStudent);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public double medieStudent(int idStudent) {
        List<Grade> grades = noteStudent(idStudent);
        return grades.stream()
                .mapToDouble(Grade::getValoareNota)
                .average()
                .orElse(0.0);
    }

    // 10. Căutare cursuri după departament
    public List<Course> cautaCursuriDepartament(String numeDept) {
        audit.log("cautare_cursuri_departament");
        try {
            return courseDao.findAll().stream()
                    .filter(c -> c.getDepartment().getNumeDepartament().equalsIgnoreCase(numeDept))
                    .toList();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    // metode auxiliare
    public void adaugaStudent(Student s) {
        try {
            studentDao.insert(s);
            studenti.put(s.getId(), s);
            audit.log("adaugare_student");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void adaugaProfesor(Professor p) {
        try {
            professorDao.insert(p);
            profesori.put(p.getId(), p);
            audit.log("adaugare_profesor");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void adaugaDepartament(Department d) {
        try {
            departmentDao.insert(d);
            audit.log("adaugare_departament");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void adaugaOferta(CourseOffering o) {
        try {
            offeringDao.insert(o);
            audit.log("adaugare_oferta");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Department getDepartmentByName(String nume) {
        try {
            return departmentDao.findByName(nume);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Department> getAllDepartments() {
        try {
            return new DepartmentDao().findAll();
        } catch(SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
