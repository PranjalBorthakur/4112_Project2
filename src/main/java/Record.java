import java.util.HashSet;
import java.util.Set;

/**
 * Created by naman on 4/15/15.
 */
public class Record {

    Set<Integer> terms;
    Double p;
    // no branch implies b = true
    boolean b;
    Double cost;
    Double fCost;
    Double cCost;
    Set<Integer> L;
    Set<Integer> R;

    public Record() {
        this(new HashSet<Integer>(), 0.0, false, 0.0, 0.0, 0.0, null, null);
    }

    public Record(Set<Integer> terms) {
        this(terms, 0.0, false, 0.0, 0.0, 0.0, null, null);
    }

    public Record(Set<Integer> terms, Double p, boolean b, Double cost, Double fCost, Double cCost,
                  Set<Integer> l, Set<Integer> r) {
        this.terms = terms;
        this.p = p;
        this.b = b;
        this.cost = cost;
        this.fCost = fCost;
        this.cCost = cCost;
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

    public Double getFCost() {
        return fCost;
    }

    public void setFCost(Double fCost) {
        this.fCost = fCost;
    }

    public Double getCCost() {
        return cCost;
    }

    public void setCCost(Double cCost) {
        this.cCost = cCost;
    }

    public Set<Integer> getL() {
        return L;
    }

    public void setL(Set<Integer> l) {
        L = l;
    }

    public Set<Integer> getR() {
        return R;
    }

    public void setR(Set<Integer> r) {
        R = r;
    }
}
