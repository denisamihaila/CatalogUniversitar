package Repositories;

import Servicii.CatalogService;
import Entitati.Department;
import Entitati.Course;
import Entitati.Professor;
import Entitati.Student;
import Entitati.CourseOffering;
import Entitati.Enrollment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class DataLoader {
    private static final String DIR = "data/";
    private final CatalogService service = CatalogService.getInstance();

    private final Map<String, Department> departments = new HashMap<>();
    private final Map<String, Course> courses = new HashMap<>();
    private final Map<String, Professor> professors = new HashMap<>();
    private final Map<String, Student> students = new HashMap<>();
    private final List<CourseOffering> offerings = new ArrayList<>();
    private final List<Enrollment> enrollments = new ArrayList<>();

    public void loadAll() {
        loadDepartments();
        loadCourses();
        loadProfessors();
        loadStudents();
        loadOfferings();
        loadEnrollments();
        loadGrades();
    }

    private void loadDepartments() {
        for (String line : readLines("department.txt")) {
            String[] f = line.split(",");
            Department d = new Department(f[0].trim(), f[1].trim());
            service.adaugaDepartament(d);
            departments.put(f[0].trim(), d);
        }
    }

    private void loadCourses() {
        for (String line : readLines("course.txt")) {
            String[] f = line.split(",");
            Department d = departments.get(f[3].trim());
            Course c = new Course(
                    f[0].trim(), f[1].trim(), Integer.parseInt(f[2].trim()), d
            );
            service.adaugaMaterie(c);
            courses.put(f[0].trim(), c);
        }
    }

    private void loadProfessors() {
        for (String line : readLines("professor.txt")) {
            String[] f = line.split(",");
            Department d = departments.get(f[2].trim());
            Professor p = new Professor(
                    f[0].trim(), f[1].trim(), d
            );
            service.adaugaProfesor(p);
            professors.put(f[0].trim(), p);
        }
    }

    private void loadStudents() {
        for (String line : readLines("student.txt")) {
            String[] f = line.split(",");
            Student s = new Student(
                    f[0].trim(), LocalDate.parse(f[1].trim()), f[2].trim()
            );
            service.adaugaStudent(s);
            students.put(f[0].trim(), s);
        }
    }

    private void loadOfferings() {
        for (String line : readLines("courseOffering.txt")) {
            String[] f = line.split(",");
            Course c = courses.get(f[0].trim());
            int sem = Integer.parseInt(f[1].trim());
            int an = Integer.parseInt(f[2].trim());
            CourseOffering o = new CourseOffering(c, sem, an);
            service.adaugaOferta(o);
            offerings.add(o);
        }
    }

    private void loadEnrollments() {
        for (String line : readLines("enrollment.txt")) {
            String[] f = line.split(",");
            String name = f[0].trim();
            String cod = f[1].trim();
            int sem = Integer.parseInt(f[2].trim());
            int an = Integer.parseInt(f[3].trim());
            LocalDate date = LocalDate.parse(f[4].trim());

            CourseOffering o = offerings.stream()
                    .filter(x -> x.getCurs().getCodMaterie().equals(cod)
                            && x.getSemestru()==sem && x.getAn()==an)
                    .findFirst().orElse(null);
            Student s = students.get(name);
            if (s != null && o != null) {
                service.inscriereStudentLaCursCuData(s.getId(), o.getIdOferta(), date);
            }
        }
        enrollments.clear();
        try {
            EnrollmentDao enrollmentDao = new EnrollmentDao();
            enrollments.addAll(enrollmentDao.findAll());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadGrades() {
        List<String> lines = readLines("grade.txt");

        EnrollmentDao enrollmentDao = new EnrollmentDao();

        for (String line : lines) {
            String[] f = line.split(",");
            if (f.length < 6) {
                System.err.println("Linia are prea puține câmpuri!");
                continue;
            }

            String name = f[0].trim();
            String cod  = f[1].trim();
            int sem;
            int an;
            LocalDate date;
            double val;
            try {
                sem  = Integer.parseInt(f[2].trim());
                an   = Integer.parseInt(f[3].trim());
                date = LocalDate.parse(f[4].trim());
                val  = Double.parseDouble(f[5].trim());
            } catch (Exception ex) {
                System.err.println("Eroare de parsare pentru linia: " + line);
                continue;
            }

            try {
                Integer idIns = enrollmentDao.findIdByStudentCourseDate(
                        name, cod, sem, an, date);
                if (idIns != null) {
                    int idNota = service.inregistrareNota(idIns, val);
                } else {
                    System.err.println("NU s-a găsit înscriere pentru: " + line);
                }
            } catch (SQLException ex) {
                System.err.println("Eroare JDBC pentru linia: " + line);
                ex.printStackTrace();
            }
        }
    }


    private List<String> readLines(String fileName) {
        try {
            return Files.readAllLines(Paths.get(DIR + fileName));
        } catch (IOException e) {
            throw new RuntimeException("Nu se poate citi fișierul: " + fileName, e);
        }
    }
}
