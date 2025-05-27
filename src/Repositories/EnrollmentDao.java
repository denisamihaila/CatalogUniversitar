package Repositories;

import Entitati.Enrollment;
import Entitati.Grade;
import Entitati.Student;
import Entitati.CourseOffering;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDao {
    private final Connection conn = DBConnection.getInstance().getConnection();

    // CREATE
    public int insert(Enrollment e) throws SQLException {
        String sql = "INSERT INTO enrollment (id_student, id_oferta, data_inscrierii) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, e.getStudent().getId());
            ps.setInt(2, e.getOferta().getIdOferta());
            ps.setDate(3, Date.valueOf(e.getDataInscrierii()));
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Inserare enrollment eșuată, niciun rând afectat.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Inserare enrollment eșuată, nu s-a obținut ID-ul.");
                }
            }
        }
    }

    // READ all
    public List<Enrollment> findAll() throws SQLException {
        String sql = "SELECT * FROM enrollment";
        List<Enrollment> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            StudentDao studentDao = new StudentDao();
            CourseOfferingDao offeringDao = new CourseOfferingDao();
            while (rs.next()) {
                int id = rs.getInt("id_inscriere");
                Student s = studentDao.findById(rs.getInt("id_student"));
                CourseOffering o = offeringDao.findById(rs.getInt("id_oferta"));
                LocalDate date = rs.getDate("data_inscrierii").toLocalDate();
                list.add(new Enrollment(id, s, o, date));
            }
        }
        return list;
    }

    // UPDATE
    public void update(Enrollment e) throws SQLException {
        String sql = "UPDATE enrollment SET id_student=?, id_oferta=?, data_inscrierii=? WHERE id_inscriere=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, e.getStudent().getId());
            ps.setInt(2, e.getOferta().getIdOferta());
            ps.setDate(3, Date.valueOf(e.getDataInscrierii()));
            ps.setInt(4, e.getIdInscriere());
            ps.executeUpdate();
        }
    }


    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM enrollment WHERE id_inscriere = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // CREATE
    public int insertGrade(int idInscriere, double valoareNota) throws SQLException {
        String sql = "INSERT INTO grade (id_inscriere, valoare_nota) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idInscriere);
            ps.setDouble(2, valoareNota);
            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new SQLException("Inserare grade eșuată, niciun rând afectat.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Inserare grade eșuată, nu s-a obținut ID-ul notei.");
                }
            }
        }
    }

    public List<Grade> findGradesByStudentId(int idStudent) throws SQLException {
        String sql = """
        SELECT g.id_nota, g.id_inscriere, g.valoare_nota
        FROM grade g
        JOIN enrollment e ON g.id_inscriere = e.id_inscriere
        WHERE e.id_student = ?
    """;
        List<Grade> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idStudent);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idNota = rs.getInt("id_nota");
                    int idIns = rs.getInt("id_inscriere");
                    double val = rs.getDouble("valoare_nota");
                    Enrollment e = findById(idIns);
                    list.add(new Grade(idNota, e, val));
                }
            }
        }
        return list;
    }

    public Integer findIdByStudentCourseDate(String numeStudent,
                                             String codMaterie,
                                             int semestru,
                                             int an,
                                             LocalDate dataInscrierii)
            throws SQLException {
        String sql = "SELECT e.id_inscriere " +
                "FROM enrollment e " +
                "JOIN student s ON e.id_student = s.id_student " +
                "JOIN course_offering o ON e.id_oferta = o.id_oferta " +
                "WHERE s.nume = ? AND o.cod_materie = ? " +
                "AND o.semestru = ? AND o.an = ? AND e.data_inscrierii = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numeStudent);
            ps.setString(2, codMaterie);
            ps.setInt(3, semestru);
            ps.setInt(4, an);
            ps.setDate(5, Date.valueOf(dataInscrierii));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_inscriere");
                }
            }
        }
        return null;
    }

    public Enrollment findById(int id) throws SQLException {
        String sql = "SELECT * FROM enrollment WHERE id_inscriere = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Obține student și ofertă prin DAO-urile lor
                    StudentDao studentDao = new StudentDao();
                    CourseOfferingDao offeringDao = new CourseOfferingDao();
                    int studentId = rs.getInt("id_student");
                    int ofertaId  = rs.getInt("id_oferta");
                    LocalDate date = rs.getDate("data_inscrierii").toLocalDate();

                    Student s = studentDao.findById(studentId);
                    CourseOffering o = offeringDao.findById(ofertaId);

                    return new Enrollment(id, s, o, date);
                }
            }
        }
        return null;
    }

}
