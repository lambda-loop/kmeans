import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // Equivalente ao parseVs e readCSVs combinados
    public static ArrayList<RealVector> readCSVs(String filePath, int d) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        ArrayList<RealVector> points = new ArrayList<>(lines.size());

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue; // Ignora linhas em branco

            String[] fields = line.split(",");

            // Equivalente ao check "length xs == expectedDim"
            if (fields.length != d) {
                throw new IllegalArgumentException(
                        "Linha inválida na posição " + (i + 1) + ": esperado " + d + " colunas, mas recebi " + fields.length
                );
            }

            double[] values = new double[d];
            for (int j = 0; j < d; j++) {
                try {
                    // Equivalente ao "TR.double . T.strip"
                    values[j] = Double.parseDouble(fields[j].trim());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Linha inválida: parse numérico falhou na linha " + (i + 1));
                }
            }
            points.add(new ArrayRealVector(values));
        }

        return points;
    }

    public static void main(String[] args) {
        // Pattern matching do args
        if (args.length != 4) {
            System.out.println("Use: java Main <k> <d> <max_iters> <arquivo.csv>");
            System.exit(1);
        }

        try {
            // let k = read kStr, dVal = read dStr...
            int k = Integer.parseInt(args[0]);
            int d = Integer.parseInt(args[1]);
            int maxIters = Integer.parseInt(args[2]);
            String filePath = args[3];

            // points <- readCSVs @d file
            ArrayList<RealVector> points = readCSVs(filePath, d);

            // print (kMeans k max_iters ...)
            // Assumindo que o método kMeans está na classe KMeansRunner que criamos antes
            ArrayList<RealVector> finalCentroids = KMeansRunner.kMeans(k, maxIters, points, d);

            System.out.println("Centroides Finais:");
            for (RealVector centroid : finalCentroids) {
                System.out.println(centroid.toString());
            }

        } catch (NumberFormatException e) {
            System.err.println("Erro: k, d e max_iters devem ser números inteiros válidos.");
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage());
        }
    }
}
