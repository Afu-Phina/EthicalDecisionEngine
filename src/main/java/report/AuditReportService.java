package report;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import model.AuditTrail;

/**
 * AuditReportService manages the lifecycle of audit report generation and storage.
 * 
 * Purpose: Handles file system operations, timestamped naming, and report export.
 * 
 * Design Pattern: Service pattern - high-level reporting interface.
 */
// handles exporting audit reports and managing the report folder.
public class AuditReportService {
    private static final String REPORTS_DIR = "reports";
    private static final String FILENAME_PATTERN = "Ethics_Report_%s.pdf";

    /**
     * Exports an audit trail as a professional PDF report with timestamp.
     * 
     * @param auditTrail The audit trail to export
     * @return The full path to the generated PDF file
     * @throws IOException if file creation fails
     */
    public static String exportAuditReport(AuditTrail auditTrail) throws IOException {
        ensureReportsDirectory();
        String filename = generateTimestampedFilename();
        String filepath = REPORTS_DIR + File.separator + filename;

        PdfAuditReportGenerator.generateReport(auditTrail, filepath);
// Return this value to the method caller so the result can be used elsewhere.
        return filepath;
    }

    /**
     * Exports an audit report to a user-selected file path.
     * 
     * @param auditTrail The audit trail to export
     * @param filepath The destination file path
     * @throws IOException if file creation fails
     */
    public static void exportAuditReportToPath(AuditTrail auditTrail, String filepath) throws IOException {
        PdfAuditReportGenerator.generateReport(auditTrail, filepath);
    }

    private static void ensureReportsDirectory() {
        File dir = new File(REPORTS_DIR);
// If the condition inside the parentheses is true, the code inside the block will run.
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private static String generateTimestampedFilename() {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
// Return this value to the method caller so the result can be used elsewhere.
        return String.format(FILENAME_PATTERN, timestamp);
    }
}
