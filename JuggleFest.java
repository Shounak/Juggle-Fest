import java.io.*;
import java.util.*;

public class JuggleFest
{
	public static void main(String[] args) 
	{
		String fileName = "simpleJuggleFest.txt";
		int linesCount = numberOfLines(fileName);
		readFile(fileName);
		HashMap<Circuit, Juggler[]> jugglerAssignments = new HashMap<>();
	}

	static int numberOfLines(String fileName)
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

		return (linesCount-1); //Done because there is an empty line between Circuits and Jugglers
	}

	static void readFile(String fileName)
	{
		BufferedReader fileReader = null;
		try
		{
			fileReader = new BufferedReader(new FileReader(fileName));
			String line;

			ArrayList<Circuit> listOfCircuits = new ArrayList<>(); // A temporary list of all the circuits
			ArrayList<Juggler> listOfJugglers = new ArrayList<>(); // A temporary list of all the jugglers

			while ((line = fileReader.readLine()) != null) 
			{
				// ignore blank lines
				if (line.trim() == "")
					continue;

				String[] data = line.split(" ");

				if (data[0].equals("C"))
				{
					String newCircuitName = data[1];
					int newCircuitHandEyeCoordination = Integer.parseInt(data[2].split(":")[1]);
					int newCircuitEndurance = Integer.parseInt(data[3].split(":")[1]);
					int newCircuitPizzazz = Integer.parseInt(data[4].split(":")[1]);
					listOfCircuits.add(new Circuit(newCircuitName, newCircuitHandEyeCoordination, newCircuitEndurance, newCircuitPizzazz));
				}
				if (data[0].equals("J"))
				{
					String newJugglerName = data[1];
					int newJugglerHandEyeCoordination = Integer.parseInt(data[2].split(":")[1]);
					int newJugglerEndurance = Integer.parseInt(data[3].split(":")[1]);
					int newJugglerPizzazz = Integer.parseInt(data[4].split(":")[1]);
					String[] circuitPrefs = data[5].split(",");
					listOfJugglers.add(new Juggler(newJugglerName, newJugglerHandEyeCoordination, newJugglerEndurance, newJugglerPizzazz, circuitPrefs));
				}
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