# Juggle-Fest
A Java based solution to Yodle's Juggle Fest problem. 
More infromation about the problem can be found below. 

This solution takes a very OO approach, where the Circuits class stores information about the circuit and all the jugglers assigned to it, and the JuggleFest class reads the file and creates and assigns all the circuits and jugglers. 

There are three basic steps taken:

1. Create all Circuit objects

2. Create all Juggler objects and assign them to their first choice preference.
  1. If the above fails, go down the juggler's preference list, trying to assign them to each circuit. 
  2. If the juggler doesn't fit into any of their preferences, place them into any open circuit. 
3. Print out all the Circuit objects and their associated Jugglers.

This solution works in O(n*m) time and O(n) space, where n is the number of lines in the input file and m is the number of preferences for each juggler. 

##Problem Statement

Many developers here at Yodle are avid jugglers. To celebrate their prowess we are organizing a Yodle Open JuggleFest, but we need your help planning it. There will be thousands of participants split into teams. Each team will attempt to complete a juggling circuit consisting of several tricks. Each circuit emphasizes different aspects of juggling, requiring hand to eye coordination (H), endurance (E) and pizzazz (P) in various amounts for successful completion. Each juggler has these abilities in various amounts as well. How good a match they are for a circuit is determined by the dot product of the juggler’s and the circuit’s H, E, and P values. The higher the result, the better the match.

Each participant will be on exactly one team and there will be a distinct circuit for each team to attempt. Each participant will rank in order of preference their top X circuits. Since we would like the audiences to enjoy the performances as much as possible, when assigning jugglers to circuits we also want to consider how well their skills match up to the circuit. In fact we want to match jugglers to circuits such that no juggler could switch to a circuit that they prefer more than the one they are assigned to and be a better fit for that circuit than one of the other jugglers assigned to it.

To help us create the juggler/circuit assignments write a program in a language of your choice that takes as input a file of circuits and jugglers and outputs a file of circuits and juggler assignments. The number of jugglers assigned to a circuit should be the number of jugglers divided by the number of circuits. Assume that the number of circuits and jugglers will be such that each circuit will have the same number of jugglers with no remainder.

###Input file###
One line per circuit or juggler. All circuits will come before any jugglers. Circuit lines start with a C and juggler lines start with a J. Names of circuits and jugglers will never have spaces. A skill and the rating for that skill are separated by a colon. Circuit lines have the circuit names followed by skills. Juggler lines have the juggler names followed by skills, followed by circuits in order of preference, separated by commas. Example:

C C0 H:7 E:7 P:10

C C1 H:2 E:1 P:1

C C2 H:7 E:6 P:4


J J0 H:3 E:9 P:2 C2,C0,C1

J J1 H:4 E:3 P:7 C0,C2,C1

J J2 H:4 E:0 P:10 C0,C2,C1

J J3 H:10 E:3 P:8 C2,C0,C1

J J4 H:6 E:10 P:1 C0,C2,C1

J J5 H:6 E:7 P:7 C0,C2,C1

J J6 H:8 E:6 P:9 C2,C1,C0

J J7 H:7 E:1 P:5 C2,C1,C0

J J8 H:8 E:2 P:3 C1,C0,C2

J J9 H:10 E:2 P:1 C1,C2,C0

J J10 H:6 E:4 P:5 C0,C2,C1

J J11 H:8 E:4 P:7 C0,C1,C2


###Output file###
One line per circuit assignment. Each line should contain the circuit name followed by the juggler name, followed by that juggler’s circuits in order of preference and the match score for that circuit. The line should include all jugglers matched to the circuit. The example below is a valid assignment for the input file above.

C2 J6 C2:128 C1:31 C0:188, J3 C2:120 C0:171 C1:31, J10 C0:120 C2:86 C1:21, J0 C2:83 C0:104 C1:17

C1 J9 C1:23 C2:86 C0:94, J8 C1:21 C0:100 C2:80, J7 C2:75 C1:20 C0:106, J1 C0:119 C2:74 C1:18

C0 J5 C0:161 C2:112 C1:26, J11 C0:154 C1:27 C2:108, J2 C0:128 C2:68 C1:18, J4 C0:122 C2:106 C1:23
