import java.util.HashSet;
import java.util.Set;

/**
 * Created by naman on 4/15/15.
 */
public class Record {

    Set<Integer> terms;
    Double p;
    boolean b;
    Double cost;
    Record L;
    Record R;

    public Record() {
        this(new HashSet<Integer>(), 0.0, false, 0.0, null, null);
    }

    public Record(Set<Integer> terms) {
        this(terms, 0.0, false, 0.0, null, null);
    }

    public Record(Set<Integer> terms, Double p, boolean b, Double cost, Record l, Record r) {
        this.terms = terms;
        this.p = p;
        this.b = b;
        this.cost = cost;
        L = l;
        R = r;
    }

    public Set<Integer> getTerms() {
        return terms;
    }

    public void setTerms(Set<Integer> terms) {
        this.terms = terms;
    }

    public Double getP() {
        return p;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Record getL() {
        return L;
    }

    public void setL(Record l) {
        L = l;
    }

    public Record getR() {
        return R;
    }

    public void setR(Record r) {
        R = r;
    }
}
