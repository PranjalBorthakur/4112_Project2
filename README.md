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







