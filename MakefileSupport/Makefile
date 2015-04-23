JC = javac
J=java
FILE= Application
default: compile run
compile: 
	$(JC) $(JFLAGS) $(FILE).java
	
run:
	$(J) $(FILE) query.txt config.txt
clean: 
	rm -r *.class
