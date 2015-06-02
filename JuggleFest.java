// Written by: Shounak Kulkarni
// github.com/shounak

import java.io.*;
import java.util.*;

public class JuggleFest
{
	public static void main(String[] args) 
	{
		String fileName = "JuggleFest.txt";

		try 
		{
			File outputFile = new File("output.txt");
			if (!outputFile.exists()) 
			{
				outputFile.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile.getAbsoluteFile()));

			output(readFile(fileName), bw);
			bw.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	// Get the total number of lines in the file. This is used to determine team size.
	private static int numberOfLines(String fileName)
	{
		int linesCount = 0;
		try
		{
			LineNumberReader lnr = new LineNumberReader(new FileReader(fileName));

			while (lnr.readLine() != null)
			{
				linesCount++;
			}

			lnr.close();
		}
		catch(IOException e)
		{
			System.out.println("There was a problem reading the specified file");
		}

		return (linesCount-1); // "-1" is because there is an empty line between Circuits and Jugglers
	}

	/*
		This function reads the file line-by-line.
		It first creates Circuit objects to add to the circuits ArrayList
		It then creates and places Juggler objects. 

		Placing jugglers is done in 3 steps:
		1. Tries to put the jugglers into their first choice circuit. If this fails jugglers are added to the rejectedJugglers list
		2. Goes down the juggler's circuit preferences, trying to put the juggler into each preferred circuit. 
		   If the juggler doesn't get into any of their preferences, they are placed in the preferencelessJugglers list
		3. preferencelessJugglers are placed into any open circuits
	*/
		static ArrayList<Circuit> readFile(String fileName)
		{
			BufferedReader fileReader = null;
		// Store all the Circuit objects
			ArrayList<Circuit> circuits = new ArrayList<>();
		// Stores jugglers that did not make it into their first choice circuit
			ArrayList<Juggler> rejectedJugglers = new ArrayList<>();
		// Store jugglers that did not make it into any of their preferred circuits
			ArrayList<Juggler> preferencelessJugglers = new ArrayList<>();

			int numberOfCircuits = 0;
			try
			{
				fileReader = new BufferedReader(new FileReader(fileName));

			// read in all the circuits and add them to the ArrayList
				String circuitLine = fileReader.readLine();
				while (!(circuitLine.trim().equals("")) && circuitLine.charAt(0) == 'C') 
				{	
				// Create a new Circuit object from the data in the line
				// e.x. C C0 H:7 E:7 P:10
					String[] data = circuitLine.split(" ");
					String newCircuitName = data[1];
					int newCircuitHandEyeCoordination = Integer.parseInt(data[2].split(":")[1]);
					int newCircuitEndurance = Integer.parseInt(data[3].split(":")[1]);
					int newCircuitPizzazz = Integer.parseInt(data[4].split(":")[1]);
					Circuit newCircuit = new Circuit(newCircuitName, newCircuitHandEyeCoordination, newCircuitEndurance, newCircuitPizzazz);
					circuits.add(newCircuit);
					numberOfCircuits++;	
					circuitLine = fileReader.readLine();
				}

				final int jugglersPerTeam = (numberOfLines(fileName) - numberOfCircuits)/numberOfCircuits;

			// All the rest of the lines are jugglers
				String jugglerLine;
				while ((jugglerLine = fileReader.readLine()) != null)
				{
				// Create a new Juggler object from the data in the line
				// e.x. J J0 H:3 E:9 P:2 C2,C0,C1
					String[] data = jugglerLine.split(" ");
					String newJugglerName = data[1];
					int newJugglerHandEyeCoordination = Integer.parseInt(data[2].split(":")[1]);
					int newJugglerEndurance = Integer.parseInt(data[3].split(":")[1]);
					int newJugglerPizzazz = Integer.parseInt(data[4].split(":")[1]);
					String[] circuitPrefs = data[5].split(",");
					Juggler newJuggler = new Juggler(newJugglerName, newJugglerHandEyeCoordination, newJugglerEndurance, newJugglerPizzazz, circuitPrefs);
					Circuit circuitToAddTo = circuits.get(Integer.parseInt(newJuggler.getPreferredCircuit().substring(1)));

				// try adding the new juggler to their #1 preferred circuit
					Juggler rejectedJuggler =  circuitToAddTo.addJuggler(newJuggler, jugglersPerTeam);
				// Jugglers that did not get into their #1 preference are placed in the rejectedJugglers list
					if (rejectedJuggler != null)
					{
						rejectedJugglers.add(rejectedJuggler);
					}
				}

			// Place all the rejected jugglers
				placeRejectedJugglers(rejectedJugglers, circuits, preferencelessJugglers, jugglersPerTeam);

			// Jugglers who did not get into any of their preferred circuits are randomly placed by linearly searching for the 
			// first non-full circuit and putting them there. 
				for (Juggler preferencelessJuggler : preferencelessJugglers)
				{
					for (Circuit circuit : circuits)
					{
						if (!circuit.isFull(jugglersPerTeam))
							circuit.addJuggler(preferencelessJuggler, jugglersPerTeam);
					}
				}
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			finally 
			{
				try 
				{
					if (fileReader != null)
						fileReader.close();
				} catch (IOException ex) 
				{
					ex.printStackTrace();
				}
			}

			return circuits;
		}

	/*
		This function takes in an ArrayList of jugglers that did not get into their first choice circuit. 
		It works through this list, attempting to place each juggler into their next-preferred circuit. 
		If the juggler does not fit into their preferred circuit, or if a juggler got kicked out of a circuit by a new juggler, 
		then that juggler is added to a new "newRejectedJugglers" list. 

		The function is then recursively called with the "newRejectedJugglers" list until all jugglers are placed into either a circuit 
		or the preferencelessJugglers list. 
		The function is tail-recursive, to decrease impact on Big-O runtime. 
	*/
		static void placeRejectedJugglers(ArrayList<Juggler> rejectedJugglers, ArrayList<Circuit> circuits, ArrayList<Juggler> preferencelessJugglers, int jugglersPerTeam)
		{
			if (rejectedJugglers.isEmpty())
				return;

			ArrayList<Juggler> newRejectedJugglers = new ArrayList<>();
			for (Juggler j : rejectedJugglers)
			{
				String preference = j.getPreferredCircuit();

			// This juggler does not have any remaining circuit preferences
				if (preference.equals(""))
					preferencelessJugglers.add(j);
				else 
				{
					Circuit rejectedJugglerCircuit = circuits.get(Integer.parseInt(preference.substring(1)));
					Juggler newRejectedJuggler =  rejectedJugglerCircuit.addJuggler(j, jugglersPerTeam);
					if (newRejectedJuggler != null)
					{
						newRejectedJugglers.add(newRejectedJuggler);
					}	
				}
			}
			placeRejectedJugglers(newRejectedJugglers, circuits, preferencelessJugglers, jugglersPerTeam);
		}

	// Calls the printJugglers function for every Circuit object 
		static void output(ArrayList<Circuit>  circuits, BufferedWriter bw)
		{
			for (int i  = circuits.size()-1; i >=0; i--)
			{
				circuits.get(i).printJugglers(circuits, bw);
			}
		}	
	}

	class Circuit
	{
		private String name;
		private int handEyeCoordination;
		private int endurance;
		private int pizzazz;
		private ArrayList<Juggler> jugglers = new ArrayList<>();

		public Circuit(String n, int h, int e, int p)
		{
			name = n;
			handEyeCoordination = h;
			endurance = e;
			pizzazz = p;
		}

	// getters
		public String getName() { return this.name; }
		public int getHandEyeCoordination() { return this.handEyeCoordination; }
		public int getEndurance() { return this.endurance; }
		public int getPizzazz() { return this.pizzazz; }

	/*
	Adds jugglers to the circuit, and keeps the jugglers in the circuit in order from greatest to least score
	If the juggler to be added does not "get in" to the circuit i.e. the circuit is full and their score is not greater than any existing juggler, 
	then that juggler gets returned without being added. 

	If the circuit is full and the new juggler gets added, then the lowest scoring juggler is removed and returned. 
	*/
	public Juggler addJuggler(Juggler j, int teamSize)
	{
		int insertIndex = 0;
		int newJugglerScore = getScore(j);
		for (Juggler currentJuggler : jugglers)
		{
			if (getScore(currentJuggler) < newJugglerScore)
				break;
			insertIndex++;
		}
		if (insertIndex >= teamSize)
			return j;
		jugglers.add(insertIndex, j);
		if (jugglers.size() > teamSize)
		{
			return jugglers.remove(jugglers.size()-1);
		}
		return null;
	}

	// Prints to text file this circuit's name and all its associated jugglers + scores etc. in the proper format
	// e.x. C0 J5 C0:161 C2:112 C1:26, J11 C0:154 C1:27 C2:108, J2 C0:128 C2:68 C1:18, J4 C0:122 C2:106 C1:23
	public void printJugglers(List<Circuit> circuitsList, BufferedWriter bw)
	{
		try 
		{
			bw.write(this.name);
			for (int j = 0; j < this.jugglers.size(); j++)
			{
				bw.write(" " + this.jugglers.get(j).getName());
				for (String s : this.jugglers.get(j).getCircuitPreferences())
					bw.write(" " + s + ":" + circuitsList.get(Integer.parseInt(s.substring(1))).getScore(this.jugglers.get(j)));
			// Only print a comma if it's not the last juggler in the circuit
				if (j < this.jugglers.size()-1)
					bw.write(",");
			}
			bw.newLine();	
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
	}

	// Returns the dot product of this circuit and the given juggler
	private int getScore(Juggler j)
	{
		return ((handEyeCoordination*j.getHandEyeCoordination()) + (endurance*j.getEndurance()) + (pizzazz*j.getPizzazz()));
	}

	// Returns true if the circuit can't accept any new jugglers without kicking out an existing one
	// This function is useful when placing preferenceless jugglers
	public boolean isFull(int teamSize)
	{
		if (jugglers.size() >= teamSize)
			return true;
		return false;
	}
}


class Juggler
{
	private String name;
	private int handEyeCoordination;
	private int endurance;
	private int pizzazz;

	// stores an array of circuit preferences which will be used to print this juggler's match score for each preferred circuit to the output file
	private String[] circuitPreferences;
	// This Queue also stores preferred circuit information. This is used to determine which circuit team to put this juggler into
	private Queue<String> prefsQueue = new LinkedList<>();

	public Juggler(String n, int h, int e, int p, String[] circuits)
	{
		name = n;
		handEyeCoordination = h;
		endurance = e;
		pizzazz = p;
		circuitPreferences = circuits;
		for (String s : circuits)
		{
			prefsQueue.add(s);
		}
	}

	// getters
	public String getName() { return this.name; }
	public int getHandEyeCoordination() { return this.handEyeCoordination; }
	public int getEndurance() { return this.endurance; }
	public int getPizzazz() { return this.pizzazz; }
	public String[] getCircuitPreferences() { return this.circuitPreferences; }

	// Removes and returns this juggler's currently most preferred circuit
	// This function is a convenient way to move down the list of preferred circuits
	// If there are no more preferences left, it returns an empty string
	public String getPreferredCircuit()
	{
		if (!prefsQueue.isEmpty()) {
			return prefsQueue.remove();
		}
		return "";
	}
}