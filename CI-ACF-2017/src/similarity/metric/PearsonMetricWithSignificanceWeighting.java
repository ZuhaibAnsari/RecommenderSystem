

package similarity.metric;

import java.util.Set;

import profile.Profile;

/**
 * Compute the Pearson similarity with significance between profiles
 * 
 * @author Zuhaib
 */
public class PearsonMetricWithSignificanceWeighting implements SimilarityMetric
{
	//Setting the value of N as 100
	private static int N=100;
	/**
	 * constructor - creates a new PearsonMetricWithSignificanceWeighting object
	 */
	public PearsonMetricWithSignificanceWeighting()
	{
	}
		
	/**
	 * computes the similarity between profiles
	 * @param profile 1 
	 * @param profile 2
	 */
	public double getSimilarity(final Profile p1, final Profile p2)
	{
        double sum_r1 = 0;
        double sum_r1_sq = 0;
        double sum_r2 = 0;
        double sum_r2_sq = 0;
        double sum_r1_r2 = 0;
        
        Set<Integer> common = p1.getCommonIds(p2);
		for(Integer id: common)
		{
			double r1 = p1.getValue(id).doubleValue();
			double r2 = p2.getValue(id).doubleValue();

			sum_r1 += r1;
            sum_r1_sq += r1 * r1;
            sum_r2 += r2;
            sum_r2_sq += r2 * r2;
            sum_r1_r2 += r1 * r2;
		}

		double above = (common.size() > 0) ? sum_r1_r2 - (sum_r1 * sum_r2) / common.size() : 0;
		double below = (common.size() > 0) ? Math.sqrt( (sum_r1_sq - (sum_r1 * sum_r1) / common.size()) * (sum_r2_sq - (sum_r2 * sum_r2) / common.size()) ) : 0;
		if(below > 0){
			// Check to see if the value of the n is < N i.e. number of common items should be less than 100 , only then is the weighting done
			if(common.size() < N){
				return ((above / below)*(common.size()/N));
			}
			else
			{
				return (above / below);
			}
		}
		else{
			return 0;
		}
	}
}
