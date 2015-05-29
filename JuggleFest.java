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
				placeJuggler(circuits, data, jugglersPerTeam);
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

	private static void placeJuggler(ArrayList<Circuit>  circuitsList, String[] abilities, int teamSize)
	{
		String newJugglerName = abilities[1];
		int newJugglerHandEyeCoordination = Integer.parseInt(abilities[2].split(":")[1]);
		int newJugglerEndurance = Integer.parseInt(abilities[3].split(":")[1]);
		int newJugglerPizzazz = Integer.parseInt(abilities[4].split(":")[1]);
		String[] circuitPrefs = abilities[5].split(",");
		Juggler newJuggler = new Juggler(newJugglerName, newJugglerHandEyeCoordination, newJugglerEndurance, newJugglerPizzazz, circuitPrefs);
		int 
		int circuitsListIndex = Character.getNumericValue(circuitPrefs[0].charAt(1));
		circuitsList.get(circuitsListIndex).jugglers.add();
	}

	static void output(ArrayList<Circuit>  jugglersAndCircuits)
	{
		for (int i  = jugglersAndCircuits.size()-1; i >=0; i--)
		{
			Circuit currentCircuit = jugglersAndCircuits.get(i);
			System.out.print(currentCircuit.getName());
			for (int j = 0; j < currentCircuit.jugglers.size(); j++)
			{
				System.out.print(" " + currentCircuit.jugglers.get(j).getName());
				for (String s : currentCircuit.jugglers.get(j).getCircuitPreferences())
					System.out.print(" " + s + ":" + getScore(jugglersAndCircuits.get(Character.getNumericValue(s.charAt(1))), currentCircuit.jugglers.get(j)));
				if (j < currentCircuit.jugglers.size()-1) // Only print a comma if it's not the last juggler in the circuit
					System.out.print(",");
			}
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
	public ArrayList<Juggler> jugglers = new ArrayList<>();

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