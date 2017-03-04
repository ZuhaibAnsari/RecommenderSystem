package alg.ib.predictor;

import java.util.Map;

import alg.ib.neighbourhood.Neighbourhood;
import profile.Profile;
import similarity.SimilarityMap;

/**
 * An class to compute the target user's predicted rating for the target item
 * (item-based CF) using the weighted average technique
 * 
 * @author Zuhaib
 *
 */
public class WeightedAveragePredictor implements Predictor {
	/**
	 * constructor - creates a new WeightedAveragePredictor object
	 */
	public WeightedAveragePredictor() {
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

		double above = 0;
		double similarityWeightSum = 0;
		// iterate over the target user's items
		for (Integer id : userProfileMap.get(userId).getIds()) {
			// the current item is in the neighbourhood
			if (neighbourhood.isNeighbour(itemId, id)) {

				//fetching the similarity weight of the item
				Double similarityWeight = simMap.getSimilarity(itemId, id);
				Double rating = userProfileMap.get(userId).getValue(id);

				// the product of the similarity weight and the rating is used
				// the numerator
				above += similarityWeight * rating.doubleValue();
				similarityWeightSum += similarityWeight;
			}
		}

		return (similarityWeightSum > 0) ? new Double(above / similarityWeightSum) : null;
	}
}
