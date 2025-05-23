package Repositories;

import Entitati.Course;

import java.sql.SQLException;
import java.util.Collection;

public final class CourseRepo {
    private static CourseRepo instance;
    private final CourseDao dao = new CourseDao();

    private CourseRepo() {}

    public static CourseRepo getInstance() {
        if (instance == null) instance = new CourseRepo();
        return instance;
    }

    public void add(Course c) {
        try {
            dao.insert(c);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la inserarea cursului", e);
        }
    }

    public Course get(String cod) {
        try {
            return dao.findByCod(cod);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la citirea cursului cu cod=" + cod, e);
        }
    }

    public Collection<Course> getAll() {
        try {
            return dao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la citirea tuturor cursurilor", e);
        }
    }

    public boolean exists(String cod) {
        return get(cod) != null;
    }
}
