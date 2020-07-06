Cleaned Data
 |-> BBC_Cleaned.txt
 |-> CNN_Cleaned.txt
 |-> CNNIBN_Cleaned.txt
 |-> NDTV_Cleaned.txt
 |-> TIMESNOW_Cleaned.txt

Uncleaned Original Data
 |-> BBC.txt
 |-> CNN.txt
 |-> CNNIBN.txt
 |-> NDTV.txt
 |-> TIMESNOW.txt

-----------------------------
| Compiling the Java files: |
-----------------------------
1) Download Hadoop supported jars into lib folder from https://ecs.wgtn.ac.nz/foswiki/pub/Courses/COMP424_2020T1/Assignments/hadoop_2.8.0_jars_files.zip
2) Copy this jar folder into the same folder as the java files
3) Run the below command to compile the files 

javac -cp "jars/*" -d compile CommercialDetection.java

4) If the ECS machine gives the below error while running the compiled jar. Please change the compile command by adding --release 8.
Error: Unsupported major.minor version 52.0

javac --release 8 -cp "jars/*" -d compile CommercialDetection.java

5) Package the compile files to jar
jar cvf Spark_Cluster.jar -C compile/ .

---------------------------------------
| Running the jar on the ECS Machine :|
---------------------------------------

1)SSH into co246a-1 machine

ssh co246a-1

2) After ssh into co246a-1, enter command csh into the console
co246a-1% csh
co246a-1: [~] %

3) Navigate to Spark folder obtained after unzipping Spark.zip (Linux Command to unzip a zip file: unzip Spark.zip)

4) Run the SetupSparkClasspath.csh and SetupHadoopClasspath.csh script using source command on the terminal. 

co246a-1: [~] % source SetupSparkClasspath.csh
co246a-1: [~] % source SetupHadoopClasspath.csh

5) Copy the dataset files into hdfs file system
co246a-1: [~] % hdfs dfs -mkdir /user/$USER
co246a-1: [~] % hdfs dfs -mkdir /user/$USER/input
co246a-1: [~] % hdfs dfs -put ~/Part2/BBC_Cleaned.csv /user/$USER/input/BBC_Cleaned.csv
co246a-1: [~] % hdfs dfs -put ~/Part2/CNN_Cleaned.csv /user/$USER/input/CNN_Cleaned.csv
co246a-1: [~] % hdfs dfs -put ~/Part2/CNNIBN_Cleaned.csv /user/$USER/input/CNNIBN_Cleaned.csv
co246a-1: [~] % hdfs dfs -put ~/Part2/NDTV_Cleaned.csv /user/$USER/input/NDTV_Cleaned.csv
co246a-1: [~] % hdfs dfs -put ~/Part2/TIMESNOW_Cleaned.csv /user/$USER/input/TIMESNOW_Cleaned.csv
co246a-1: [~] % hdfs dfs -ls hdfs://co246a-a.ecs.vuw.ac.nz:9000/user/$USER/input/

6) Below commands can be used to run the spark programs: 

spark-submit --class "com.classification.CommercialDetection" --master yarn --deploy-mode cluster Spark_Cluster.jar /user/$USER/input/NDTV_Cleaned.csv 5 /user/$USER/output/ClassifierOutput.txt

Arguments: 
1) Input file
2) Random seed value
3) Ouptut file
4) local (Optional, to run the code locally)

We have five input file
-> BBC_Cleaned.csv
-> CNN_Cleaned.csv
-> CNNIBN_Cleaned.csv
-> NDTV_Cleaned.csv
-> TIMESNOW_Cleaned.csv

7) Use below command to view the program output
co246a-1: [~] % hdfs dfs -cat hdfs://co246a-a.ecs.vuw.ac.nz:9000/user/$USER/output/ClassifierOutput.txt

---------------------------------------------------
| Compiling the and running CleanData Java files: |
---------------------------------------------------

javac CleanData2.java
java CleanData2 NDTV.csv

Arguments: 
1) The input file to be cleaned.
The program will automatically create the output file.