/**
 * Created by naman on 4/15/15.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
            float selectivity[] = new float[k];
            for(int i = 0; i < k; i++) {
                selectivity[i] = Float.valueOf(splits[i]);
            }
            findOptimalPlan(selectivity, r, t, l, m, a, f, comparator);
        }
    }

    // finds the optimal plan for the given selectivity and config
    private static void findOptimalPlan(float selectivity[], int r, int t, int l, int m, int a, int f,
                                        Comparator<Set<Integer>> comparator) {
        Map<Set<Integer>, Record> A = getArray(selectivity, comparator);

        for(Map.Entry<Set<Integer>, Record> s: A.entrySet()) {
            for (Map.Entry<Set<Integer>, Record> sDash : A.entrySet()) {
                if(!Collections.disjoint(s.getKey(), sDash.getKey())) {
                    continue;
                }
                // check first lemma
                // check second lemma
                // recompute
            }
        }
    }

    private static Map<Set<Integer>, Record> getArray(float[] selectivity, Comparator<Set<Integer>> comparator) {
        Map<Set<Integer>, Record> A =  new TreeMap<Set<Integer>, Record>(comparator);

        // populate sets in array
        for(int i = 1; i <= selectivity.length; i++) {
            populateArray(A, selectivity.length, i, 0, new HashSet<Integer>(), 0);
        }

        // initialize each record in the array
        for(Map.Entry<Set<Integer>, Record> entry: A.entrySet()) {
            initializeRecord(entry.getValue(), selectivity);
            System.out.println(entry.getKey());
        }
        return A;
    }

    private static void initializeRecord(Record record, float selectivity[]) {
        Set<Integer> terms = record.getTerms();
        //initialize P value
        Double p = 1.0;
        for(Integer i: terms) {
            p = p*selectivity[i];
        }
        record.setP(p);
        //TODO: incomplete
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
                    return 1;
                }
            }
        };
    }

}
