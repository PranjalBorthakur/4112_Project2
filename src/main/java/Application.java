/**
 * Created by naman on 4/15/15.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Application {

    public static void main(String[] args) {

        // validate that program takes exactly two parameters
        if(args.length != 2) {
            System.out.println("Expects exactly two parameters: query.txt config.txt");
            System.out.println("Instead got: " + Arrays.toString(args));
            System.exit(1);
        }

        // read config file and initialize config variables
        Properties properties = new Properties();
        int r=0,t=0,l=0,m=0,a=0,f=0;
        try {
            properties.load(new FileInputStream(args[1]));
            r = Integer.parseInt(properties.getProperty("r"));
            t = Integer.parseInt(properties.getProperty("t"));
            l = Integer.parseInt(properties.getProperty("l"));
            m = Integer.parseInt(properties.getProperty("m"));
            a = Integer.parseInt(properties.getProperty("a"));
            f = Integer.parseInt(properties.getProperty("f"));
        } catch (Exception e) {
            System.out.println("Error in reading config file");
            e.printStackTrace();
            System.exit(2);
        }

        // open query file
        Scanner in = null;
        try {
            in = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            System.out.println("Error in reading query file");
            e.printStackTrace();
            System.exit(3);
        }

        Comparator<Set<Integer>> comparator = getComparator();
        // parse queries and call DP algorithm
        while(in.hasNext()) {
            String line = in.nextLine();
            String[] splits = line.split("\\s+");
            int k = splits.length;
            float selectivity[] = new float[k+1];
            for(int i = 0; i < k; i++) {
                selectivity[i+1] = Float.valueOf(splits[i]);
            }
            Map<Set<Integer>, Record> A = findOptimalPlan(selectivity, r, t, l, m, a, f, comparator);
            printOptimalPlan(A, k);
        }
    }

    // finds the optimal plan for the given selectivity and config
    private static Map<Set<Integer>, Record> findOptimalPlan(float selectivity[], int r, int t, int l, int m, int a,
                                                             int f, Comparator<Set<Integer>> comparator) {
        Map<Set<Integer>, Record> A = getArray(selectivity, comparator, r, t, l, m, a, f);

        for(Map.Entry<Set<Integer>, Record> s: A.entrySet()) {
            for (Map.Entry<Set<Integer>, Record> sDash : A.entrySet()) {
                // checks if the sets are disjoint
                if(!Collections.disjoint(s.getKey(), sDash.getKey())) {
                    continue;
                }
                // checks for the lemma conditions
                Record E1 = sDash.getValue();
                Record E2 = s.getValue();
//                System.out.println(s);
//                System.out.println(sDash);
//                System.out.println("");
                if(isFirstLemma(A, E1, E2) || isSecondLemma(A, E1, E2)) {
                    continue;
                }
                // recompute
                Double newCost = calculateNewCost(E1, E2, m);
                Set<Integer> union = new HashSet<Integer>(s.getKey());
                union.addAll(sDash.getKey());
                Record record = A.get(union);
                if(newCost < record.getCost()) {
                    record.setCost(newCost);
                    record.setL(sDash.getKey());
                    record.setR(s.getKey());
                }
            }
        }
        return A;
    }

    private static void printOptimalPlan(Map<Set<Integer>, Record> A, int k) {
        Set<Integer> finalKey = new HashSet<Integer>();
        for(int i = 1; i <= k; i++) {
            finalKey.add(i);
        }
        StringBuilder result = new StringBuilder();
        int closeBracketCount = 0;
        result.append("if(");
        closeBracketCount++;
        Record record = A.get(finalKey);
        System.out.println(record.getCost());
        boolean first = true;
        while(record != null) {
            if(record.getL() != null) {
                if(!first) {
                    result.append(" && (");
                    closeBracketCount++;
                }
                result.append(getLogicalAndString(record.getL()));
                record = A.get(record.getR());
            } else {
                if(!record.isB()) {
                    result.append(getLogicalAndString(record.getTerms()));
                }
                for(int i = 0; i < closeBracketCount; i++) {
                    result.append(")");
                }
                result.append(" { \n");
                if(record.isB()) {
                    result.append("    answer[j] = i; \n");
                    result.append("    j += ");
                    result.append(getLogicalAndString(record.getTerms()));
                    result.append("; \n");
                } else {
                    result.append("    answer[j++] = i; \n");
                }
                result.append("}");
                record = null;
            }
            if(first) {
                first = false;
            }
        }
        System.out.println(result.toString());
    }

    private static String getLogicalAndString(Set<Integer> set) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        Iterator<Integer> iter = set.iterator();
        int val;
        while(iter.hasNext()) {
            val = iter.next();
            builder.append("t");
            builder.append(val);
            builder.append("[o");
            builder.append(val);
            builder.append("[i]]");
            if(iter.hasNext()) {
                builder.append(" & ");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    private static Double calculateNewCost(Record E, Record E1, int m) {
        Double cost = 0.0;
        cost += E.getFCost();
        cost += m * Math.min(E.getP(), 1-E.getP());
        cost += E.getP() * E1.getCost();
        return cost;
    }

    // returns if first lemma holds true. If holds true, plan cannot be optimal
    private static boolean isFirstLemma(Map<Set<Integer>, Record> A, Record E1, Record E2) {
        if(E2.getL() != null) {
            E2 = A.get(E2.getL());
        }
        if(E2.getP() <= E1.getP()) {
            if(E2.cCost < E1.cCost) {
                return true;
            }
        }
        return false;
    }

    // returns if second lemma holds true. If holds true, plan cannot be optimal
    private static boolean isSecondLemma(Map<Set<Integer>, Record> A, Record E1, Record E2) {
        if(E1.getP() > 0.5) {
            return false;
        }
        Record tmp;
        while(E2 != null) {
            if(E2.getL() != null) {
                tmp = A.get(E2.getL());
            } else {
                tmp = E2;
            }
            if(tmp.getP() < E1.getP()) {
                if(tmp.getFCost() < E1.getFCost()) {
                    return true;
                }
            }
            E2 = A.get(E2.getR());
        }
        return false;
    }

    private static Map<Set<Integer>, Record> getArray(float[] selectivity, Comparator<Set<Integer>> comparator,
                                                      int r, int t, int l, int m, int a, int f) {
        Map<Set<Integer>, Record> A =  new TreeMap<Set<Integer>, Record>(comparator);
//        Map<Set<Integer>, Record> A =  new TreeMap<Set<Integer>, Record>();

        // populate sets in array
        for(int i = 1; i <= selectivity.length; i++) {
            populateArray(A, selectivity.length, i, 1, new HashSet<Integer>(), 0);
        }

        // initialize each record in the array
        for(Map.Entry<Set<Integer>, Record> entry: A.entrySet()) {
            initializeRecord(entry.getValue(), selectivity, r, t, l, m, a, f);
        }
        return A;
    }

    private static void initializeRecord(Record record, float selectivity[], int r, int t, int l, int m, int a, int f) {
        Set<Integer> terms = record.getTerms();
        int k = terms.size();
        //initialize P value
        Double p = 1.0;
        for(Integer i: terms) {
            p = p*selectivity[i];
        }
        record.setP(p);
        // calculate cost and whether branch
        Double q = Math.min(p, 1-p);
        Double cost1 = k*r + (k-1)*l + k*f + t + m*q + p*a;
        Double cost2 = (double) (k*r + (k-1)*l + k*f + a);
        if(cost1 < cost2) {
            record.setCost(cost1);
            record.setB(false);
        } else {
            record.setCost(cost2);
            record.setB(true);
        }
        Double fCost = (double) (k * r + (k - 1) * l + k * f + t);
        record.setFCost(fCost);
        Double cCost = (p-1)/fCost;
        record.setCCost(cCost);
    }

    // creates all ^kC_{requireSize} combinations and populate them in order in TreeMap A
    private static void populateArray(Map<Set<Integer>, Record> A, int k, int requiredSize, int index,
                                      Set<Integer> result, int len) {
        if(len == requiredSize) {
            A.put(result, new Record(result));
            return;
        }
        if(index >= k) {
            return;
        }
        populateArray(A, k, requiredSize, index+1, result, len);
        Set<Integer> newSet = new HashSet<Integer>();
        newSet.addAll(result);
        newSet.add(index);
        populateArray(A, k, requiredSize, index+1, newSet, len+1);
    }

    // implementing comparable for set
    private static Comparator<Set<Integer>> getComparator() {
        return new Comparator<Set<Integer>>() {
            @Override
            public int compare(Set<Integer> o1, Set<Integer> o2) {
                if(o1 == null && o2 == null) {
                    return 0;
                }
                if(o1 == null || o2 == null) {
                    return 1;
                }
                if(o1.equals(o2)) {
                    return 0;
                } else {
                    if(o1.size() == o2.size()) {
                        Iterator<Integer> iter1 = o1.iterator();
                        Iterator<Integer> iter2 = o2.iterator();
                        while (iter1.hasNext() && iter2.hasNext()) {
                            int int1 = iter1.next();
                            int int2 = iter2.next();
                            if (int1 < int2) {
                                return -1;
                            }
                            if (int1 > int2) {
                                return 1;
                            }
                        }
                    } else {
                        if(o1.size() < o2.size()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                    return 0;
                }
            }
        };
    }

}
