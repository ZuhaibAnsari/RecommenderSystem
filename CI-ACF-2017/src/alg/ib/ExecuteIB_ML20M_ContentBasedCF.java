package alg.ib;

import alg.constants.Constants;
import alg.ib.neighbourhood.NearestNeighbourhoodWithSimilarityThresholding;
import alg.ib.neighbourhood.Neighbourhood;
import alg.ib.predictor.ContentBasedCosinePredictor;
import alg.ib.predictor.Predictor;
import similarity.metric.CosineSimilarityMetricForContentBased;
import similarity.metric.SimilarityMetric;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

/**
 * This class contains the experiments for the Content based Predictions
 * @author Zuhaib
 *
 */
public class ExecuteIB_ML20M_ContentBasedCF {
	public static void main(String[] args)
	{
		// set the paths and filenames of the item file, genome scores file,
		// train file and test file using names declared in Constants file
		String itemFile = Constants.ITEM_FILE_NAME;
		String itemGenomeScoresFile = Constants.ITEM_GENOME_SCORES_FILE;
		String trainFile = Constants.TRAIN_FILE_NAME;
		String testFile = Constants.TEST_FILE_NAME;

		// set the path and filename of the output file ...
		String outputFile = Constants.OUTPUT_FILE_NAME;
		
		//Calculating the prediction and accuracy when the ratings are greater than 4 , the last parameter in the method call
		// is set to true which specifies if the ratings greater than 4 are only to be considered
		generateContentBasedPredictionsAndCalculateAccuracy(itemFile, itemGenomeScoresFile, trainFile, testFile, outputFile,true);		
	
		//Generating a new prediction file for the results where ratings are not bounded by a rating cap of 4
		outputFile=Constants.OUTPUT_FILE_PATH+"prediction_2.txt";
		generateContentBasedPredictionsAndCalculateAccuracy(itemFile, itemGenomeScoresFile, trainFile, testFile, outputFile,false);		

	}

	/**
	 * This method is used to calculate the coverage and generate the predictions
	 * @param itemFile
	 * @param itemGenomeScoresFile
	 * @param trainFile
	 * @param testFile
	 * @param outputFile
	 * @param isRatingGreaterThanFour  - this paramter specifies if only ratings greater than 4 are to be taken into consideration
	 */
	private static void generateContentBasedPredictionsAndCalculateAccuracy(String itemFile, String itemGenomeScoresFile, String trainFile, String testFile,
			String outputFile, boolean isRatingGreaterThanFour) {
		
		
		// configure the content-based CF algorithm - set the predictor, neighborhood and similarity metric ...
		Predictor predictor = new ContentBasedCosinePredictor(isRatingGreaterThanFour);
		
		//Setting the threshold to 10000 so that similarity thresholding doesnt have any effect on it
		Neighbourhood neighbourhood = new NearestNeighbourhoodWithSimilarityThresholding(10000);
		
		SimilarityMetric metric = new CosineSimilarityMetricForContentBased();
		
		////////////////////////////////////////////////
		// Evaluates the CF algorithm (do not change!!):
		// - the RMSE (if actual ratings are available) and coverage are output to screen
		// - output file is created
		DatasetReader reader = new DatasetReader(itemFile, itemGenomeScoresFile, trainFile, testFile);
		ContentBasedCF ibcf = new ContentBasedCF(predictor, neighbourhood, metric, reader);
		Evaluator eval = new Evaluator(ibcf, reader.getTestData());
		
		// Write to output file
		eval.writeResults(outputFile);
		
		// Display RMSE and coverage
		Double RMSE = eval.getRMSE();
		if(RMSE != null) System.out.printf("RMSE: %.6f\n", RMSE);
		
		double coverage = eval.getCoverage();
		System.out.printf("coverage: %.2f%%\n", coverage);
	}
}
