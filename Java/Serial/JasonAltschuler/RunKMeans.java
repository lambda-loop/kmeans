import java.util.Arrays;

public class RunKMeans {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("Uso: java RunKMeans <csv> <rows> <cols> <k> [maxIter]");
            System.exit(1);
        }

        String file = args[0];
        int rows = Integer.parseInt(args[1]);
        int cols = Integer.parseInt(args[2]);
        int k = Integer.parseInt(args[3]);
        int maxIter = (args.length >= 5) ? Integer.parseInt(args[4]) : 100;

        double[][] pts = CSVreader.read(file, rows, cols);

        KMeans km = new KMeans.Builder(k, pts)
                .iterations(maxIter)
                .pp(true)
                .build();

        double[][] centers = km.getCentroids();
        System.out.println("Centroides:");
        for (double[] c : centers) {
            System.out.println(Arrays.toString(c));
        }

        System.out.println("WCSS: " + km.getWCSS());
        System.out.println(km.getTiming());
    }
}
