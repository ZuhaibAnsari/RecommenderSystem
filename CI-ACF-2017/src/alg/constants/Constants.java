package alg.constants;

import java.io.File;

/**
 * This is the class containing all the constants
 * @author Zuhaib
 *
 */
public class Constants {
	public static final String OUTPUT_FILE_PATH = "results" + File.separator;
	public static final String INPUT_FILE_PATH = "ml-20m" + File.separator;
	public static final String NON_PERSONALISED = "Non Personalised";
	public static final String SIMPLE_AVERAGE = "Simple Average";
	public static final String WEIGHTED_AVERAGE = "Weighted Average";
	public static final String DEVIATION_FROM_MEAN = "Deviation From Mean";

	public static final String PEARSON_CORRELATION = "Pearson";
	public static final String PEARSON_CORRELATION_WITH_SIGNIFICANCE_WEIGHTING = "Pearson With Weighting";
	public static final String COSINE = "Cosine";

	public static final String OUTPUT_FILE_NAME = OUTPUT_FILE_PATH + "predictions.txt";
	public static final String TEST_FILE_NAME = INPUT_FILE_PATH + "test.txt";
	public static final String TRAIN_FILE_NAME = INPUT_FILE_PATH + "train.txt";
	public static final String ITEM_GENOME_SCORES_FILE = INPUT_FILE_PATH + "genome-scores-sample.txt";
	public static final String ITEM_FILE_NAME = INPUT_FILE_PATH + "movies-sample.txt";

}
