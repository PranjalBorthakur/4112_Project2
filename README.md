#Uni
nj2303 - Naman Jain

rg2930 - Rahul Gaur

# How to Run

Go to the folder where you cloned the git repo. Run the command "mvn clean install". It will generate a target folder with the jar for the application. The jar is named as 'dbsi-project2-1.0-SNAPSHOT.jar'. You need to run the jar with 2 arguments, path to query.txt and config.txt respectively. You can run the following command after going to target folder: 'java -jar dbsi-project2-1.0-SNAPSHOT.jar ../src/main/resources/query.txt ../src/main/resources/config.txt'

You can also run it using Makefile. Go to the folder MakefileSupport and make the script. It will generate the output using query and config txt files present in resources.

#Selection Conditions in Main Memory

In this project we have implemented a query optimization algorithm for a class of queries over an in-memory database.

We have optimized a class of scan queries over a large array of records (implemented as a Map 'A' whose keys are all possible 2^k subsets of S). 

The queries apply conditions to the records, and return answers (ids of records matching all conditions) in an answer array (Updated in the same array A).

This dynamic program (Application.java) is an implementation of an algorithm, which finds an optimal plan among various plans for given set of basic terms (given in query.txt) and machine configurations (provided in config.txt). 

The following is the terminology:

1. S : the the set of basic terms.
2. n : Number of basic terms
3. b : bit b to determine whether the no-branch optimization was used to get the best cost, initialized to 0
4. p : product p of the selectivities of all terms in the subset
5. c : the current best cost c for the subset

At the end of the algorithm, A[S].c contains the optimal cost, and its corresponding plan can be recursively derived by combining the &-conjunction A[S].L to the plan for A[S].R via &&.

The Entire Algorithm description can be found at: http://www.cs.columbia.edu/~kar/pubsk/selcondsTODS.pdf 

The following is the structure of Application.java which contains the implementation of the Algorithm:

1. The code starts from main function which takes path to query file and config file as input respectively.
2. For each case in query file, it parses the selectivity, finds the optimal plan and print it.
3. It uses a TreeMap where its key is subset of basic terms (implemented as hashmap) and its value is an object that contains all the necessary information (refer to Record.java)
4. It then generates the output by contructing the optimal plan from the entry whose key is the original set S itself. 

We are using a TreeMap instead of other maps as we want to control the order of iteration over the map. Also, since we are using Set as a key for the map, we need to implement a Comparator for Set which is returned by getComparator() function. 
