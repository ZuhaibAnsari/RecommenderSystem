package alg.ib;

import alg.CFAlgorithm;
import alg.ib.neighbourhood.Neighbourhood;
import alg.ib.predictor.Predictor;
import similarity.SimilarityMap;
import similarity.metric.SimilarityMetric;
import util.reader.DatasetReader;

/**
 *  A class to implement the content-based filtering algorithm
 * @author Zuhaib
 *
 */
public class ContentBasedCF implements CFAlgorithm
{
	private Predictor predictor; // the predictor technique  
	private Neighbourhood neighbourhood; // the neighbourhood technique
	private DatasetReader reader; // dataset reader
	private SimilarityMap simMap; // similarity map - stores all item-item similarities
	
	/**
	 * constructor - creates a new ContentBasedCF object
	 * @param predictor - the predictor technique
	 * @param neighbourhood - the neighbourhood technique
	 * @param metric - the similarity metric
	 * @param reader - dataset reader
	 */
	public ContentBasedCF(final Predictor predictor, final Neighbourhood neighbourhood, final SimilarityMetric metric, final DatasetReader reader)
	{
		this.predictor = predictor;
		this.neighbourhood = neighbourhood;
		this.reader = reader;
		// passing the item genome scores to find the cosine similarity .. Hoping this to be the magical line of code which Michael mentioned
		this.simMap = new SimilarityMap(reader.getItemGenomeScores(), metric);
		this.neighbourhood.computeNeighbourhoods(simMap); // compute the neighbourhoods for all items
	}
	
	/**
	 * @returns the target user's predicted rating for the target item or null if a prediction cannot be computed
	 * @param userId - the target user ID
	 * @param itemId - the target item ID
	 */
	public Double getPrediction(final Integer userId, final Integer itemId)
	{	
		return predictor.getPrediction(userId, itemId, reader.getUserProfiles(), reader.getItemProfiles(), neighbourhood, simMap);
	}
}