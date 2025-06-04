package Repositories;

import Entitati.Course;
import Entitati.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {
    private final Connection conn = DBConnection.getInstance().getConnection();
    private final DepartmentDao departmentDao = new DepartmentDao();

    // CREATE
    public void insert(Course c) throws SQLException {
        String sql = "INSERT INTO course (cod_materie, denumire, credite, id_departament) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCodMaterie());
            ps.setString(2, c.getDenumire());
            ps.setInt(3, c.getCredite());
            ps.setInt(4, c.getDepartment().getIdDepartament());
            ps.executeUpdate();
        }
    }

    // READ by cod
    public Course findByCod(String cod) throws SQLException {
        String sql = "SELECT * FROM course WHERE cod_materie = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cod);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Department d = departmentDao.findById(rs.getInt("id_departament"));
                    return new Course(
                            rs.getString("cod_materie"),
                            rs.getString("denumire"),
                            rs.getInt("credite"),
                            d
                    );
                }
            }
        }
        return null;
    }

    // READ all
    public List<Course> findAll() throws SQLException {
        String sql = "SELECT * FROM course";
        List<Course> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Department d = departmentDao.findById(rs.getInt("id_departament"));
                list.add(new Course(
                        rs.getString("cod_materie"),
                        rs.getString("denumire"),
                        rs.getInt("credite"),
                        d
                ));
            }
        }
        return list;
    }

    // UPDATE
    public void update(Course c) throws SQLException {
        String sql = "UPDATE course SET denumire=?, credite=?, id_departament=? WHERE cod_materie=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getDenumire());
            ps.setInt(2, c.getCredite());
            ps.setInt(3, c.getDepartment().getIdDepartament());
            ps.setString(4, c.getCodMaterie());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void delete(String cod) throws SQLException {
        String sql = "DELETE FROM course WHERE cod_materie = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cod);
            ps.executeUpdate();
        }
    }
}
