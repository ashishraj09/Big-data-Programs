package com.classifier;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.IndexToString;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.ml.feature.VectorIndexer;
import org.apache.spark.ml.feature.VectorIndexerModel;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

/**
 * @author ashraj
 *
 */
public class DecisionTree {

	/**
	 * @param args[0] : Input dataSet file path with name
	 * @param args[1] : Random seed value
	 * @param args[2] : Optional, Output file path with name
	 */

	private static final String HDFS_URI = "hdfs://co246a-a.ecs.vuw.ac.nz:9000";

	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();
		
		if (null != args && 0 == args.length) {
			System.out.println("Please the Dataset.");
			System.out.println("Provide arguments as <DataSet> <Seed_value> <Output_file (Optional)>");
			System.exit(0);
		}

		SparkSession spark = SparkSession.builder()
				//.master("local")
				.appName("Decision Tree Classifier").getOrCreate();
		/**
		 * The path can be either a single text file or a directory storing text files
		 */
		Dataset<Row> dataFile = spark.read().format("csv").option("sep", ",").option("inferSchema", "true")
				.option("header", "false").load(args[0]).dropDuplicates();
		/**
		 * Feature extraction and separating label from features
		 */
		StructType schema = new StructType(
				new StructField[] { new StructField("label", DataTypes.StringType, true, Metadata.empty()),
						new StructField("features", new VectorUDT(), true, Metadata.empty()), });
		Dataset<Row> dataSet = dataFile.map((MapFunction<Row, Row>) row -> {
			double[] vectors = new double[row.length() - 1];
			for (int i = 0; i < (row.length() - 1); i++) {
				/**
				 * Converting features to double from String and loop only read till the second
				 * last column excluding the label
				 */
				vectors[i] = Double.parseDouble(row.get(i).toString());
			}
			return RowFactory.create(row.get(row.length() - 1).toString(), Vectors.dense(vectors));
		}, RowEncoder.apply(schema));

		/**
		 * Index labels, adding metadata to the label column. Fit on whole dataset to
		 * include all labels in index.
		 */

		StringIndexerModel labelIndexer = new StringIndexer().setInputCol("label").setOutputCol("indexedLabel")
				.fit(dataSet);

		/**
		 * Automatically identify categorical features, and index them.
		 */
		VectorIndexerModel featureIndexer = new VectorIndexer().setInputCol("features").setOutputCol("indexedFeatures")
				.setMaxCategories(4) // features with > 4 distinct values are treated as continuous
				.fit(dataSet);

		long seed = args[1].isEmpty() ? 1 : Long.parseLong(args[1]);
		/**
		 * Split the data into training and test sets (30% held out for testing)
		 */
		Dataset<Row>[] splits = dataSet.randomSplit(new double[] { 0.7, 0.3 }, seed);
		Dataset<Row> trainingData = splits[0];
		Dataset<Row> testData = splits[1];

		/**
		 * Train a DecisionTree model
		 */
		DecisionTreeClassifier dt = new DecisionTreeClassifier().setLabelCol("indexedLabel")
				.setFeaturesCol("indexedFeatures");

		/**
		 * Convert indexed labels back to original labels.
		 */
		IndexToString labelConverter = new IndexToString().setInputCol("prediction").setOutputCol("predictedLabel")
				.setLabels(labelIndexer.labels());

		/**
		 * Chain indexers and tree in a Pipeline.
		 */
		Pipeline pipeline = new Pipeline()
				.setStages(new PipelineStage[] { labelIndexer, featureIndexer, dt, labelConverter });

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

		StringBuilder sb = new StringBuilder("------------------------------\nDecision Tree Classifier\n");

		sb.append("------------------------------\n");
		sb.append("Seed  : " + (seed) + "\n");
		sb.append("Training Accuracy : " +String.format("%.4f",accuracyTest * 100) + "%" + "\n");
		sb.append("Training Error : " + String.format("%.4f",(1.0 - accuracyTest)) + "\n");
		sb.append("Test Accuracy : " + String.format("%.4f",accuracy * 100) + "%" + "\n");
		sb.append("Test Error : " + String.format("%.4f",(1.0 - accuracy)) + "\n");
		sb.append("Running Time : " + (endTime / 1000) + "s\n");
		sb.append("------------------------------\n");
		System.out.println(sb.toString());
		/**
		 * Check if write to external file is used by the user
		 */
		if (null != args && 3 == args.length) {
			FSDataOutputStream outputStream = null;
			Configuration config = new Configuration();
			config.set("fs.defaultFs", HDFS_URI);
			try {
				FileSystem fs = FileSystem.get(URI.create(HDFS_URI), config);
				Path hdfsPath = new Path(HDFS_URI + args[2]);

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
				System.out.println("Output written to file: " + HDFS_URI + args[2]);
				outputStream.writeBytes(sb.toString());
			} catch (IOException e) {
				System.out.println("IO Eception Occured " + e.getLocalizedMessage());
			} finally {
				try {
					if(null != outputStream)
					outputStream.close();
				} catch (IOException e) {
				}
			}

			spark.stop();

		}

	}

}
