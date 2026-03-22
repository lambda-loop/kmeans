import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;

public class RawData {

    public final int d;

    public final ArrayList<RealVector> points;
    public ArrayList<RealVector> centroids;

    public RawData(int d, ArrayList<RealVector> points) {
        this.points = points;
        this.d = d;
    }

    public void updateCentroids() {
        int k             = centroids.size();
        RealVector[] sums = new RealVector[k];
        int[] counts      = new int[k];

        for (int i = 0; i < k; i++) {
            sums[i] = new ArrayRealVector(d);
        }

        for (RealVector point : this.points) {
            int closestIdx = getClosestCentroidIndex(point);
            sums[closestIdx] = sums[closestIdx].add(point);
            counts[closestIdx]++;
        }

        ArrayList<RealVector> newCentroids = new ArrayList<>(k);

        for (int i = 0; i < k; i++) {
            if (counts[i] == 0) {
                newCentroids.add(this.centroids.get(i).copy());
            } else {
                newCentroids.add(sums[i].mapDivide(counts[i]));
            }
        }

        this.centroids = newCentroids;
    }

    private int getClosestCentroidIndex(RealVector point) {
        int bestIdx = 0;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < centroids.size(); i++) {
            double dist = point.getDistance(centroids.get(i));
            if (dist < minDistance) {
                minDistance = dist;
                bestIdx = i;
            }
        }
        return bestIdx;
    }

    public static ArrayList<RealVector> kMeans(int k, int maxIterations, ArrayList<RealVector> points, int d) {
        RawData rawData = initializeRawData(k, points, d);
        return convergeAtMostIn(rawData, maxIterations);
    }

    private static RawData initializeRawData(int k, ArrayList<RealVector> points, int d) {
        RealVector[] sums = new RealVector[k];
        int[] counts = new int[k];

        for (int i = 0; i < k; i++) {
            sums[i] = new ArrayRealVector(d);
        }

        for (int i = 0; i < points.size(); i++) {
            RealVector point = points.get(i);
            int groupIdx = i % k;

            sums[groupIdx] = sums[groupIdx].add(point);
            counts[groupIdx]++;
        }

        ArrayList<RealVector> initialCentroids = new ArrayList<>(k);

        for (int i = 0; i < k; i++) {
            initialCentroids.add(sums[i].mapDivide(counts[i]));
        }

        RawData rawData = new RawData(d, points);
        rawData.centroids = initialCentroids;

        return rawData;
    }

    private static ArrayList<RealVector> convergeAtMostIn(RawData rawData, int maxIterations) {
        for (int i = 0; i < maxIterations; i++) {

            ArrayList<RealVector> oldCentroids = new ArrayList<>(rawData.centroids.size());
            for (RealVector c : rawData.centroids) {
                oldCentroids.add(c.copy());
            }

            rawData.updateCentroids();

            if (oldCentroids.equals(rawData.centroids)) {
                System.out.println("Convergiu antecipadamente na iteração: " + (i + 1));
                break;
            }
        }

        return rawData.centroids;
    }


}