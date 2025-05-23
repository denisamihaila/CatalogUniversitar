package Repositories;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DBInitializer {
    private DBInitializer() {}

    public static void initialize() {
        Connection conn = DBConnection.getInstance().getConnection();
        try (Statement st = conn.createStatement()) {

            st.execute("DROP TABLE IF EXISTS grade");
            st.execute("DROP TABLE IF EXISTS enrollment");
            st.execute("DROP TABLE IF EXISTS course_offering");
            st.execute("DROP TABLE IF EXISTS professor");
            st.execute("DROP TABLE IF EXISTS student");
            st.execute("DROP TABLE IF EXISTS course");
            st.execute("DROP TABLE IF EXISTS department");

            st.execute("CREATE TABLE department ("
                    + "id_departament INT PRIMARY KEY AUTO_INCREMENT,"
                    + "nume_departament VARCHAR(100) NOT NULL,"
                    + "facultate VARCHAR(100) NOT NULL) ENGINE=InnoDB");

            st.execute("CREATE TABLE course ("
                    + "cod_materie VARCHAR(10) PRIMARY KEY,"
                    + "denumire VARCHAR(100) NOT NULL,"
                    + "credite INT NOT NULL,"
                    + "id_departament INT,"
                    + "FOREIGN KEY(id_departament) REFERENCES department(id_departament)"
                    + " ON DELETE SET NULL ON UPDATE CASCADE) ENGINE=InnoDB");

            st.execute("CREATE TABLE professor ("
                    + "id_profesor INT PRIMARY KEY AUTO_INCREMENT,"
                    + "nume VARCHAR(100) NOT NULL,"
                    + "titlu_didactic VARCHAR(50),"
                    + "id_departament INT,"
                    + "FOREIGN KEY(id_departament) REFERENCES department(id_departament)"
                    + " ON DELETE SET NULL ON UPDATE CASCADE) ENGINE=InnoDB");

            st.execute("CREATE TABLE student ("
                    + "id_student INT PRIMARY KEY AUTO_INCREMENT,"
                    + "nume VARCHAR(100) NOT NULL,"
                    + "data_nasterii DATE NOT NULL,"
                    + "specializare VARCHAR(100) NOT NULL) ENGINE=InnoDB");

            st.execute("CREATE TABLE course_offering ("
                    + "id_oferta INT PRIMARY KEY AUTO_INCREMENT,"
                    + "cod_materie VARCHAR(10) NOT NULL,"
                    + "semestru TINYINT NOT NULL,"
                    + "an SMALLINT NOT NULL,"
                    + "id_profesor INT,"
                    + "FOREIGN KEY(cod_materie) REFERENCES course(cod_materie)"
                    + " ON DELETE CASCADE ON UPDATE CASCADE,"
                    + "FOREIGN KEY(id_profesor) REFERENCES professor(id_profesor)"
                    + " ON DELETE SET NULL ON UPDATE CASCADE) ENGINE=InnoDB");

            st.execute("CREATE TABLE enrollment ("
                    + "id_inscriere INT PRIMARY KEY AUTO_INCREMENT,"
                    + "id_student INT NOT NULL,"
                    + "id_oferta INT NOT NULL,"
                    + "data_inscrierii DATE NOT NULL,"
                    + "FOREIGN KEY(id_student) REFERENCES student(id_student)"
                    + " ON DELETE CASCADE ON UPDATE CASCADE,"
                    + "FOREIGN KEY(id_oferta) REFERENCES course_offering(id_oferta)"
                    + " ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB");

            st.execute("CREATE TABLE grade ("
                    + "id_nota INT PRIMARY KEY AUTO_INCREMENT,"
                    + "id_inscriere INT NOT NULL,"
                    + "valoare_nota DOUBLE NOT NULL,"
                    + "FOREIGN KEY(id_inscriere) REFERENCES enrollment(id_inscriere)"
                    + " ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB");

        } catch (SQLException e) {
            throw new RuntimeException("Eroare la initializarea bazei de date", e);
        }
    }
}
