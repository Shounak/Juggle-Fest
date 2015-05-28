import java.io.*;
import java.util.*;

public class JuggleFest
{
	public static void main(String[] args) 
	{
		String fileName = "simpleJuggleFest.txt";
		output(readFile(fileName));
	}

	private static int numberOfLines(String fileName)
	{
		// Get the total number lines in the file. This is used to determine team size.
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

	static HashMap<Circuit, ArrayList<Juggler>> readFile(String fileName)
	{
		BufferedReader fileReader = null;

		// A Hashmap where Keys are Circuits and Values are Jugglers
		// HashMap is used for its O(1) lookup and as a natural way of organizing circuits and their lists of jugglers 
		HashMap<Circuit, ArrayList<Juggler>> jugglerAssignments = new HashMap<>(); 

		// Maps string circuit names to circuit objects e.x. "C0" => C C0 H:7 E:7 P:10
		HashMap<String, Circuit> circuits = new HashMap<>();

		int numberOfCircuits = 0;
		try
		{
			fileReader = new BufferedReader(new FileReader(fileName));

			// read in all the circuits
			String circuitLine = fileReader.readLine();
			while (!(circuitLine.trim().equals("")) && circuitLine.charAt(0) == 'C') 
			{	
				String[] data = circuitLine.split(" ");

				String newCircuitName = data[1];
				int newCircuitHandEyeCoordination = Integer.parseInt(data[2].split(":")[1]);
				int newCircuitEndurance = Integer.parseInt(data[3].split(":")[1]);
				int newCircuitPizzazz = Integer.parseInt(data[4].split(":")[1]);
				Circuit newCircuit = new Circuit(newCircuitName, newCircuitHandEyeCoordination, newCircuitEndurance, newCircuitPizzazz);
				jugglerAssignments.put(newCircuit, new ArrayList<Juggler>());
				circuits.put(newCircuitName, newCircuit);
				numberOfCircuits++;	
				circuitLine = fileReader.readLine();
			}

			final int jugglersPerTeam = (numberOfLines(fileName) - numberOfCircuits)/numberOfCircuits;

			// All the rest of the lines are jugglers
			String jugglerLine;
			while ((jugglerLine = fileReader.readLine()) != null)
			{
				String[] data = jugglerLine.split(" ");
				placeJuggler(jugglerAssignments, circuits, data, jugglersPerTeam);
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

		return jugglerAssignments;
	}

	private static void placeJuggler(HashMap<Circuit, ArrayList<Juggler>>  jugglersMap, HashMap<String, Circuit> circuitsMap, String[] abilities, int teamSize)
	{
		String newJugglerName = abilities[1];
		int newJugglerHandEyeCoordination = Integer.parseInt(abilities[2].split(":")[1]);
		int newJugglerEndurance = Integer.parseInt(abilities[3].split(":")[1]);
		int newJugglerPizzazz = Integer.parseInt(abilities[4].split(":")[1]);
		String[] circuitPrefs = abilities[5].split(",");
		jugglersMap.get(circuitsMap.get(circuitPrefs[0])).add(new Juggler(newJugglerName, newJugglerHandEyeCoordination, newJugglerEndurance, newJugglerPizzazz, circuitPrefs));
	}

	static void output(HashMap<Circuit, ArrayList<Juggler>>  jugglersAndCircuits)
	{
		Set set = jugglersAndCircuits.entrySet();
		Iterator i = set.iterator();

		while(i.hasNext()){
			Map.Entry<Circuit, ArrayList<Juggler>> entry = (Map.Entry)i.next();
			System.out.print(entry.getKey().getName());
			for (Juggler juggler : entry.getValue())
				System.out.print(" " + juggler.getName());
			System.out.println("");
		}
	}

	private static int getScore(Circuit c, Juggler j)
	{
		return ((c.getHandEyeCoordination()*j.getHandEyeCoordination()) + (c.getEndurance()*j.getEndurance()) + (c.getPizzazz()*j.getPizzazz()));
	}	
}

class Circuit
{
	private String name;
	private int handEyeCoordination;
	private int endurance;
	private int pizzazz;

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

	// Overriding .equals and .hashCode is necessary for inserting Circuits as a Key into the HashMap
	@Override
	public boolean equals(Object other) 
	{
		if (!(other instanceof Circuit)) 
		{
			return false;
		}
		Circuit c = (Circuit) other;
		return equalsHelper(name, c.name) && 
		equalsHelper(handEyeCoordination, c.handEyeCoordination) &&
		equalsHelper(endurance, c.endurance) &&
		equalsHelper(pizzazz, c.pizzazz);
	}

	private static boolean equalsHelper(Object a, Object b) 
	{
		if (a == null || b == null) 
		{
			return false;
		}
		if (a == b) 
		{
			return true;
		}

		return a.equals(b);
	}


	@Override
	public int hashCode() 
	{
		int hash = 17;
		hash = hash * 31 + (name == null ? 0 : name.hashCode());
		hash = hash * 31 + handEyeCoordination;
		hash = hash * 31 + endurance;
		hash = hash * 31 + pizzazz;
		return hash;
	}
}

class Juggler
{
	private String name;
	private int handEyeCoordination;
	private int endurance;
	private int pizzazz;
	private String[] circuitPreferences;

	public Juggler(String n, int h, int e, int p, String[] circuits)
	{
		name = n;
		handEyeCoordination = h;
		endurance = e;
		pizzazz = p;
		circuitPreferences = circuits;
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
}