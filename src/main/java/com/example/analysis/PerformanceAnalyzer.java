package analysis;

import model.MSTResult;
import model.Graph;
import java.util.*;

public class PerformanceAnalyzer {

    public static void printComparison(Graph graph, MSTResult primResult, MSTResult kruskalResult) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║         GRAPH #" + graph.getId() + " - PERFORMANCE COMPARISON          ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        System.out.println("\n📊 Graph Statistics:");
        System.out.println("   Vertices: " + graph.getVertexCount());
        System.out.println("   Edges:    " + graph.getEdgeCount());
        System.out.println("   Density:  " + calculateDensity(graph));

        System.out.println("\n📈 Algorithm Results:");
        System.out.println("┌─────────────┬─────────────┬─────────────┬─────────────┬─────────────┐");
        System.out.println("│  Algorithm  │ Total Cost  │ Operations  │   Time(ms)  │   Winner    │");
        System.out.println("├─────────────┼─────────────┼─────────────┼─────────────┼─────────────┤");

        System.out.printf("│ %-11s │ %-11d │ %-11d │ %-11.2f │ %-11s │%n",
                "Prim",
                primResult.getTotalCost(),
                primResult.getOperationsCount(),
                primResult.getExecutionTimeMs(),
                "");

        System.out.printf("│ %-11s │ %-11d │ %-11d │ %-11.2f │ %-11s │%n",
                "Kruskal",
                kruskalResult.getTotalCost(),
                kruskalResult.getOperationsCount(),
                kruskalResult.getExecutionTimeMs(),
                "");

        System.out.println("└─────────────┴─────────────┴─────────────┴─────────────┴─────────────┘");

        analyzeWinner(primResult, kruskalResult);
        verifyCosts(primResult, kruskalResult);

        System.out.println();
    }

    private static String calculateDensity(Graph graph) {
        int v = graph.getVertexCount();
        int e = graph.getEdgeCount();
        int maxEdges = v * (v - 1) / 2;

        if (maxEdges == 0) return "0%";

        double density = (double) e / maxEdges * 100;
        return String.format("%.1f%%", density);
    }

    private static void analyzeWinner(MSTResult primResult, MSTResult kruskalResult) {
        System.out.println("\n🏆 Performance Analysis:");

        if (primResult.getExecutionTimeMs() < kruskalResult.getExecutionTimeMs()) {
            double improvement = ((kruskalResult.getExecutionTimeMs() - primResult.getExecutionTimeMs())
                    / kruskalResult.getExecutionTimeMs()) * 100;
            System.out.printf("   ⚡ Prim is %.1f%% faster than Kruskal%n", improvement);
        } else if (kruskalResult.getExecutionTimeMs() < primResult.getExecutionTimeMs()) {
            double improvement = ((primResult.getExecutionTimeMs() - kruskalResult.getExecutionTimeMs())
                    / primResult.getExecutionTimeMs()) * 100;
            System.out.printf("   ⚡ Kruskal is %.1f%% faster than Prim%n", improvement);
        } else {
            System.out.println("   ⚖️  Both algorithms have equal execution time");
        }

        if (primResult.getOperationsCount() < kruskalResult.getOperationsCount()) {
            double improvement = ((kruskalResult.getOperationsCount() - primResult.getOperationsCount())
                    / (double) kruskalResult.getOperationsCount()) * 100;
            System.out.printf("   📉 Prim uses %.1f%% fewer operations than Kruskal%n", improvement);
        } else if (kruskalResult.getOperationsCount() < primResult.getOperationsCount()) {
            double improvement = ((primResult.getOperationsCount() - kruskalResult.getOperationsCount())
                    / (double) primResult.getOperationsCount()) * 100;
            System.out.printf("   📉 Kruskal uses %.1f%% fewer operations than Prim%n", improvement);
        } else {
            System.out.println("   ⚖️  Both algorithms use equal number of operations");
        }
    }

    private static void verifyCosts(MSTResult primResult, MSTResult kruskalResult) {
        System.out.println("\n✅ Correctness Verification:");
        if (primResult.getTotalCost() == kruskalResult.getTotalCost()) {
            System.out.println("   ✓ Both algorithms produce the same MST cost: "
                    + primResult.getTotalCost());
            System.out.println("   ✓ Results are CORRECT!");
        } else {
            System.out.println("   ✗ WARNING: Costs differ!");
            System.out.println("   Prim:    " + primResult.getTotalCost());
            System.out.println("   Kruskal: " + kruskalResult.getTotalCost());
        }
    }

    public static void printOverallStatistics(List<Map<String, Object>> allResults) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("OVERALL STATISTICS - ALL GRAPHS");
        System.out.println("=".repeat(60));

        int primWinsByTime = 0;
        int kruskalWinsByTime = 0;
        int primWinsByOps = 0;
        int kruskalWinsByOps = 0;

        for (Map<String, Object> result : allResults) {
            @SuppressWarnings("unchecked")
            Map<String, Object> prim = (Map<String, Object>) result.get("prim");
            @SuppressWarnings("unchecked")
            Map<String, Object> kruskal = (Map<String, Object>) result.get("kruskal");

            double primTime = ((Number) prim.get("execution_time_ms")).doubleValue();
            double kruskalTime = ((Number) kruskal.get("execution_time_ms")).doubleValue();
            long primOps = ((Number) prim.get("operations_count")).longValue();
            long kruskalOps = ((Number) kruskal.get("operations_count")).longValue();

            if (primTime < kruskalTime) primWinsByTime++;
            else if (kruskalTime < primTime) kruskalWinsByTime++;

            if (primOps < kruskalOps) primWinsByOps++;
            else if (kruskalOps < primOps) kruskalWinsByOps++;
        }

        System.out.println("\n🏆 Wins by Execution Time:");
        System.out.println("   Prim:    " + primWinsByTime);
        System.out.println("   Kruskal: " + kruskalWinsByTime);

        System.out.println("\n📊 Wins by Operation Count:");
        System.out.println("   Prim:    " + primWinsByOps);
        System.out.println("   Kruskal: " + kruskalWinsByOps);

        System.out.println("\n" + "=".repeat(60) + "\n");
    }

    public static void printRecommendations() {
        System.out.println("💡 RECOMMENDATIONS:");
        System.out.println("━".repeat(60));
        System.out.println("\n📌 Use PRIM'S Algorithm when:");
        System.out.println("   • Graph is DENSE (many edges)");
        System.out.println("   • Using adjacency list representation");
        System.out.println("   • Need to start from specific vertex");
        System.out.println("   • Working with connected graphs");

        System.out.println("\n📌 Use KRUSKAL'S Algorithm when:");
        System.out.println("   • Graph is SPARSE (few edges)");
        System.out.println("   • Edges are already sorted");
        System.out.println("   • Using edge list representation");
        System.out.println("   • Graph might be disconnected");

        System.out.println("\n" + "━".repeat(60) + "\n");
    }
}