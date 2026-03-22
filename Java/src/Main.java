import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    // --- Configurações do K-Means ---
    private static final int K = 3;                  // Número de clusters
    private static final int D = 2;                  // Dimensão dos vetores (ex: 2 para X,Y)
    private static final int MAX_ITERS = 100;        // Limite de iterações
    private static final String FILE_PATH = "../simple.csv"; // Caminho do arquivo

    public static void main(String[] args) {
        try {
            System.out.println("Lendo o arquivo: " + FILE_PATH);

            // Fazendo o parse do CSV usando as constantes
            ArrayList<RealVector> points = readCSV(FILE_PATH, D);

            System.out.println("Arquivo lido com sucesso. Total de pontos: " + points.size());
            System.out.println("Executando K-Means com K=" + K + ", D=" + D + ", Max Iters=" + MAX_ITERS + "...");

            // Chamando o método kMeans da sua classe RawData
            ArrayList<RealVector> centroids = RawData.kMeans(K, MAX_ITERS, points, D);

            // Imprimindo os resultados
            System.out.println("\n--- Centróides Finais ---");
            for (int i = 0; i < centroids.size(); i++) {
                System.out.println("Centróide " + (i + 1) + ": " + centroids.get(i));
            }

        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Erro durante o processamento: " + e.getMessage());
        }
    }

    /**
     * Lê um arquivo CSV e converte suas linhas em RealVectors.
     */
    private static ArrayList<RealVector> readCSV(String filePath, int expectedDim) throws IOException {
        ArrayList<RealVector> points = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                // Ignora linhas vazias
                if (line.isEmpty()) continue;

                String[] fields = line.split(",");

                // Verifica a dimensão
                if (fields.length != expectedDim) {
                    throw new IllegalArgumentException(String.format(
                            "Linha %d inválida: esperado %d colunas, mas recebi %d",
                            lineNumber, expectedDim, fields.length));
                }

                // Converte as strings para double
                double[] pointValues = new double[expectedDim];
                for (int i = 0; i < expectedDim; i++) {
                    pointValues[i] = Double.parseDouble(fields[i].trim());
                }

                points.add(new ArrayRealVector(pointValues));
            }
        }

        return points;
    }
}