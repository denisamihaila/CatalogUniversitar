package Repositories;

import Entitati.CourseOffering;

import java.sql.SQLException;
import java.util.List;

public final class CourseOfferingRepo {
    private static CourseOfferingRepo instance;
    private final CourseOfferingDao dao = new CourseOfferingDao();

    private CourseOfferingRepo() {}

    public static CourseOfferingRepo getInstance() {
        if (instance == null) instance = new CourseOfferingRepo();
        return instance;
    }

    public void add(CourseOffering o) {
        try {
            dao.insert(o);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la inserarea ofertei", e);
        }
    }

    public CourseOffering get(int id) {
        try {
            return dao.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la citirea ofertei cu id=" + id, e);
        }
    }

    public List<CourseOffering> all() {
        try {
            return dao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la citirea tuturor ofertelor", e);
        }
    }
}
