package Repositories;

import Entitati.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDao {
    private final Connection conn = DBConnection.getInstance().getConnection();

    // CREATE
    public void insert(Department d) throws SQLException {
        String sql = "INSERT INTO department (nume_departament, facultate) VALUES (?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getNumeDepartament());
            ps.setString(2, d.getFacultate());
            ps.executeUpdate(); //execută instrucțiunea SQL (INSERT)
            try (ResultSet rs = ps.getGeneratedKeys()) {
                //dacă există cel puțin o cheie generată
                if (rs.next()) {
                    d.setIdDepartament(rs.getInt(1)); //id-ul generat de DB
                }
            }
        }
    }

    // READ by ID
    public Department findById(int id) throws SQLException {
        String sql = "SELECT * FROM department WHERE id_departament = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Department(
                            rs.getInt("id_departament"),
                            rs.getString("nume_departament"),
                            rs.getString("facultate")
                    );
                }
            }
        }
        return null;
    }

    // READ all
    public List<Department> findAll() throws SQLException {
        String sql = "SELECT * FROM department";
        List<Department> list = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Department(
                        rs.getInt("id_departament"),
                        rs.getString("nume_departament"),
                        rs.getString("facultate")
                ));
            }
        }
        return list;
    }

    // READ by name
    public Department findByName(String numeDepartament) throws SQLException {
        String sql = "SELECT * FROM department WHERE nume_departament = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numeDepartament);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Department(
                            rs.getInt("id_departament"),
                            rs.getString("nume_departament"),
                            rs.getString("facultate")
                    );
                }
            }
        }
        return null;
    }
}
