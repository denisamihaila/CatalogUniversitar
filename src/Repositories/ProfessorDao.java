package Repositories;

import Entitati.Professor;
import Entitati.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDao {
    private final Connection conn = DBConnection.getInstance().getConnection();
    private final DepartmentDao departmentDao = new DepartmentDao();

    // CREATE
    public void insert(Professor p) throws SQLException {
        String sql = "INSERT INTO professor (nume, titlu_didactic, id_departament) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNume());
            ps.setString(2, p.getTitluDidactic());
            ps.setInt(3, p.getDepartment().getIdDepartament());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setIdProfesor(rs.getInt(1));
                }
            }
        }
    }

    // READ by ID
    public Professor findById(int id) throws SQLException {
        String sql = "SELECT * FROM professor WHERE id_profesor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Department d = departmentDao.findById(rs.getInt("id_departament"));
                    return new Professor(
                            rs.getInt("id_profesor"),
                            rs.getString("nume"),
                            rs.getString("titlu_didactic"),
                            d
                    );
                }
            }
        }
        return null;
    }

    // READ all
    public List<Professor> findAll() throws SQLException {
        String sql = "SELECT id_profesor FROM professor";
        List<Professor> list = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(findById(rs.getInt("id_profesor")));
            }
        }
        return list;
    }

    // UPDATE
    public void update(Professor p) throws SQLException {
        String sql = "UPDATE professor SET nume=?, titlu_didactic=?, id_departament=? WHERE id_profesor=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNume());
            ps.setString(2, p.getTitluDidactic());
            ps.setInt(3, p.getDepartment().getIdDepartament());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM professor WHERE id_profesor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
