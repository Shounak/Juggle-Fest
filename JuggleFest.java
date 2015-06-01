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
				placeJuggler(circuits, rejectedJugglers, newJuggler, jugglersPerTeam);
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

	private static void placeJuggler(ArrayList<Circuit>  circuitsList, ArrayList<Juggler> rejectedJugglers, Juggler newJuggler, int teamSize)
	{
		System.out.println(newJuggler.getPreferredCircuit().charAt(1));
		circuitsList.get(Character.getNumericValue(newJuggler.getPreferredCircuit().charAt(1))).addJuggler(newJuggler, teamSize);
	}

	static void output(ArrayList<Circuit>  jugglersAndCircuits)
	{
		System.out.println("\n\n ----------------------------Output-------------------------");
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

	public Juggler addJuggler(Juggler j, int teamSize)
	{
		jugglers.add(j);
		return null;
	}

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
		System.out.println("");	
	}

	public int getScore(Juggler j)
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
	private String[] circuitPreferences;
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