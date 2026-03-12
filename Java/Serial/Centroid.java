
import java.util.Map;
import java.util.Objects;

public class Centroid {
    public Map<String, Double> coordinates;

    public Centroid(Map<String, Double> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "Centroid{" +
                "coordinates=" + coordinates +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Centroid centroid = (Centroid) o;
        return Objects.equals(coordinates, centroid.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates);
    }


}