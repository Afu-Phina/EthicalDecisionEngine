package server;

import com.google.gson.Gson;
import controller.EthicalAnalysisController;
import model.AuditTrail;
import model.Decision;
import model.EthicalAnalysisResult;
import io.javalin.Javalin;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Minimal API server exposing the analysis functionality over HTTP.
 * - POST /api/analyze  accepts a JSON `Decision` and returns analysis JSON
 * - GET  /api/health   returns a simple OK string
 */
public class ApiServer {
    public static void main(String[] args) {
        Gson gson = new Gson();
        EthicalAnalysisController controller = new EthicalAnalysisController();

        Javalin app = Javalin.create();

        app.post("/api/analyze", ctx -> {
            Decision decision = gson.fromJson(ctx.body(), Decision.class);
            List<EthicalAnalysisResult> results = controller.analyzeDecision(decision);

            double overall = controller.calculateOverallScore(results);
            String recommendation = controller.generateRecommendation(results);
            String conflict = controller.detectConflicts(results);
            AuditTrail audit = controller.buildAuditTrail(decision, results);

                List<Map<String, Object>> frameworks = results.stream().map(r -> {
                java.util.Map<String, Object> m = new java.util.HashMap<>();
                m.put("framework", r.getFrameworkName());
                m.put("score", r.getScore());
                m.put("explanation", r.getExplanation());
                m.put("risks", r.getRisks());
                m.put("violations", r.getViolations());
                return m;
                }).collect(Collectors.toList());

                java.util.Map<String, Object> auditMap = new java.util.HashMap<>();
                auditMap.put("verdict", audit.getVerdict());
                auditMap.put("conflictSummary", audit.getConflictSummary());
                auditMap.put("remediation", audit.getRemediation());
                auditMap.put("solutionPaths", audit.getSolutionPaths());
                auditMap.put("timestamp", audit.getTimestamp().toString());

                java.util.Map<String, Object> resp = new java.util.HashMap<>();
                resp.put("results", frameworks);
                resp.put("overallScore", overall);
                resp.put("recommendation", recommendation);
                resp.put("conflictAnalysis", conflict);
                resp.put("auditTrail", auditMap);

                ctx.contentType("application/json");
                ctx.result(gson.toJson(resp));
        });

        app.get("/api/health", ctx -> ctx.result("ok"));

        app.start(7000);
        System.out.println("API server started on http://localhost:7000");
    }
}
