-----------------------------
| Compiling the Java files: |
-----------------------------

1) Download Hadoop and spark supported jars into lib folder from https://github.com/ashishraj09/Big-data-Programs/blob/master/hadoop_2.8.0_jars_files.zip?raw=true
2) Copy this lib folder into the same folder as the java files
3) Run the below command to compile the files 

javac -cp "jars/*" -d Spark_classifier *.java

4) If the ECS machine gives the below error while running the compiled jar. Please change the compile command by adding --release 8.
Error: Unsupported major.minor version 52.0

javac --release 8 -cp "jars/*" -d Spark_classifier *.java

5) Package the compile files to jar
jar cvf SparkClassifier.jar -C Spark_classifier/ .

Running the jar on the ECS Machine : 
1)SSH into co246a-1 machine

ssh co246a-1

2) After ssh into co246a-1, enter command csh into the console
co246a-1% csh
co246a-1: [~] %

3) Navigate to Spark folder obtained after unzipping Spark.zip (Linux Command to unzip a zip file: unzip Spark.zip)

4) Run the SetupSparkClasspath.csh and SetupHadoopClasspath.csh script using source command on the terminal. 

co246a-1: [~] % source SetupSparkClasspath.csh
co246a-1: [~] % source SetupHadoopClasspath.csh

5) Copy the kdd.data file into hdfs file system
co246a-1: [~] % hdfs dfs -mkdir /user/$USER
co246a-1: [~] % hdfs dfs -mkdir /user/$USER/input
co246a-1: [~] % hdfs dfs -put ~/Spark/kdd.data /user/$USER/input/kdd.data
co246a-1: [~] % hdfs dfs -ls hdfs://co246a-a.ecs.vuw.ac.nz:9000/user/$USER/input/

6) Below commands can be used to run the spark programs: 

spark-submit --class "com.classifier.DecisionTree" --master yarn --deploy-mode cluster ~/Spark/SparkClassifier.jar /user/$USER/input/kdd.data 1 /user/$USER/output/DecisionTreeOutput.txt

spark-submit --class "com.classifier.LogisticRegressionCLassifier" --master yarn --deploy-mode cluster SparkClassifier.jar /user/$USER/input/kdd.data 1 /user/$USER/output/LogisticRegressionOutput.txt

Arguments: 
1) Input file
2) Random seed values
3) Output file

7) Alternatively, runScript.sh can used to run both the Spark program 10 times with different seed values. 
co246a-1: [~] % sh runScript.sh > ~/Spark/SampleOutput.log
co246a-1: [~] % cat ~/Spark/SampleOutput.log
