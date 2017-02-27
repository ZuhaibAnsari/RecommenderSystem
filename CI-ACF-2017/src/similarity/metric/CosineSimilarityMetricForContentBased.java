/**
 * Compute the Cosine similarity between profiles
 * 
 * Mohammad Zuhaib Ansari
 * 08/02/2017
 */

package similarity.metric;

import java.util.Set;

import profile.Profile;

public class CosineSimilarityMetricForContentBased implements SimilarityMetric
{
	/**
	 * constructor - creates a new CosineSimilarity object
	 */
	public CosineSimilarityMetricForContentBased()
	{
	}
		
	/**
	 * computes the similarity between profiles
	 * @param profile 1 
	 * @param profile 2
	 */
	public double getSimilarity(final Profile p1, final Profile p2)
	{
        double productOfNormalizedSquaredValues = 0;
        double dotProduct = 0;
        
        Set<Integer> common = p1.getCommonIds(p2);
		for(Integer id: common)
		{
			double r1 = p1.getValue(id).doubleValue();
			double r2 = p2.getValue(id).doubleValue();
		
            dotProduct += r1 * r2;
          
           
		}
		 productOfNormalizedSquaredValues = p1.getNorm()*p2.getNorm() ;
		double above = (common.size() > 0) ? dotProduct : 0;
		double below = (common.size() > 0) ? productOfNormalizedSquaredValues: 0;
		return (below > 0) ? above / below : 0;
	}
}
