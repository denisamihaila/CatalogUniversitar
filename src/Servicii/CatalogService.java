package Servicii;

import Entitati.*;
import Repositories.*;
import Utile.AuditService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public final class CatalogService {
    private static CatalogService instance; // Singleton

    private final Map<Integer, Student> studenti = new TreeMap<>();
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
        if (instance == null) {
            instance = new CatalogService();
        }
        return instance;
    }

    // Metode de validare / găsire entități

    public Student findStudentById(int idStudent) {
        try {
            Student s = studentDao.findById(idStudent);
            if (s == null) {
                throw new IllegalArgumentException("Nu există student cu id=" + idStudent);
            }
            return s;
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare BD la findStudentById", ex);
        }
    }

    public Professor findProfessorById(int idProfesor) {
        try {
            Professor p = professorDao.findById(idProfesor);
            if (p == null) {
                throw new IllegalArgumentException("Nu există profesor cu id=" + idProfesor);
            }
            return p;
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare BD la findProfessorById", ex);
        }
    }

    public CourseOffering findOfferingById(int idOferta) {
        try {
            CourseOffering o = offeringDao.findById(idOferta);
            if (o == null) {
                throw new IllegalArgumentException("Nu există ofertă cu id=" + idOferta);
            }
            return o;
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare BD la findOfferingById", ex);
        }
    }

    public Enrollment findEnrollmentById(int idInscriere) {
        try {
            Enrollment e = enrollmentDao.findById(idInscriere);
            if (e == null) {
                throw new IllegalArgumentException("Nu există înscriere cu id=" + idInscriere);
            }
            return e;
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare BD la findEnrollmentById", ex);
        }
    }

    public Department getDepartmentByName(String nume) {
        try {
            Department d = departmentDao.findByName(nume);
            if (d == null) {
                throw new IllegalArgumentException("Nu există departament cu numele '" + nume + "'");
            }
            return d;
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare BD la getDepartmentByName", ex);
        }
    }

    // 1. Înscriere student la curs cu dată

    public Enrollment inscriereStudentLaCursCuData(int idStudent, int idOferta, LocalDate data) {
        Student s = findStudentById(idStudent);
        CourseOffering o = findOfferingById(idOferta);

        Enrollment e = new Enrollment(0, s, o, data);
        try {
            int newId = enrollmentDao.insert(e);
            e = new Enrollment(newId, s, o, data);
            inscrieri.put(newId, e);
            s.adaugaInscriere(e);
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare la inserarea înscrierii", ex);
        }
        audit.log("inscriere_student_la_curs_cu_data");
        return e;
    }

    // 2. Retragere student din curs

    public void retragereStudentDinCurs(int idInscriere) {
        try {
            enrollmentDao.delete(idInscriere);
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare la ștergerea înscrierii", ex);
        }
        audit.log("retragere_student_din_curs");
    }

    // 3. Listare cursuri disponibile pentru un semestru și un an

    public List<CourseOffering> listareCursuri(int sem, int an) {
        audit.log("listare_cursuri_semestru");
        try {
            return offeringDao.findBySemestruAndAn(sem, an);
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare BD la listareCursuri", ex);
        }
    }

    // 4. Atribuire profesor la curs

    public void atribuireProfesorLaCurs(int idProfesor, int idOferta) {
        CourseOffering o = findOfferingById(idOferta);
        Professor p = findProfessorById(idProfesor);
        o.setProfessor(p);
        try {
            offeringDao.update(o);
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare la actualizarea ofertei", ex);
        }
        audit.log("atribuire_profesor_la_curs");
    }

    // 5. Adăugare materie în catalog

    public void adaugaMaterie(Course c) {
        try {
            Course existing = courseDao.findByCod(c.getCodMaterie());
            if (existing != null) {
                throw new IllegalArgumentException("Există deja o materie cu codul " + c.getCodMaterie());
            }
            courseDao.insert(c);
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare BD la adaugareMaterie", ex);
        }
        audit.log("adaugare_materie");
    }

    // 6. Modificare credite materie

    public void modificaMaterie(String cod, int crediteNoi) {
        try {
            Course c = courseDao.findByCod(cod);
            if (c == null) {
                throw new IllegalArgumentException("Nu există materie cu codul " + cod);
            }
            c.setCredite(crediteNoi);
            courseDao.update(c);
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare BD la modificareMaterie", ex);
        }
        audit.log("modificare_materie");
    }

    // 7. Vizualizare studenți înscriși la un curs

    public List<Student> vizualizareStudentiCurs(int idOferta) {
        findOfferingById(idOferta);
        audit.log("vizualizare_studenti_curs");
        try {
            return enrollmentDao.findAll().stream()
                    .filter(e -> e.getOferta().getIdOferta() == idOferta)
                    .map(Enrollment::getStudent)
                    .toList();
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare BD la vizualizareStudentiCurs", ex);
        }
    }

    // 8. Înregistrare notă pentru student

    public int inregistrareNota(int idInscriere, double valoareNota) {
        if (valoareNota < 0.0 || valoareNota > 10.0) {
            throw new IllegalArgumentException("Valoarea notei trebuie să fie între 0.0 și 10.0");
        }
        Enrollment e = findEnrollmentById(idInscriere);
        int idNota;
        try {
            idNota = enrollmentDao.insertGrade(idInscriere, valoareNota);
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare BD la inregistrareNota", ex);
        }
        // Adăugăm nota și în obiectul din memorie
        Grade g = new Grade(idNota, e, valoareNota);
        e.adaugaNota(g);
        audit.log("inregistrare_nota");
        return idNota;
    }

    // 9. Listare note & medie student

    public List<Grade> noteStudent(int idStudent) {
        findStudentById(idStudent);
        audit.log("listare_note_student");
        try {
            return enrollmentDao.findGradesByStudentId(idStudent);
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare BD la noteStudent", ex);
        }
    }

    public double medieStudent(int idStudent) {
        findStudentById(idStudent);
        double avg = noteStudent(idStudent).stream()
                .mapToDouble(Grade::getValoareNota)
                .average()
                .orElse(0.0);

        // Păstrăm doar primele două zecimale prin tăiere
        return Math.floor(avg * 100) / 100.0;
    }

    // 10. Căutare cursuri după departament

    public List<Course> cautaCursuriDepartament(String numeDept) {
        audit.log("cautare_cursuri_departament");
        try {
            return courseDao.findAll().stream()
                    .filter(c -> c.getDepartment().getNumeDepartament().equalsIgnoreCase(numeDept))
                    .toList();
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare BD la cautareCursuriDepartament", ex);
        }
    }

    // Metode auxiliare pentru DAO-uri

    public void adaugaStudent(Student s) {
        try {
            studentDao.insert(s);
            studenti.put(s.getId(), s);
            audit.log("adaugare_student");
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare BD la adaugaStudent", ex);
        }
    }

    public void adaugaProfesor(Professor p) {
        try {
            professorDao.insert(p);
            profesori.put(p.getId(), p);
            audit.log("adaugare_profesor");
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare BD la adaugaProfesor", ex);
        }
    }

    public void adaugaDepartament(Department d) {
        try {
            departmentDao.insert(d);
            audit.log("adaugare_departament");
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare BD la adaugaDepartament", ex);
        }
    }

    public void adaugaOferta(CourseOffering o) {
        try {
            offeringDao.insert(o);
            audit.log("adaugare_oferta");
        } catch (SQLException ex) {
            throw new RuntimeException("Eroare BD la adaugaOferta", ex);
        }
    }
}
