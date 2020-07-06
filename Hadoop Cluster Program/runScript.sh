#!/bin/sh

echo "Running for user $USER:"
echo "kdd.data expected in HDFS path : hdfs://co246a-a.ecs.vuw.ac.nz:9000/user/$USER/input/kdd.data" 
hdfs dfs -rm -r hdfs://co246a-a.ecs.vuw.ac.nz:9000/user/$USER/output/
echo "Running Decision Tree"

echo "Running Decision Tree Seed - 1"
spark-submit --class "com.classifier.DecisionTree" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 1 /user/$USER/output/DecisionTreeOutput.txt
echo "Running Decision Tree Seed - 2"                                                    
spark-submit --class "com.classifier.DecisionTree" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 2 /user/$USER/output/DecisionTreeOutput.txt
echo "Running Decision Tree Seed - 3"                                                   
spark-submit --class "com.classifier.DecisionTree" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 3 /user/$USER/output/DecisionTreeOutput.txt
echo "Running Decision Tree Seed - 4"                                                   
spark-submit --class "com.classifier.DecisionTree" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 4 /user/$USER/output/DecisionTreeOutput.txt
echo "Running Decision Tree Seed - 5"                                                   
spark-submit --class "com.classifier.DecisionTree" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 5 /user/$USER/output/DecisionTreeOutput.txt
echo "Running Decision Tree Seed - 6"                                                   
spark-submit --class "com.classifier.DecisionTree" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 6 /user/$USER/output/DecisionTreeOutput.txt
echo "Running Decision Tree Seed - 7"                                                  
spark-submit --class "com.classifier.DecisionTree" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 7 /user/$USER/output/DecisionTreeOutput.txt
echo "Running Decision Tree Seed - 8"                                                   
spark-submit --class "com.classifier.DecisionTree" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 8 /user/$USER/output/DecisionTreeOutput.txt
echo "Running Decision Tree Seed - 9"                                                    
spark-submit --class "com.classifier.DecisionTree" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 9 /user/$USER/output/DecisionTreeOutput.txt
echo "Running Decision Tree Seed - 10"                                                
spark-submit --class "com.classifier.DecisionTree" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 10 /user/$USER/output/DecisionTreeOutput.txt

echo "Running Logistic Regression"

echo "Running Logistic Regression Seed - 1"
spark-submit --class "com.classifier.LogisticRegressionCLassifier" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 1 /user/$USER/output/LogisticRegressionOutput.txt
echo "Running Logistic Regression Seed - 2"                                                              
spark-submit --class "com.classifier.LogisticRegressionCLassifier" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 2 /user/$USER/output/LogisticRegressionOutput.txt
echo "Running Logistic Regression Seed - 3"                                                             
spark-submit --class "com.classifier.LogisticRegressionCLassifier" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 3 /user/$USER/output/LogisticRegressionOutput.txt
echo "Running Logistic Regression Seed - 4"                                                             
spark-submit --class "com.classifier.LogisticRegressionCLassifier" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 4 /user/$USER/output/LogisticRegressionOutput.txt
echo "Running Logistic Regression Seed - 5"                                                             
spark-submit --class "com.classifier.LogisticRegressionCLassifier" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 5 /user/$USER/output/LogisticRegressionOutput.txt
echo "Running Logistic Regression Seed - 6"                                                             
spark-submit --class "com.classifier.LogisticRegressionCLassifier" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 6 /user/$USER/output/LogisticRegressionOutput.txt
echo "Running Logistic Regression Seed - 7"                                                             
spark-submit --class "com.classifier.LogisticRegressionCLassifier" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 7 /user/$USER/output/LogisticRegressionOutput.txt
echo "Running Logistic Regression Seed - 8"                                                             
spark-submit --class "com.classifier.LogisticRegressionCLassifier" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 8 /user/$USER/output/LogisticRegressionOutput.txt
echo "Running Logistic Regression Seed - 9"                                                              
spark-submit --class "com.classifier.LogisticRegressionCLassifier" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 9 /user/$USER/output/LogisticRegressionOutput.txt
echo "Running Logistic Regression Seed - 10"                                                            
spark-submit --class "com.classifier.LogisticRegressionCLassifier" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 10 /user/$USER/output/LogisticRegressionOutput.txt

echo "All Process Complete"
echo "Use below command to see output files:"
echo "hdfs dfs -cat hdfs://co246a-a.ecs.vuw.ac.nz:9000/user/$USER/output/DecisionTreeOutput.txt"
echo "hdfs dfs -cat hdfs://co246a-a.ecs.vuw.ac.nz:9000/user/$USER/output/LogisticRegressionOutput.txt" 

hdfs dfs -cat hdfs://co246a-a.ecs.vuw.ac.nz:9000/user/$USER/output/DecisionTreeOutput.txt
hdfs dfs -cat hdfs://co246a-a.ecs.vuw.ac.nz:9000/user/$USER/output/LogisticRegressionOutput.txt