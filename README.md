# Juggle-Fest
A Java based solution to Yodle's Juggle Fest problem. 
More infromation about the problem can be found in ProblemStatement.md. 

This solution takes a very OO approach, where the Circuits class stores information about the circuit and all the jugglers assigned to it, and the JuggleFest class reads the file and creates and assigns all the circuits and jugglers. 

There are three basic steps taken:

1. Create all Circuit objects

2. Create all Juggler objects and assign them to their first choice preference.
  1. If the above fails, go down the juggler's preference list, trying to assign them to each circuit. 
  2. If the juggler doesn't fit into any of their preferences, place them into any open circuit. 
3. Print out all the Circuit objects and their associated Jugglers.

This solution works in O(n*m) time and O(n) space, where n is the number of lines in the input file and m is the number of preferences for each juggler. 
