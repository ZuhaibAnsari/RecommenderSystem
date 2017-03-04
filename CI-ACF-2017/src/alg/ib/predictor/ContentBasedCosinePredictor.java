package alg.ib.predictor;

import java.util.Map;

import alg.ib.neighbourhood.Neighbourhood;
import profile.Profile;
import similarity.SimilarityMap;

/**
 * The class to compute the content based predictions
 * 
 * @author Zuhaib
 *
 */
public class ContentBasedCosinePredictor implements Predictor {

	boolean isRatingGreaterThanFour = false;

	/**
	 * constructor - creates a new ContentBasedCosinePredictor object
	 */
	public ContentBasedCosinePredictor() {
	}

	/**
	 * @param isRatingGreaterThanFour
	 *            - value to check if ratings greater than 4 are to be only
	 *            considered
	 */
	public ContentBasedCosinePredictor(boolean isRatingGreaterThanFour) {
		this.isRatingGreaterThanFour = isRatingGreaterThanFour;
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
		
		//This will hold the sum of the similarities
		double similarityWeightSum = 0;
		
		//This is the count of the movies which are rated by the user
		double countofMoviesRatedbyUser = 0;
		double similarityWeight = 0;
		
		//Iterate over the target users items
		for (Integer id : userProfileMap.get(userId).getIds())
		{
			//Fetch the ratings for the users
			Double rating = userProfileMap.get(userId).getValue(id);
			
			//Check for ratings greater than 4, if yes , then only those are considered 
			if (!isRatingGreaterThanFour) {
				similarityWeight = simMap.getSimilarity(itemId, id);
				similarityWeightSum += similarityWeight;
				countofMoviesRatedbyUser++;
			} else if (isRatingGreaterThanFour && rating >= 4.0) {
				similarityWeight = simMap.getSimilarity(itemId, id);
				similarityWeightSum += similarityWeight;
				countofMoviesRatedbyUser++;
			}

		}

		return (countofMoviesRatedbyUser > 0) ? new Double(similarityWeightSum / countofMoviesRatedbyUser) : null;
	}
}
