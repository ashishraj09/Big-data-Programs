
# Big-data-Programs
Big data program using Spark MLlib & ML libraries and R Programming language
---
1) **R Programming**

  - **Regression** : we considered the case in which the features/predictors appeared only linearly in the model. For the Credit dataset, ridge regression
and the lasso made only a small improvement over the ordinary linear model.
The simplest type of nonlinearity we could add to the model is pairwise
interactions of the features. If xj and xk are distinct features, this means we
also consider xjxk as a feature. Pairwise interactions are rather straightforward
to implement in R:
X = model.matrix(balance ∼ . ∗ ., Credit)[, −1] (1) 
becomes the new design matrix. The construction . ∗ . means consider all pairwise multiplications of distinct features.
- **Generalised additive models** : we looked at the Credit dataset, restricting our attention to the features income, limit and student as predictors for balance In this question, we are going to replace limit with age in the analysis. Specifically, include student as before and incorporate the effect of income in terms of a natural spline with 4 degrees of freedom. Use a natural spline to also account for the nonlinear effect of age.
- **Regression with high-dimensional data** : The dataset parkinsons.csv contains information on 42 patients with Parkinson’s disease. The outcome of interest is UPDRS, which is the total unified Parkinson’s disease rating scale. The first 96 features, X1-X96, have been derived from audio recordings of speech tests (i.e. there has already been a process of feature extraction) while feature X97 is already known to be informative for UPDRS. This dataset has been derived from the Oxford Parkinson’s Disease Telemonitoring Dataset available at: http://archive.ics.uci.edu/ml/datasets/Parkinsons+Telemonitoring. Read in the dataset and set up the model matrix X. (You do not need to consider pairwise interactions of the features) Standardise the model matrix before your analysis: X = scale(X) so that the absolute size of the estimated coefficients will give a measure of the relative importance of the features in the fitted model. Next, randomly split the data in a training set with 30 patients and a test set with 12 patients.
- **Clustering** : The NCI60 dataset consists of p = 6,830 gene expression measurements for each of n = 64 cancer cell lines. (See also ISLR 10.6.2) To start with, enter the following commands in R: > library(ISLR) > nci.data = NCI60$data We are going to be interested in clustering the features (genes) rather than the observations (cancer cell lines). In order to do this, enter the following commands: > X = scale(t(nci.data)) > P = X %*% prcomp(X)$rotation X will be the data matrix of interest; P is its the principal components. You will use the the first 2 principal components, i.e. the first 2 columns of P, to graphically depict the clusters.
---
2) **Hadoop Cluster Program**
- Spark MLlib and Spark ML are libraries that support scalable machine learning and data mining algorithms such as classification, clustering, collaborative filtering and frequent pattern mining.
- We Implement two programs that apply Spark’s Decision Tree algorithm and Logistic Regression algorithm to the provided KDD dataset. Run these programs 10 times on the school’s Hadoop cluster, each using a different seed to split the dataset into a training and a test set.
---
3) **Hadoop Cluster Program 2**

**TV News Channel Commercial Detection:** Commercials can take up to 40-60% of the total airtime. This dataset aims to detect the commercial between news videos. This dataset was collected by Dr. Prithwijit Guha , Raghvendra D. Kannao, and Ravishankar Soni from the Indian Institute of Technology, Guwahati, India. The manual classification of commercials from a thousand TV channels is timeconsuming and not economical. Hence, the need for a machine learning-based method. 
**Dataset Source:** https://archive.ics.uci.edu/ml/datasets/TV+News+Channel+Commercial+Detection+Dataset The dataset has 12 main features with many sub-features making 4125 features in total. These 12 main features are further divided into smaller features in the dataset as each of them helps to classify the data. Overall, there are 4125 features. Labels: +1/-1 ( Commercials/Non-Commercials ) 
**Features:** 
1: Shot Length 
2 - 3: Motion Distribution( Mean and Variance) 
4 - 5: Frame Difference Distribution ( Mean and Variance) 
6 - 7: Short time energy ( Mean and Variance) 
8 - 9: ZCR( Mean and Variance) 
10 - 11: Spectral Centroid ( Mean and Variance) 
12 - 13: Spectral Roll-off ( Mean and Variance) 
14 - 15: Spectral Flux ( Mean and Variance) 
16 - 17: Fundamental Frequency ( Mean and Variance) 
18 - 58: Motion Distribution ( 40 bins) 
59 - 91: Frame Difference Distribution ( 32 bins) 
92 - 122: Text area distribution ( 15 bins Mean and 15 bins for variance ) 
123 - 4123: Bag of Audio Words ( 4000 bins) 
4124 - 4125: Edge change Ratio ( Mean and Variance) 

**Objective:** 
-
- Classify and identify is a given video clip is a commercial or not a commercial. As the new videos have still and changing footage it can be difficult to classify them as news content or a commercial. There the dataset has 12 main features with many sub-features to classify more efficiently.

- Implemeting Random Forest and Decision Tree classifier using Spark MLlib and Spark ML libraries with normalising/scaling the data and transforming data using PCA.
---
