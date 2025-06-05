package Utile;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;

import static Utile.Constante.FILE_AUDIT;

public final class AuditService {
    private static AuditService instance;
    private AuditService() {}

    public static AuditService getInstance() {
        if (instance == null) instance = new AuditService();
        return instance;
    }

    public void log(String actiune) {
        try {
            Path path = Paths.get(FILE_AUDIT);
            Files.createDirectories(path.getParent());

            try (FileWriter fw = new FileWriter(FILE_AUDIT, true)) {
                fw.write(actiune + "," + LocalDateTime.now() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Eroare audit: " + e.getMessage());
        }
    }
}
