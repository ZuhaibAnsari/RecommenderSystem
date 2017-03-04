package alg.ib;

import alg.constants.Constants;
import alg.ib.neighbourhood.NearestNeighbourhood;
import alg.ib.neighbourhood.Neighbourhood;
import alg.ib.predictor.DeviationsFromMeanPredictor;
import alg.ib.predictor.Predictor;
import similarity.metric.CosineSimilarityMetric;
import similarity.metric.PearsonMetric;
import similarity.metric.PearsonMetricWithSignificanceWeighting;
import similarity.metric.SimilarityMetric;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

/**
 * This class is used to run the tasks for experiment 3
 * @author Zuhaib
 *
 */
public class ExecuteIB_ML20M_Experiment_3 {
	
	private static final String COSINE_SIMILARITY_USING_100_NEIGHBOURS = "Cosine_Similarity_Using_100_Neighbours";
	private static final String PEARSON_CORRELATION_WITH_SIGNIFICANCE_WEIGHING_USING_100_NEIGHBOURS = "Pearson_Correlation_With_Significance_Weighing_Using_100_Neighbours";
	private static final String PEARSON_CORRELATION_WITH_100_NEIGHBOURS = "Pearson_Correlation_With_100_Neighbours";

	public static void main(String[] args) {

		// set the paths and filenames of the item file, genome scores file,
		// train file and test file using values from constant file
		String itemFile = Constants.ITEM_FILE_NAME;
		String itemGenomeScoresFile = Constants.ITEM_GENOME_SCORES_FILE;
		String trainFile = Constants.TRAIN_FILE_NAME;
		String testFile = Constants.TEST_FILE_NAME;

		//Generate predictions using Pearson correlation metric
		// set the path and filename of the output file ...
		String outputFile = Constants.OUTPUT_FILE_PATH+PEARSON_CORRELATION_WITH_100_NEIGHBOURS+Constants.OUTPUT_FILE_EXTENSION;
		
		generatePredictionAndCalculateRMSE(itemFile, itemGenomeScoresFile, trainFile, testFile, outputFile,
				Constants.PEARSON_CORRELATION);

		//Generate predictions using Pearson correlation metric with weighting
		outputFile = Constants.OUTPUT_FILE_PATH+PEARSON_CORRELATION_WITH_SIGNIFICANCE_WEIGHING_USING_100_NEIGHBOURS+Constants.OUTPUT_FILE_EXTENSION;
		
		generatePredictionAndCalculateRMSE(itemFile, itemGenomeScoresFile, trainFile, testFile, outputFile,
				Constants.PEARSON_CORRELATION_WITH_SIGNIFICANCE_WEIGHTING);
		

		//Generate predictions using Cosine metric
		outputFile = Constants.OUTPUT_FILE_PATH+COSINE_SIMILARITY_USING_100_NEIGHBOURS+Constants.OUTPUT_FILE_EXTENSION;
		
		generatePredictionAndCalculateRMSE(itemFile, itemGenomeScoresFile, trainFile, testFile, outputFile,
				Constants.COSINE);
	}

	
	 
	/**
	 * This is the method which will be used to calcuate the Predictions and the RMSE using various similarity metrics which are selected
	 * based on the value of the metric type  parameter
	 * @param itemFile
	 *            is the file containing the details of the items
	 * @param itemGenomeScoresFile
	 *            is the file with details of the genome scores
	 * @param trainFile
	 *            is the file with training data
	 * @param testFile
	 *            is the file with test data
	 * @param outputFile
	 *            is the file where the output will be stored for each execution
	 * @param metricType is the paramter which is used to specify the metric type to be used 
	 */
	private static void generatePredictionAndCalculateRMSE(String itemFile, String itemGenomeScoresFile,
			String trainFile, String testFile, String outputFile, String metricType) {
		
		System.out.println("########################### Calculating Prediction using "+metricType+" ##########################");

			// configure the item-based CF algorithm - set the predictor,
			// neighbourhood and similarity metric ...
			Predictor predictor=new DeviationsFromMeanPredictor();
			SimilarityMetric metric = null;
			
			//Setting the metric based on the value of the metricType which is passed
			if (Constants.PEARSON_CORRELATION.equals(metricType)) {
				 metric = new PearsonMetric();

			} else if (Constants.PEARSON_CORRELATION_WITH_SIGNIFICANCE_WEIGHTING.equals(metricType)) {
				 metric = new PearsonMetricWithSignificanceWeighting();

			} else if (Constants.COSINE.equals(metricType)) {
				metric = new CosineSimilarityMetric();

			}
			
			// Setting the neighbours to 100
			Neighbourhood neighbourhood = new NearestNeighbourhood(100);

			////////////////////////////////////////////////
			// Evaluates the CF algorithm (do not change!!):
			// - the RMSE (if actual ratings are available) and coverage are
			//////////////////////////////////////////////// output to screen
			// - output file is created
			DatasetReader reader = new DatasetReader(itemFile, itemGenomeScoresFile, trainFile, testFile);
			ItemBasedCF ibcf = new ItemBasedCF(predictor, neighbourhood, metric, reader);
			Evaluator eval = new Evaluator(ibcf, reader.getTestData());

			// Write to output file
			eval.writeResults(outputFile);

			// Display RMSE and coverage
			Double RMSE = eval.getRMSE();
			if (RMSE != null)
				System.out.printf("RMSE for neighour count : %.6f\n", RMSE);

			double coverage = eval.getCoverage();
			System.out.printf("coverage: %.2f%%\n", coverage);
		
		System.out.println("########################### End of Prediction using "+metricType+" ##########################");
		
	}
}
