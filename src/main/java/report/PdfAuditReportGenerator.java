package report;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import model.AuditTrail;
import model.EthicalAnalysisResult;

/**
 * PdfAuditReportGenerator creates professional, corporate-style PDF audit reports.
 *
 * Purpose: Generates formatted PDF reports for ethics audits with score cards,
 * verdict analysis, conflict detection, and recommendations.
 */
public class PdfAuditReportGenerator {
    private static final Color ACCENT_BLUE = new DeviceRgb(59, 130, 246);
    private static final Color SUCCESS_GREEN = new DeviceRgb(34, 197, 94);
    private static final Color WARNING_ORANGE = new DeviceRgb(249, 115, 22);
    private static final Color DANGER_RED = new DeviceRgb(239, 68, 68);
    private static final Color DIALOG_BG = new DeviceRgb(17, 24, 39);

    public static void generateReport(AuditTrail auditTrail, String filePath) throws IOException {
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDoc);
        document.setMargins(40, 40, 50, 40);

        addTitleSection(document, auditTrail);
        addDecisionSummary(document, auditTrail);
        addEthicalScoresTable(document, auditTrail);
        addConflictAnalysis(document, auditTrail);
        addVerdict(document, auditTrail);
        addRecommendations(document, auditTrail);
        addFooter(document);

        document.close();
    }

    private static void addTitleSection(Document document, AuditTrail auditTrail) {
        PdfFont titleFont = font(StandardFonts.HELVETICA_BOLD);
        PdfFont bodyFont = font(StandardFonts.HELVETICA);

        document.add(new Paragraph("Ethical Decision Analysis Report")
                .setFont(titleFont)
                .setFontSize(26)
                .setFontColor(ACCENT_BLUE)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(8));

        document.add(new Paragraph("Professional ethics audit with framework scoring and guidance.")
                .setFont(bodyFont)
                .setFontSize(12)
                .setFontColor(ColorConstants.GRAY)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(14));

        String timestamp = auditTrail.getTimestamp().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' HH:mm:ss"));
        document.add(new Paragraph("Generated: " + timestamp)
                .setFont(bodyFont)
                .setFontSize(10)
                .setFontColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20));

        document.add(createSectionDivider());
    }

    private static void addDecisionSummary(Document document, AuditTrail auditTrail) {
        PdfFont headingFont = font(StandardFonts.HELVETICA_BOLD);
        PdfFont labelFont = font(StandardFonts.HELVETICA_BOLD);
        PdfFont valueFont = font(StandardFonts.HELVETICA);

        document.add(new Paragraph("Decision Summary")
                .setFont(headingFont)
                .setFontSize(14)
                .setFontColor(ACCENT_BLUE)
                .setMarginTop(18)
                .setMarginBottom(10));

        Table table = new Table(new float[]{2f, 4f}).useAllAvailableWidth();
        addKeyValueRow(table, "Decision:", auditTrail.getDecision().getDescription(), labelFont, valueFont);
        addKeyValueRow(table, "Context:", auditTrail.getDecision().getContext(), labelFont, valueFont);
        addKeyValueRow(table, "Stakeholders:", auditTrail.getDecision().getStakeholders(), labelFont, valueFont);
        addKeyValueRow(table, "Associated Risks:", auditTrail.getDecision().getRisks(), labelFont, valueFont);
        addKeyValueRow(table, "Applicable Policies:", auditTrail.getDecision().getApplicablePolicies(), labelFont, valueFont);

        document.add(table);
    }

    private static void addEthicalScoresTable(Document document, AuditTrail auditTrail) {
        PdfFont headingFont = font(StandardFonts.HELVETICA_BOLD);
        PdfFont bodyFont = font(StandardFonts.HELVETICA);

        document.add(new Paragraph("Ethical Frameworks Analysis")
                .setFont(headingFont)
                .setFontSize(14)
                .setFontColor(ACCENT_BLUE)
                .setMarginTop(18)
                .setMarginBottom(10));

        Table table = new Table(new float[]{2f, 1f, 3f, 3f}).useAllAvailableWidth();
        addTableHeader(table, "Framework", bodyFont);
        addTableHeader(table, "Score", bodyFont);
        addTableHeader(table, "Explanation", bodyFont);
        addTableHeader(table, "Key Risks", bodyFont);

        for (EthicalAnalysisResult result : auditTrail.getResults()) {
            addTableCell(table, result.getFrameworkName(), bodyFont, DIALOG_BG);
            addTableCell(table, String.format("%.1f/10", result.getScore()), bodyFont, scoreBackground(result.getScore()));
            addTableCell(table, result.getExplanation(), bodyFont, DIALOG_BG);
            addTableCell(table, result.getRisks(), bodyFont, DIALOG_BG);
        }

        document.add(table);
    }

    private static void addConflictAnalysis(Document document, AuditTrail auditTrail) {
        PdfFont headingFont = font(StandardFonts.HELVETICA_BOLD);
        PdfFont bodyFont = font(StandardFonts.HELVETICA);

        document.add(new Paragraph("Conflict Detection & Analysis")
                .setFont(headingFont)
                .setFontSize(14)
                .setFontColor(ACCENT_BLUE)
                .setMarginTop(18)
                .setMarginBottom(10));

        document.add(new Paragraph(auditTrail.getConflictSummary())
                .setFont(bodyFont)
                .setFontSize(11)
                .setFontColor(ColorConstants.WHITE)
                .setMarginBottom(12));
    }

    private static void addVerdict(Document document, AuditTrail auditTrail) {
        PdfFont headingFont = font(StandardFonts.HELVETICA_BOLD);
        PdfFont bodyFont = font(StandardFonts.HELVETICA);

        document.add(new Paragraph("Final Verdict")
                .setFont(headingFont)
                .setFontSize(14)
                .setFontColor(ACCENT_BLUE)
                .setMarginTop(18)
                .setMarginBottom(10));

        document.add(new Paragraph(auditTrail.getVerdict())
                .setFont(headingFont)
                .setFontSize(12)
                .setFontColor(ColorConstants.WHITE)
                .setBackgroundColor(getVerdictColor(auditTrail.getVerdict()))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(10)
                .setMarginBottom(10));

        document.add(new Paragraph("Remediation Guidance:")
                .setFont(headingFont)
                .setFontSize(12)
                .setFontColor(ACCENT_BLUE)
                .setMarginBottom(6));

        document.add(new Paragraph(auditTrail.getRemediation())
                .setFont(bodyFont)
                .setFontSize(11)
                .setFontColor(ColorConstants.WHITE)
                .setMarginBottom(12));
    }

    private static void addRecommendations(Document document, AuditTrail auditTrail) {
        PdfFont headingFont = font(StandardFonts.HELVETICA_BOLD);
        PdfFont bodyFont = font(StandardFonts.HELVETICA);

        document.add(new Paragraph("Recommended Actions")
                .setFont(headingFont)
                .setFontSize(14)
                .setFontColor(ACCENT_BLUE)
                .setMarginTop(18)
                .setMarginBottom(10));

        List<String> solutions = auditTrail.getSolutionPaths();
        if (solutions == null || solutions.isEmpty()) {
            document.add(new Paragraph("No recommendations available.")
                    .setFont(bodyFont)
                    .setFontSize(11)
                    .setFontColor(ColorConstants.WHITE));
            return;
        }

        for (String solution : solutions) {
            document.add(new Paragraph("• " + solution)
                    .setFont(bodyFont)
                    .setFontSize(11)
                    .setFontColor(ColorConstants.WHITE)
                    .setMarginLeft(12)
                    .setMarginBottom(6));
        }
    }

    private static void addFooter(Document document) {
        PdfFont footerFont = font(StandardFonts.HELVETICA);

        document.add(createSectionDivider());
        document.add(new Paragraph("This audit report was generated by the Ethical Decision Engine. "
                + "All analyses are confidential and intended for compliance purposes only.")
                .setFont(footerFont)
                .setFontSize(9)
                .setFontColor(ColorConstants.GRAY)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(16));
    }

    private static void addKeyValueRow(Table table, String label, String value, PdfFont labelFont, PdfFont valueFont) {
        table.addCell(new Cell().add(new Paragraph(label).setFont(labelFont).setFontSize(11)).setBackgroundColor(DIALOG_BG).setPadding(8));
        table.addCell(new Cell().add(new Paragraph(value == null || value.isBlank() ? "—" : value).setFont(valueFont).setFontSize(11)).setBackgroundColor(new DeviceRgb(12, 17, 29)).setPadding(8));
    }

    private static void addTableHeader(Table table, String text, PdfFont font) {
        table.addCell(new Cell().add(new Paragraph(text).setFont(font).setFontSize(11).setFontColor(ColorConstants.WHITE)).setBackgroundColor(ACCENT_BLUE).setPadding(10).setTextAlignment(TextAlignment.CENTER));
    }

    private static void addTableCell(Table table, String text, PdfFont font, Color background) {
        table.addCell(new Cell().add(new Paragraph(text == null || text.isBlank() ? "—" : text).setFont(font).setFontSize(10).setFontColor(ColorConstants.WHITE)).setBackgroundColor(background).setPadding(10));
    }

    private static PdfFont font(String standardFont) {
        try {
            return PdfFontFactory.createFont(standardFont);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load PDF font: " + standardFont, e);
        }
    }

    private static Color scoreBackground(double score) {
        if (score >= 8.0) {
            return SUCCESS_GREEN;
        }
        if (score >= 5.0) {
            return WARNING_ORANGE;
        }
        return DANGER_RED;
    }

    private static Color getVerdictColor(String verdict) {
        if (verdict == null || verdict.isBlank()) {
            return new DeviceRgb(75, 85, 99);
        }
        String normalized = verdict.toLowerCase();
        if (normalized.contains("approved") || normalized.contains("defensible") || normalized.contains("acceptable")) {
            return SUCCESS_GREEN;
        }
        if (normalized.contains("conditions") || normalized.contains("caution")) {
            return WARNING_ORANGE;
        }
        return DANGER_RED;
    }

    private static Paragraph createSectionDivider() {
        return new Paragraph("\n").setMarginTop(6).setMarginBottom(18);
    }
}
