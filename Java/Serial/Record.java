
import java.util.Map;
import java.util.Objects;

public class Record {
  public final String desc;
  public final Map<String, Double> feats;

  public Record(String desc, Map<String, Double> feats) {
    this.desc = desc;
    this.feats = feats;
  }

  @Override
  public String toString() {
    return "Record{" +
        "desc='" + desc + '\'' +
        ", feats=" + feats +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Record record = (Record) o;
    return Objects.equals(desc, record.desc) && Objects.equals(feats, record.feats);
  }

  @Override
  public int hashCode() {
    return Objects.hash(desc, feats);
  }

}
