package Repositories;

import Entitati.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {
    private final Connection conn = DBConnection.getInstance().getConnection();

    // CREATE
    public void insert(Student s) throws SQLException {
        String sql = "INSERT INTO student (nume, data_nasterii, specializare) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getNume());
            ps.setDate(2, Date.valueOf(s.getDataNasterii()));
            ps.setString(3, s.getSpecializare());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    s.setIdStudent(rs.getInt(1));
                }
            }
        }
    }

    // READ by ID
    public Student findById(int id) throws SQLException {
        String sql = "SELECT * FROM student WHERE id_student = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student s = new Student(
                            rs.getInt("id_student"),
                            rs.getString("nume"),
                            rs.getDate("data_nasterii").toLocalDate(),
                            rs.getString("specializare")
                    );
                    return s;
                }
            }
        }
        return null;
    }

    // READ all
    public List<Student> findAll() throws SQLException {
        String sql = "SELECT id_student FROM student";
        List<Student> list = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(findById(rs.getInt("id_student")));
            }
        }
        return list;
    }

    // UPDATE
    public void update(Student s) throws SQLException {
        String sql = "UPDATE student SET nume = ?, data_nasterii = ?, specializare = ? WHERE id_student = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getNume());
            ps.setDate(2, Date.valueOf(s.getDataNasterii()));
            ps.setString(3, s.getSpecializare());
            ps.setInt(4, s.getId());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM student WHERE id_student = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
