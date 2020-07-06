#Question 1
library(ISLR) 
nci.data = NCI60$data 
X = scale(t(nci.data)) 
P = X %*% prcomp(X)$rotation 
hc.euclidean = hclust(dist(X),method = "complete") 

plot(hc.euclidean,main="Euclidean Distance",xlab="Observations",ylab="Dissimilarity",cex=0.5)

hc.euclidean_3 = cutree(hc.euclidean,3)
hc.euclidean_4 = cutree(hc.euclidean,4)
hc.euclidean_5 = cutree(hc.euclidean,5)
hc.euclidean_6 = cutree(hc.euclidean,6)

table(hc.euclidean_3)
table(hc.euclidean_4)
table(hc.euclidean_5)
table(hc.euclidean_6)

par(mfrow=c(1,4)) 
plot(P[,1:2], main = "Three Clusters", col = hc.euclidean_3+2, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
plot(P[,1:2], main = "Four Clusters", col = hc.euclidean_4+2, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
plot(P[,1:2], main = "Five Clusters", col = hc.euclidean_5+2, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
plot(P[,1:2], main = "Six Clusters", col = hc.euclidean_6+2, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)


#Question 2
 hc.correlation = hclust(as.dist(cor(t(X))), method = "complete")
 plot(hc.correlation,main="Correlation-Based Distance",xlab="Observations",ylab="Dissimilarity",sub="",cex=0.5)

 hc.correlation_3 = cutree(hc.correlation,3)
 hc.correlation_4 = cutree(hc.correlation,4)
 hc.correlation_5 = cutree(hc.correlation,5)
 hc.correlation_6 = cutree(hc.correlation,6)
 
 table(hc.correlation_3)
 table(hc.correlation_4)
 table(hc.correlation_5)
 table(hc.correlation_6)
 
 par(mfrow=c(1,4)) 
 plot(P[,1:2], main = "Three Clusters", col = hc.correlation_3+2, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 plot(P[,1:2], main = "Four Clusters", col = hc.correlation_4+2, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 plot(P[,1:2], main = "Five Clusters", col = hc.correlation_5+2, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 plot(P[,1:2], main = "Six Clusters", col = hc.correlation_6+2, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 
#Question 3
 km_3 = kmeans(X,3)
 km_4 = kmeans(X,4)
 km_5 = kmeans(X,5)
 km_6 = kmeans(X,6)
 
 table(km_3$cluster) 
 table(km_4$cluster) 
 table(km_5$cluster) 
 table(km_6$cluster) 
 
 par(mfrow=c(1,4)) 
 plot(P[,1:2], main = "K-Mean Clustering, k=3", col = km_3$cluster, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 plot(P[,1:2], main = "K-Mean Clustering, k=4", col = km_4$cluster, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 plot(P[,1:2], main = "K-Mean Clustering, k=5", col = km_5$cluster, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 plot(P[,1:2], main = "K-Mean Clustering, k=6", col = km_6$cluster, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 
 par(mfrow=c(1,3)) 
 plot(P[,1:2], main = "Euclidian Distance Cluster", col = hc.euclidean_3+2, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 plot(P[,1:2], main = "Correlation-Based Distance Clusters", col = hc.correlation_3+2, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 plot(P[,1:2], main = "K-Mean Clustering", col = km_3$cluster, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 
 par(mfrow=c(1,3)) 
 plot(P[,1:2], main = "Euclidian Distance Cluster", col = hc.euclidean_4+2, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 plot(P[,1:2], main = "Correlation-Based Distance Clusters", col = hc.correlation_4+2, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 plot(P[,1:2], main = "K-Mean Clustering", col = km_4$cluster, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 
 par(mfrow=c(1,3)) 
 plot(P[,1:2], main = "Euclidian Distance Cluster", col = hc.euclidean_5+2, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 plot(P[,1:2], main = "Correlation-Based Distance Clusters", col = hc.correlation_5+2, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 plot(P[,1:2], main = "K-Mean Clustering", col = km_5$cluster, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 
 par(mfrow=c(1,3)) 
 plot(P[,1:2], main = "Euclidian Distance Cluster", col = hc.euclidean_6+2, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 plot(P[,1:2], main = "Correlation-Based Distance Clusters", col = hc.correlation_6+2, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 plot(P[,1:2], main = "K-Mean Clustering", col = km_6$cluster, pch=20, xlab ="Z1",ylab="Z2", cex=1.5)
 