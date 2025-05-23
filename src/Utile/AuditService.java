package Utile;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;

public final class AuditService {
    private static final String FILE = "Resources/audit.csv";
    private static AuditService instance;
    private AuditService() {}

    public static AuditService getInstance() {
        if (instance == null) instance = new AuditService();
        return instance;
    }

    public void log(String actiune) {
        try {
            Path path = Paths.get(FILE);
            Files.createDirectories(path.getParent());

            try (FileWriter fw = new FileWriter(FILE, true)) {
                fw.write(actiune + "," + LocalDateTime.now() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Eroare audit: " + e.getMessage());
        }
    }
}
