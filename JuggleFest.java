import java.io.*;
import java.util.*;

public class JuggleFest
{
	public static void main(String[] args) 
	{
		String fileName = "JuggleFest.txt";
		output(readFile(fileName));
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

	static ArrayList<Circuit> readFile(String fileName)
	{
		BufferedReader fileReader = null;
		ArrayList<Circuit> circuits = new ArrayList<>();
		ArrayList<Juggler> rejectedJugglers = new ArrayList<>();

		int numberOfCircuits = 0;
		try
		{
			fileReader = new BufferedReader(new FileReader(fileName));

			// read in all the circuits and add them to the ArrayList
			String circuitLine = fileReader.readLine();
			while (!(circuitLine.trim().equals("")) && circuitLine.charAt(0) == 'C') 
			{	
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
				String[] data = jugglerLine.split(" ");
				String newJugglerName = data[1];
				int newJugglerHandEyeCoordination = Integer.parseInt(data[2].split(":")[1]);
				int newJugglerEndurance = Integer.parseInt(data[3].split(":")[1]);
				int newJugglerPizzazz = Integer.parseInt(data[4].split(":")[1]);
				String[] circuitPrefs = data[5].split(",");
				Juggler newJuggler = new Juggler(newJugglerName, newJugglerHandEyeCoordination, newJugglerEndurance, newJugglerPizzazz, circuitPrefs);
				Circuit circuitToAddTo = circuits.get(Character.getNumericValue(newJuggler.getPreferredCircuit().charAt(1)));
				Juggler rejectedJuggler =  circuitToAddTo.addJuggler(newJuggler, jugglersPerTeam);
				if (rejectedJuggler != null)
				{
					rejectedJugglers.add(rejectedJuggler);
				}
			}
			placeRejectedJugglers(rejectedJugglers, circuits, jugglersPerTeam);
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

	static void placeRejectedJugglers(ArrayList<Juggler> rejectedJugglers, ArrayList<Circuit> circuits, int jugglersPerTeam)
	{
		if (rejectedJugglers.isEmpty())
			return;

		ArrayList<Juggler> newRejectedJugglers = new ArrayList<>();
		for (Juggler j : rejectedJugglers)
		{
			Circuit rejectedJugglerCircuit = circuits.get(Character.getNumericValue(j.getPreferredCircuit().charAt(1)));
			Juggler newRejectedJuggler =  rejectedJugglerCircuit.addJuggler(j, jugglersPerTeam);
			if (newRejectedJuggler != null)
			{
				newRejectedJugglers.add(newRejectedJuggler);
			}	
		}

		placeRejectedJugglers(newRejectedJugglers, circuits, jugglersPerTeam);
	}

	static void output(ArrayList<Circuit>  jugglersAndCircuits)
	{
		for (int i  = jugglersAndCircuits.size()-1; i >=0; i--)
		{
			jugglersAndCircuits.get(i).printJugglers(jugglersAndCircuits);
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

	public String getName()
	{
		return this.name;
	}
	public int getHandEyeCoordination()
	{
		return this.handEyeCoordination;
	}
	public int getEndurance()
	{
		return this.endurance;
	}
	public int getPizzazz()
	{
		return this.pizzazz;
	}

	// Adds jugglers to the circuit, and keeps the ordering of the jugglers in the circuit as greatest to least score. 
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

	// Prints in the proper format this circuit's name and all its associated jugglers + scores etc. 
	public void printJugglers(ArrayList<Circuit> circuitsList)
	{
		System.out.print(this.name);
		for (int j = 0; j < this.jugglers.size(); j++)
		{
			System.out.print(" " + this.jugglers.get(j).getName());
			for (String s : this.jugglers.get(j).getCircuitPreferences())
				System.out.print(" " + s + ":" + circuitsList.get(Character.getNumericValue(s.charAt(1))).getScore(this.jugglers.get(j)));
			// Only print a comma if it's not the last juggler in the circuit
			if (j < this.jugglers.size()-1)
				System.out.print(",");
		}
		System.out.println("\n");	
	}

	// Returns the dot product of this circuit and the given juggler
	private int getScore(Juggler j)
	{
		return ((handEyeCoordination*j.getHandEyeCoordination()) + (endurance*j.getEndurance()) + (pizzazz*j.getPizzazz()));
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

	public String getName()
	{
		return this.name;
	}
	public int getHandEyeCoordination()
	{
		return this.handEyeCoordination;
	}
	public int getEndurance()
	{
		return this.endurance;
	}
	public int getPizzazz()
	{
		return this.pizzazz;
	}
	public String[] getCircuitPreferences()
	{
		return this.circuitPreferences;
	}

	public String getPreferredCircuit()
	{
		if (!prefsQueue.isEmpty()) {
			return prefsQueue.remove();
		}
		System.out.println("Queue is empty!");
		return "";
	}
}