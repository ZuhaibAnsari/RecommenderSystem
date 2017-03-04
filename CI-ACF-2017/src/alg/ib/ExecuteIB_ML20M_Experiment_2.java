package alg.ib;

import alg.constants.Constants;
import alg.ib.neighbourhood.NearestNeighbourhoodWithSimilarityThresholding;
import alg.ib.neighbourhood.Neighbourhood;
import alg.ib.predictor.DeviationsFromMeanPredictor;
import alg.ib.predictor.Predictor;
import similarity.metric.CosineSimilarityMetric;
import similarity.metric.SimilarityMetric;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

/**
 * This class is used to run the tasks for experiment 2
 * @author Zuhaib
 *
 */
public class ExecuteIB_ML20M_Experiment_2 {
	
	private static final String DEVIATIONS_FROM_MEAN_USING_NEIGHBOURHOOD_THRESHOLDING = "Deviations_From_Mean_Using_Neighbourhood_Thresholding";

	public static void main(String[] args) {

		// set the paths and filenames of the item file, genome scores file,
		// train file and test file from the values taken form constant file
		String itemFile = Constants.ITEM_FILE_NAME;
		String itemGenomeScoresFile = Constants.ITEM_GENOME_SCORES_FILE;
		String trainFile = Constants.TRAIN_FILE_NAME;
		String testFile = Constants.TEST_FILE_NAME;

		// set the path and filename of the output file ...
		String  outputFile = Constants.OUTPUT_FILE_PATH+DEVIATIONS_FROM_MEAN_USING_NEIGHBOURHOOD_THRESHOLDING+Constants.OUTPUT_FILE_EXTENSION;

		//Generate predictions using deviation from mean and neighbor similarity thresholding
		generatePredictionAndCalculateRMSE(itemFile, itemGenomeScoresFile, trainFile, testFile, outputFile);

	}

	/**
	 * This is the method which takes in all the input parameters and calcualtes the predicted rating using
	 * similarity thresholding using Deviations from Item mean predictor
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
	 */
	private static void generatePredictionAndCalculateRMSE(String itemFile, String itemGenomeScoresFile,
			String trainFile, String testFile, String outputFile) {
		System.out.println("########################### Calculating Prediction using Deviations from Item Mean with Similarity Thresholding ##########################");
		
		//Setting the similarity thresholds from 0 till 0.8 in steps of 0.05
		for (double similarityThresholdValue = 0; similarityThresholdValue <= 0.85; similarityThresholdValue += 0.05) {

			// configure the content-based CF algorithm - set the predictor,
			// neighbourhood and similarity metric ...
			
			//choosing the predictor
			Predictor predictor= new DeviationsFromMeanPredictor();
			
			SimilarityMetric metric = new CosineSimilarityMetric();
			// Setting the neighbours using the similarity threshold value
			Neighbourhood neighbourhood = new NearestNeighbourhoodWithSimilarityThresholding(similarityThresholdValue);

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
				System.out.printf("RMSE for neighour count " + similarityThresholdValue + ": %.6f\n", RMSE);

			double coverage = eval.getCoverage();
			System.out.printf("coverage: %.2f%%\n", coverage);
		}
		System.out.println("########################### End of Prediction using Deviations from Item Mean using Similarity Thresholding ##########################");
		
	}
}
