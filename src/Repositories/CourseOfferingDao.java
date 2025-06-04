package Repositories;

import Entitati.CourseOffering;
import Entitati.Course;
import Entitati.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseOfferingDao {
    private final Connection conn = DBConnection.getInstance().getConnection();
    private final CourseDao courseDao = new CourseDao();
    private final ProfessorDao profDao = new ProfessorDao();

    // CREATE
    public void insert(CourseOffering o) throws SQLException {
        String sql = "INSERT INTO course_offering (cod_materie, semestru, an, id_profesor) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, o.getCurs().getCodMaterie());
            ps.setInt   (2, o.getSemestru());
            ps.setInt   (3, o.getAn());
            ps.setObject(4, o.getProfessor()==null ? null : o.getProfessor().getId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    o.setIdOferta(rs.getInt(1));
                }
            }
        }
    }

    // READ by ID
    public CourseOffering findById(int id) throws SQLException {
        String sql = "SELECT * FROM course_offering WHERE id_oferta = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Course c = courseDao.findByCod(rs.getString("cod_materie"));
                    CourseOffering o = new CourseOffering(
                            rs.getInt("id_oferta"),
                            c,
                            rs.getInt("semestru"),
                            rs.getInt("an")
                    );
                    int profId = rs.getInt("id_profesor");
                    if (!rs.wasNull()) {
                        Professor p = profDao.findById(profId);
                        o.setProfessor(p);
                    }
                    return o;
                }
            }
        }
        return null;
    }

    // READ all
    public List<CourseOffering> findAll() throws SQLException {
        String sql = "SELECT id_oferta FROM course_offering";
        List<CourseOffering> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(findById(rs.getInt("id_oferta")));
            }
        }
        return list;
    }

    // UPDATE
    public void update(CourseOffering o) throws SQLException {
        String sql = "UPDATE course_offering SET cod_materie=?, semestru=?, an=?, id_profesor=? WHERE id_oferta=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getCurs().getCodMaterie());
            ps.setInt   (2, o.getSemestru());
            ps.setInt   (3, o.getAn());
            ps.setObject(4, o.getProfessor()==null ? null : o.getProfessor().getId());
            ps.setInt   (5, o.getIdOferta());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM course_offering WHERE id_oferta = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // READ by semestru & an
    public List<CourseOffering> findBySemestruAndAn(int semestru, int an) throws SQLException {
        String sql = "SELECT * FROM course_offering WHERE semestru = ? AND an = ?";
        List<CourseOffering> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, semestru);
            ps.setInt(2, an);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // citește Course prin CourseDao
                    Course c = courseDao.findByCod(rs.getString("cod_materie"));
                    CourseOffering o = new CourseOffering(
                            rs.getInt("id_oferta"),
                            c,
                            rs.getInt("semestru"),
                            rs.getInt("an")
                    );
                    int profId = rs.getInt("id_profesor");
                    if (!rs.wasNull()) {
                        o.setProfessor(profDao.findById(profId));
                    }
                    list.add(o);
                }
            }
        }
        return list;
    }

}
