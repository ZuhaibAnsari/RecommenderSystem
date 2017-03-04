package alg.ib;

import alg.constants.Constants;
import alg.ib.neighbourhood.NearestNeighbourhood;
import alg.ib.neighbourhood.Neighbourhood;
import alg.ib.predictor.DeviationsFromMeanPredictor;
import alg.ib.predictor.NonPersonalisedPredictor;
import alg.ib.predictor.Predictor;
import alg.ib.predictor.SimpleAveragePredictor;
import alg.ib.predictor.WeightedAveragePredictor;
import similarity.metric.CosineSimilarityMetric;
import similarity.metric.SimilarityMetric;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

/**
 * This is the class to trigger the 1st Experiment where we calculate the
 * predictors performance using varying Neighbourhood sizes .
 * 
 * @author Zuhaib
 *
 */
public class ExecuteIB_ML20M_Experiment_1 {

	private static final String DEVIATION_FROM_MEAN_WITH_NEIGHBOURHOOD = "Deviation_From_Mean_With_Neighbourhood";
	private static final String NON_PERSONALISED_WITH_NEIGHBOURHOOD = "Non_Personalised_With_Neighbourhood";
	private static final String WEIGHTED_AVERAGE_WITH_NEIGHBOURHOOD = "Weighted_Average_With_Neighbourhood";
	private static final String SIMPLE_AVERAGE_WITH_NEIGHBOURHOOD = "Simple_Average_With_Neighbourhood";

	public static void main(String[] args) {

		// set the paths and filenames of the item file, genome scores file,
		// train file and test file using the constants file
		String itemFile = Constants.ITEM_FILE_NAME;
		String itemGenomeScoresFile = Constants.ITEM_GENOME_SCORES_FILE;
		String trainFile = Constants.TRAIN_FILE_NAME;
		String testFile = Constants.TEST_FILE_NAME;

		// Generate predictions using simple average
		// set the path and filename of the output file ...
		String outputFile = Constants.OUTPUT_FILE_PATH + SIMPLE_AVERAGE_WITH_NEIGHBOURHOOD
				+ Constants.OUTPUT_FILE_EXTENSION;
		generatePredictionAndCalculateRMSE(itemFile, itemGenomeScoresFile, trainFile, testFile, outputFile,
				Constants.SIMPLE_AVERAGE);

		// Generate predictions using weighted average
		outputFile = Constants.OUTPUT_FILE_PATH + WEIGHTED_AVERAGE_WITH_NEIGHBOURHOOD + Constants.OUTPUT_FILE_EXTENSION;
		generatePredictionAndCalculateRMSE(itemFile, itemGenomeScoresFile, trainFile, testFile, outputFile,
				Constants.WEIGHTED_AVERAGE);

		// Generate predictions using non personalised

		outputFile = Constants.OUTPUT_FILE_PATH + NON_PERSONALISED_WITH_NEIGHBOURHOOD + Constants.OUTPUT_FILE_EXTENSION;
		generatePredictionAndCalculateRMSE(itemFile, itemGenomeScoresFile, trainFile, testFile, outputFile,
				Constants.NON_PERSONALISED);

		// Generate predictions using deviations from mean
		outputFile = Constants.OUTPUT_FILE_PATH + DEVIATION_FROM_MEAN_WITH_NEIGHBOURHOOD
				+ Constants.OUTPUT_FILE_EXTENSION;
		generatePredictionAndCalculateRMSE(itemFile, itemGenomeScoresFile, trainFile, testFile, outputFile,
				Constants.DEVIATION_FROM_MEAN);

	}

	/**
	 * This is the method which takes in all the input parameters along with a
	 * predictor type based on which it will choose the predictor on the fly and
	 * execute , this is to make the code more modular and resuable
	 * 
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
	 * @param predictorType 
	 * 			  is the paramter which is used to select the predictor type to be used with the experiment           
	 */
	private static void generatePredictionAndCalculateRMSE(String itemFile, String itemGenomeScoresFile,
			String trainFile, String testFile, String outputFile, String predictorType) {
		System.out.println("########################### Calculating Prediction using " + predictorType
				+ " ##########################");

		// Starting the loop from neighbor count of 5 and incrementing in steps
		// of 5
		for (int neighborCount = 5; neighborCount <= 100; neighborCount += 5) {

			// configure the item-based CF algorithm - set the predictor,
			// neighbourhood and similarity metric ...
			Predictor predictor = null;

			// Check to select the right predictor on the basis of value of
			// predictorType
			if (Constants.SIMPLE_AVERAGE.equals(predictorType)) {
				predictor = new SimpleAveragePredictor();

			} else if (Constants.WEIGHTED_AVERAGE.equals(predictorType)) {
				predictor = new WeightedAveragePredictor();

			} else if (Constants.NON_PERSONALISED.equals(predictorType)) {
				predictor = new NonPersonalisedPredictor();

			} else if (Constants.DEVIATION_FROM_MEAN.equals(predictorType)) {
				predictor = new DeviationsFromMeanPredictor();

			}

			// Setting the similarity metric to be cosine similarity
			SimilarityMetric metric = new CosineSimilarityMetric();
			// Setting the neighbours based on the value of loop variable
			// neighborCount
			Neighbourhood neighbourhood = new NearestNeighbourhood(neighborCount);

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
				System.out.printf("RMSE for neighour count " + neighborCount + ": %.6f\n", RMSE);

			double coverage = eval.getCoverage();
			System.out.printf("coverage: %.2f%%\n", coverage);

		}
		System.out.println(
				"########################### End of Prediction using " + predictorType + " ##########################");

	}
}
