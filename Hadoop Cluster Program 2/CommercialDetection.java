package com.classification;

import java.io.IOException;
import java.net.URI;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.IndexToString;
import org.apache.spark.ml.feature.LabeledPoint;
import org.apache.spark.ml.feature.MinMaxScaler;
import org.apache.spark.ml.feature.MinMaxScalerModel;
import org.apache.spark.ml.feature.PCA;
import org.apache.spark.ml.feature.PCAModel;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.ml.feature.VectorIndexer;
import org.apache.spark.ml.feature.VectorIndexerModel;
import org.apache.spark.ml.linalg.DenseVector;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class CommercialDetection {

	static StringBuilder outputFileContent = new StringBuilder();
	private static final String HDFS_URI = "hdfs://co246a-a.ecs.vuw.ac.nz:9000";
	private static final String DC = "DecisionTreeClassifier";
	private static final String RF = "RandomForestClassifier";

	public static void main(String[] args) {

		if (null != args && 0 == args.length) {
			outputFileContent.append("Please the Dataset.");
			outputFileContent.append("Provide arguments as <DataSet> <Seed_value> <hdfs output filename>");
			System.out.println(outputFileContent.toString());
			if (null != args && 3 == args.length) {
				writeToHDFS(args[2]);
			}
			System.exit(0);
		}
		try {
			SparkSession spark;
			if (null != args && 4 == args.length && args[3].equalsIgnoreCase("local")) {
				spark = SparkSession.builder().master("local").appName("News Channel Commercial Detection").getOrCreate();
			} else {
				spark = SparkSession.builder().appName("News Channel Commercial Detection").getOrCreate();
			}

			/**
			 * The path can be either a single text file or a directory storing text files
			 */
			JavaRDD<String> lines = spark.sparkContext().textFile(args[0], 1).toJavaRDD();
			JavaRDD<LabeledPoint> linesRDD = lines.map(line -> {
				String[] featureTokens = line.split(",");
				int featureLength = featureTokens.length - 1;
				double[] features = new double[featureLength];
				for (int i = 0; i < featureLength; i++) {
					features[i] = Double.parseDouble(featureTokens[i]);
				}
				Vector v = new DenseVector(features);

				double label = Double.parseDouble(featureTokens[featureLength]);
				return new LabeledPoint(label, v);
			});
			Dataset<Row> dataSet = spark.createDataFrame(linesRDD, LabeledPoint.class);
			long seed = (1 <= args.length && 2 == args[1].length()) ? new Random().nextLong() : Integer.parseInt(args[1]);
			startClassificationSuite(dataSet, seed);
			System.out.println(outputFileContent.toString());

			if (null != args && 3 == args.length) {
				writeToHDFS(args[2]);
			}
			spark.stop();
		} catch (Exception e) {
			outputFileContent.append("main :: Exception occured: " + e.getLocalizedMessage() + "\n");
			System.out.println(outputFileContent.toString());
		}
	}

	private static void writeToHDFS(String fileName) {

		/**
		 * Check if write to external file is used by the user
		 */

		FSDataOutputStream outputStream = null;
		Configuration config = new Configuration();
		config.set("fs.defaultFs", HDFS_URI);
		try {
			FileSystem fs = FileSystem.get(URI.create(HDFS_URI), config);
			Path hdfsPath = new Path(HDFS_URI + fileName);

			/**
			 * Checking if the file exists or else create the file in that path
			 */
			if (fs.exists(hdfsPath)) {
				outputStream = fs.append(hdfsPath);
			} else {
				outputStream = fs.create(hdfsPath);
			}
			/**
			 * Write Content to file
			 */
			System.out.println("Output written to file: " + HDFS_URI + fileName);
			outputStream.writeBytes(outputFileContent.toString());
		} catch (IOException e) {
			System.out.println("IO Eception Occured " + e.getLocalizedMessage());
		} finally {
			try {
				if (null != outputStream)
					outputStream.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 
	 * @param dataSet
	 * @param seed
	 */
	private static void startClassificationSuite(Dataset<Row> dataSet, long seed) {
		runClassifier(dataSet, seed);
		scaleDataSet(dataSet, seed);
		performPCA(dataSet, seed);
	}

	private static void runClassifier(Dataset<Row> dataSet, long seed) {

		/**
		 * Original data set
		 */

		outputFileContent.append("--------------------------------------------------------");
		outputFileContent.append("\n| Commercial Detection Classifier : With Original Data |\n");
		outputFileContent.append("--------------------------------------------------------\n");

		String featureType = "features";
		classifier(dataSet, seed, featureType, DC);
		classifier(dataSet, seed, featureType, RF);

	}

	private static void scaleDataSet(Dataset<Row> dataSet, long seed) {

		/**
		 * Scaling the data set
		 */

		outputFileContent.append("------------------------------------------------------");
		outputFileContent.append("\n| Commercial Detection Classifier : With Scaled Data |\n");
		outputFileContent.append("------------------------------------------------------\n");

		String featureType = "scaledfeatures";
		MinMaxScaler scaler = new MinMaxScaler().setInputCol("features").setOutputCol("scaledfeatures");
		MinMaxScalerModel scalerModel = scaler.fit(dataSet);
		Dataset<Row> scaledDataSet = scalerModel.transform(dataSet);
		classifier(scaledDataSet, seed, featureType, DC);
		classifier(scaledDataSet, seed, featureType, RF);

	}

	private static void performPCA(Dataset<Row> dataSet, long seed) {

		/**
		 * Performing PCA on the data set
		 */

		outputFileContent.append("---------------------------------------------------------------");
		outputFileContent.append("\n| Commercial Detection Classifier : With PCA transformed Data |\n");
		outputFileContent.append("---------------------------------------------------------------\n");
		
		String featureType = "pcafeatures";
		PCAModel pca = new PCA().setInputCol("features").setOutputCol("pcafeatures").setK(10).fit(dataSet);
		Dataset<Row> pcaDataSet = pca.transform(dataSet);
		classifier(pcaDataSet, seed, featureType, DC);
		classifier(pcaDataSet, seed, featureType, RF);

	}

	private static void classifier(Dataset<Row> dataSet, long seed, String feature, String testType) {

		try {
			StringBuilder classifierOutput = new StringBuilder();
			long startTime = System.currentTimeMillis();

			/**
			 * Index labels, adding metadata to the label column. Fit on whole dataset to
			 * include all labels in index.
			 */

			StringIndexerModel labelIndexer = new StringIndexer().setInputCol("label").setOutputCol("indexedLabel")
					.fit(dataSet);

			/**
			 * Automatically identify categorical features, and index them.
			 */
			VectorIndexerModel featureIndexer = new VectorIndexer().setInputCol(feature).setOutputCol("indexedFeatures")
					.setMaxCategories(4) // features with > 4 distinct values are treated as continuous
					.fit(dataSet);

			/**
			 * Split the data into training and test sets (30% held out for testing)
			 */
			Dataset<Row>[] splits = dataSet.randomSplit(new double[] { 0.7, 0.3 }, seed);
			Dataset<Row> trainingData = splits[0];
			Dataset<Row> testData = splits[1];

			/**
			 * Convert indexed labels back to original labels.
			 */
			IndexToString labelConverter = new IndexToString().setInputCol("prediction").setOutputCol("predictedLabel")
					.setLabels(labelIndexer.labels());

			Pipeline pipeline = null;
			String testName = "";
			/**
			 * Declaring train Classifier model instance Chain indexers and tree in a
			 * Pipeline.
			 */

			switch (testType) {

			case DC:
				DecisionTreeClassifier dt = new DecisionTreeClassifier().setLabelCol("indexedLabel")
						.setFeaturesCol("indexedFeatures");
				pipeline = new Pipeline()
						.setStages(new PipelineStage[] { labelIndexer, featureIndexer, dt, labelConverter });
				testName = "Decision Tree Classifier";
				break;
			case RF:
				RandomForestClassifier rf = new RandomForestClassifier().setLabelCol("indexedLabel")
						.setFeaturesCol("indexedFeatures");
				pipeline = new Pipeline()
						.setStages(new PipelineStage[] { labelIndexer, featureIndexer, rf, labelConverter });
				testName = "Random Forest Classifier";
				break;

			}

			/**
			 * Train model. This also runs the indexers.
			 */
			PipelineModel model = pipeline.fit(trainingData);

			/**
			 * Make predictions.
			 */
			Dataset<Row> predictions = model.transform(testData);
			Dataset<Row> predictionsTest = model.transform(trainingData);

			/**
			 * Select (prediction, true label) and compute test error.
			 */
			MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
					.setLabelCol("indexedLabel").setPredictionCol("prediction").setMetricName("accuracy");
			double accuracy = evaluator.evaluate(predictions);
			double accuracyTest = evaluator.evaluate(predictionsTest);
			long endTime = System.currentTimeMillis() - startTime;

			classifierOutput.append("---------------------------------------\n" + testName + " : \n");

			classifierOutput.append("---------------------------------------\n");
			classifierOutput.append("Seed  : " + (seed) + "\n");
			classifierOutput.append("Training Accuracy : " + String.format("%.4f", accuracyTest * 100) + "%" + "\n");
			classifierOutput.append("Training Error : " + String.format("%.4f", (1.0 - accuracyTest)) + "\n");
			classifierOutput.append("Test Accuracy : " + String.format("%.4f", accuracy * 100) + "%" + "\n");
			classifierOutput.append("Test Error : " + String.format("%.4f", (1.0 - accuracy)) + "\n");
			classifierOutput.append("Running Time : " + (endTime / 1000) + "s\n");
			classifierOutput.append("---------------------------------------\n");
			outputFileContent.append(classifierOutput);
		} catch (Exception e) {
			outputFileContent
					.append("classifier : " + testType + " :: Exception occured: " + e.getLocalizedMessage() + "\n");
			System.out.println(outputFileContent.toString());
		}
	}

}
