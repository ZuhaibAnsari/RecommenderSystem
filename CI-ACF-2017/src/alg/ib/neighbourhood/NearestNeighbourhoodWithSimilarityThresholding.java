package alg.ib.neighbourhood;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import similarity.SimilarityMap;
import profile.Profile;
import util.ScoredThingDsc;

/**
 * This class is used for neighbourhood formation using similarity thresholding 
 * @author Zuhaib
 *
 */
public class NearestNeighbourhoodWithSimilarityThresholding extends Neighbourhood
{
	private double threshold=Integer.MAX_VALUE; // threshold value for similarity restriction
	
	/**
	 * constructor - creates a new NearestNeighbourhood object
	 * @param threshold - the number of neighbours in the neighbourhood based the threshold
	 */
	
	public NearestNeighbourhoodWithSimilarityThresholding(final double threshold)
	{
		this.threshold=threshold;
	}

	/**
	 * stores the neighbourhoods for all items in member Neighbour.neighbourhoodMap
	 * @param simMap - a map containing item-item similarities
	 */
	public void computeNeighbourhoods(final SimilarityMap simMap)
	{
		for(Integer itemId: simMap.getIds()) // iterate over each item
		{
			SortedSet<ScoredThingDsc> ss = new TreeSet<ScoredThingDsc>(); // for the current item, store all similarities in order of descending similarity in a sorted set

			Profile profile = simMap.getSimilarities(itemId); // get the item similarity profile
			if(profile != null)
			{
				for(Integer id: profile.getIds()) // iterate over each item in the profile
				{
					double sim = profile.getValue(id);
				
					//Limiting to those items which are less than or equal to the threshold value
					if(sim>=threshold){
						ss.add(new ScoredThingDsc(sim, id));
					}
											
				}
			}

			// get the most similar items (neighbours)
			Iterator<ScoredThingDsc> iter = ss.iterator();
			while( iter.hasNext() )
			{
				ScoredThingDsc st = iter.next();
				Integer id = (Integer)st.thing;
				this.add(itemId, id);
			}
		}
	}
}
