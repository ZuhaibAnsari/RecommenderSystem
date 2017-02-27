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

public class ExecuteIB_ML20M_Experiment1 {
	

	public static void main(String[] args) {

		// set the paths and filenames of the item file, genome scores file,
		// train file and test file using the constants file
		String itemFile = Constants.ITEM_FILE_NAME;
		String itemGenomeScoresFile = Constants.ITEM_GENOME_SCORES_FILE;
		String trainFile = Constants.TRAIN_FILE_NAME;
		String testFile = Constants.TEST_FILE_NAME;

		// set the path and filename of the output file ...
		String outputFile = Constants.OUTPUT_FILE_NAME;

		// Generate predictions using simple average 
		generatePredictionAndCalculateRMSE(itemFile, itemGenomeScoresFile, trainFile, testFile, outputFile,
				Constants.SIMPLE_AVERAGE);
		
		// Generate predictions using weighted average 
		generatePredictionAndCalculateRMSE(itemFile, itemGenomeScoresFile, trainFile, testFile, outputFile,
				Constants.WEIGHTED_AVERAGE);
		
		// Generate predictions using non personalised
		generatePredictionAndCalculateRMSE(itemFile, itemGenomeScoresFile, trainFile, testFile, outputFile,
				Constants.NON_PERSONALISED);
		
		// Generate predictions using deviations from mean
		generatePredictionAndCalculateRMSE(itemFile, itemGenomeScoresFile, trainFile, testFile, outputFile,
				Constants.DEVIATION_FROM_MEAN);

	}

	/**
	 * @param itemFile
	 * @param itemGenomeScoresFile
	 * @param trainFile
	 * @param testFile
	 * @param outputFile
	 */
	private static void generatePredictionAndCalculateRMSE(String itemFile, String itemGenomeScoresFile,
			String trainFile, String testFile, String outputFile, String predictorType) {
		System.out.println("########################### Calculating Prediction using "+predictorType+" ##########################");
		
		// Starting the loop from neighbor count of 5 and incrementing in steps of 5
		for (int neighborCount = 5; neighborCount <= 100; neighborCount += 5) {

			// configure the item-based CF algorithm - set the predictor,
			// neighbourhood and similarity metric ...
			Predictor predictor=null;
			
			//Check to select the right predictor on the basis of value of predictorType 
			if (Constants.SIMPLE_AVERAGE.equals(predictorType)) {
				predictor = new SimpleAveragePredictor();

			} else if (Constants.WEIGHTED_AVERAGE.equals(predictorType)) {
				predictor = new WeightedAveragePredictor();

			} else if (Constants.NON_PERSONALISED.equals(predictorType)) {
				predictor = new NonPersonalisedPredictor();

			} else if (Constants.DEVIATION_FROM_MEAN.equals(predictorType)) {
				predictor = new DeviationsFromMeanPredictor();

			}
			
			SimilarityMetric metric = new CosineSimilarityMetric();
			// Setting the neighbours based on the value of loop variable neighborCount
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
		System.out.println("########################### End of Prediction using "+predictorType+" ##########################");
		
	}
}
