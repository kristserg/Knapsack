import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Solver 
{
	public static int[] fillTheChromosome(Random r, int numberOfItems, int[] newChromosome, int len) 
	{
		int pos = 0;
		ArrayList<Integer> usedPos = new ArrayList<Integer>();
		for(int i = 0; i < numberOfItems; i++)
		{
			pos = r.nextInt(len);
			while(usedPos.contains(pos))
			{
				pos = r.nextInt(len);
			}
			newChromosome[pos] = 1;
		}
		usedPos.clear();
		return newChromosome;
	}

	public static int calculateValues(int[] newChromosome, int[] values) 
	{
		int value = 0;
		for(int i = 0; i < values.length; i++)
		{
			if(newChromosome[i] == 1)
			{
				value += values[i];
			}
		}
		return value;
	}
	
	public static int calculateWeights(int[] newChromosome, int[] weights) 
	{
		int weight = 0;
		for(int i = 0; i < weights.length; i++)
		{
			if(newChromosome[i] == 1)
			{
				weight += weights[i];
			}
		}
		return weight;
	}
	
	public static int[] nextGeneration(List<int[]> newGeneration, List<int[]> bestSacks, int len, 
			Random r, List<int[]> NNGeneration, List<int[]> newBestSacks, 
			int[] weights, int[] values) 
	{
		int pointOfCrossover = 0;
		int currentValue = 0;
		int currentWeight = 0;
		int[] bestChromosome = new int[len];
		int bestValue = 0;
		int indexOfMutation = 0;
		NNGeneration.clear();
		newBestSacks.clear();
		for(int i = 0; i < (len * len) / 2; i++)
		{
			int end = 0;
			int[] parent1 = new int[len];
			int[] parent2 = new int[len];
			parent1 = bestSacks.get(r.nextInt(bestSacks.size()));
			parent2 = newGeneration.get(r.nextInt(newGeneration.size()));
			while(parent2.equals(parent1) && end < 20)
			{
				parent2 = newGeneration.get(r.nextInt(newGeneration.size()));
				end++;
			}
			if(end >= 20)
			{
				return parent1;
			}
			else
			{
				int[] child1 = new int[len];
				int[] child2 = new int[len];
				pointOfCrossover = r.nextInt(len / 2 + len / 4);
				for(int j = 0; j < len; j++)
				{
					if(j <= pointOfCrossover)
					{
						child1[j] = parent1[j];
						child2[j] = parent2[j];
					}
					else
					{
						child1[j] = parent2[j];
						child2[j] = parent1[j];
					}
					if(i % 16 == 0)
					{
						indexOfMutation = r.nextInt(len);
						if(child1[indexOfMutation] == 0)
						{
							child1[indexOfMutation] = 1;
						}
						else
						{
							child1[indexOfMutation] = 0;
						}
					}
				}
				currentWeight = calculateWeights(child1, weights);
				if(currentWeight <= 5000 && !NNGeneration.contains(child1))
				{
					NNGeneration.add(child1);
					currentValue = calculateValues(child1, values);
					if(currentValue > bestValue)
					{
						bestValue = currentValue;
						bestChromosome = child1;
						newBestSacks.add(bestChromosome);
					}
				}
				currentWeight = calculateWeights(child2, weights);
				if(currentWeight <= 5000 && !NNGeneration.contains(child2))
				{
					NNGeneration.add(child2);
					currentValue = calculateValues(child2, values);
					if(currentValue > bestValue)
					{
						bestValue = currentValue;
						bestChromosome = child2;
						newBestSacks.add(bestChromosome);
					}
				}
				/*System.out.println("NNgen: ");
				for(int k = 0; k < NNGeneration.size(); k++)
				{
					for(int l = 0; l < 24; l++)
					{
						System.out.print(NNGeneration.get(k)[l]);
					}
					System.out.println();
				}
				System.out.println();*/
			}
		}
		return bestChromosome;
	}
	
	public static void main(String[] args) 
	{
		String[] items = {"map", "compass", "water", "sandwich", 
				"glucose", "tin", "banana", "apple", "cheese", 
				"beer", "suntan cream", "camera", "t-shirt", 
				"trousers", "umbrella", "waterproof trousers", 
				"waterproof overclothes", "note-case", "sunglasses", 
				"towel", "socks", "book", "notebook", "tent"};
		int[] weights = {90, 130, 1530, 500, 150, 680, 270, 390, 
				230, 520, 110, 320, 240, 480, 730, 420, 430, 220, 
				70, 180, 40, 300, 900, 2000};
		int[] values = {150, 35, 200, 160, 60, 45, 60, 40, 30, 10, 
				70, 30, 15, 10, 40, 70, 75, 80, 20, 12, 50, 10, 1, 150};
		
		Random r = new Random();
		int len = items.length;
		List<int[]> newGeneration = new ArrayList<int[]>();
		List<int[]> bestSacks = new ArrayList<int[]>();
		//int[] bestSack = new int[items.length];
		int numberOfItems = 0;
		int currentValue = 0;
		int currentWeight = 0;
		int bestValue = 0;
		for(int i = 0; i < items.length * items.length; i++)
		{
			while(numberOfItems == 0)
			{
				numberOfItems = r.nextInt(items.length);
			}
			int[] newChromosome = new int[items.length];
			for(int j = 0; j < items.length; j++)
			{
				newChromosome[j] = 0;
			}
			newChromosome = fillTheChromosome(r, numberOfItems, newChromosome, len);
			currentWeight = calculateWeights(newChromosome, weights);
			if(currentWeight <= 5000 && !newGeneration.contains(newChromosome))
			{
				newGeneration.add(newChromosome);
			}
			else
			{
				continue;
			}
			currentValue = calculateValues(newChromosome, values);
			if(currentValue > bestValue)
			{
				bestSacks.add(newChromosome);
				//bestSack = newChromosome;
				bestValue = currentValue;
			}
		}
		int[] currentGenerationBest = new int[items.length];
		int allW = 0;
		int allV = 0;
		for(int i = 0; i < items.length; i++)
		{
			//System.out.println("i = " + i + " ");
			List<int[]> newNewGeneration = new ArrayList<int[]>();
			List<int[]> newBestSacks = new ArrayList<int[]>();
			currentGenerationBest = nextGeneration(newGeneration, bestSacks, len, r, 
					newNewGeneration, newBestSacks, weights, values);
			/*allW = 0;
			allV = 0;
			for(int j = 0; j < items.length; j++)
			{
				if(currentGenerationBest[j] == 1)
				{
					//System.out.print(items[j] + "; ");
					allW += weights[j];
					allV += values[j];
				}
			}
			System.out.print("W: " + allW + ", V: " + allV);
			System.out.println();*/
			newGeneration = newNewGeneration;
			bestSacks = newBestSacks;
		}
		System.out.print("I'm taking: ");
		for(int i = 0; i < items.length; i++)
		{
			if(currentGenerationBest[i] == 1)
			{
				System.out.print(items[i] + "; ");
			}
		}
		System.out.println();
		for(int j = 0; j < items.length; j++)
		{
			if(currentGenerationBest[j] == 1)
			{
				//System.out.print(items[j] + "; ");
				allW += weights[j];
				allV += values[j];
			}
		}
		System.out.print("Weight: " + allW + ", Value: " + allV);
	}
}
