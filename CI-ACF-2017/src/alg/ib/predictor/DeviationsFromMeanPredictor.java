package alg.ib.predictor;

import java.util.Map;

import alg.ib.neighbourhood.Neighbourhood;
import profile.Profile;
import similarity.SimilarityMap;

/**
 * An class to compute the target user's predicted rating for the target item
 * (item-based CF) using the deviation from mean technique.
 *
 * @author Zuhaib
 *
 */
public class DeviationsFromMeanPredictor implements Predictor {
	/**
	 * constructor - creates a new DeviationsFromMeanPredictor object
	 */
	public DeviationsFromMeanPredictor() {
	}

	/**
	 * @returns the target user's predicted rating for the target item or null
	 *          if a prediction cannot be computed
	 * @param userId
	 *            - the numeric ID of the target user
	 * @param itemId
	 *            - the numerid ID of the target item
	 * @param userProfileMap
	 *            - a map containing user profiles
	 * @param itemProfileMap
	 *            - a map containing item profiles
	 * @param neighbourhood
	 *            - a Neighbourhood object
	 * @param simMap
	 *            - a SimilarityMap object containing item-item similarities
	 */
	public Double getPrediction(final Integer userId, final Integer itemId, final Map<Integer, Profile> userProfileMap,
			final Map<Integer, Profile> itemProfileMap, final Neighbourhood neighbourhood, final SimilarityMap simMap) {
		
		//This is used to store the numerator the equation used for calcualting deviations from mean
		double above = 0;
		
		// This is used to store the sum of the similarities
		double similarityWeightSum = 0;
		double similarityWeight = 0;

		// iterate over the target user's items
		for (Integer id : userProfileMap.get(userId).getIds()) {
			if (neighbourhood.isNeighbour(itemId, id)) {
				similarityWeight = simMap.getSimilarity(itemId, id);
				Double rating = userProfileMap.get(userId).getValue(id);
				
				// The product of the similarity weight and the difference of ratings from the item mean value
				
				above += similarityWeight * (rating.doubleValue() - itemProfileMap.get(id).getMeanValue());
									
				similarityWeightSum += similarityWeight;	
				
			}
		}
		
		// Adding the item mean to the result in order to find the normalised predicted rating
		return (similarityWeightSum > 0)
				?  + itemProfileMap.get(itemId).getMeanValue()+(new Double(above / similarityWeightSum)) : null;
	}
}
