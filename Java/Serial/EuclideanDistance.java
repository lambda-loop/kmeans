
import java.util.Map;

public class EuclideanDistance implements Distance {
    @Override
    public double calc(
            Map<String, Double> f1,
            Map<String, Double> f2) {
        double sum = 0;
        for (String i : f1.keySet()) {
            Double v1 = f1.get(i);
            Double v2 = f2.get(i);

            if (v1 != null && v2 != null) {
                sum += Math.pow(v1 - v2, 2);
            }
        }

        return Math.sqrt(sum);
    }
}
