package alg.ib.predictor;

import java.util.Map;

import alg.ib.neighbourhood.Neighbourhood;
import profile.Profile;
import similarity.SimilarityMap;

/**
 * An class to compute the target user's predicted rating for the target item (item-based CF) using the non personalised technique.

 * @author Zuhaib
 *
 */
public class NonPersonalisedPredictor implements Predictor
{
	/**
	 * constructor - creates a new NonPersonalisedPredictor object
	 */
	public NonPersonalisedPredictor()
	{
	}
	
	/**
	 * @returns the target user's predicted rating for the target item or null if a prediction cannot be computed
	 * @param userId - the numeric ID of the target user
	 * @param itemId - the numerid ID of the target item
	 * @param userProfileMap - a map containing user profiles
	 * @param itemProfileMap - a map containing item profiles
	 * @param neighbourhood - a Neighbourhood object
	 * @param simMap - a SimilarityMap object containing item-item similarities
	 */
	public Double getPrediction(final Integer userId, final Integer itemId, final Map<Integer,Profile> userProfileMap, final Map<Integer,Profile> itemProfileMap, final Neighbourhood neighbourhood, final SimilarityMap simMap)	
	{
		double rating = 0;
		
		//returns the mean of the item as the prediction
		rating = itemProfileMap.get(itemId).getMeanValue();
		
		return rating;
	}
}

