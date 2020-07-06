#1
 Credit = read.csv("Credit.csv", header = TRUE)[,-1]
 X = model.matrix(Credit$Balance ~ . * ., Credit)[, -1]
 p = ncol(X)
 sprintf("The number of predictors : %s", p)
#2
 set.seed(1)
 trainSet = sample(1:nrow(X), nrow(X)/2)
 testSet = -trainSet
 y = Credit$Balance
 
 #Calculate test errors for the linear model
 linear.mod = lm(y[trainSet] ~ X[trainSet,])
 linear.pred = coef(linear.mod)[1] + (X[testSet,] %*% coef(linear.mod)[-1])
 linear.error = mean((linear.pred - y[testSet])^2)

#3
 install.packages("glmnet")
 library(glmnet)
 grid = 10^seq(3,-1,length=100)
 set.seed(1)
 cv.out= cv.glmnet(X[trainSet,],y[trainSet],alpha=0,lambda = grid, nfolds=10, thresh=1e-10)
 ridge.bestlam = cv.out$lambda.min
 sprintf("The tuning parameter for the ridge regression model : %s", ridge.bestlam)
 
 #Calculate test errors for the ridge model
 ridge.pred = predict(cv.out, s=ridge.bestlam, newx=X[testSet,])
 ridge.error = mean((ridge.pred - y[testSet])^2)

 ridge.out=glmnet(X,y,alpha=0, lambda = grid)
 ridge.coef=predict (ridge.out ,type="coefficients",s= ridge.bestlam) [1:66,]
 ridge.size = nnzero(ridge.coef)
 sprintf("Features selected by the ridge: %s ", ridge.size - 1 )
 
#4
 set.seed(1)
 lasso.cv.out= cv.glmnet(X[trainSet,],y[trainSet],alpha=1,lambda = grid, nfolds=10, thresh=1e-10)
 lasso.bestlam = lasso.cv.out$lambda.min
 sprintf("The tuning parameter for the lasso regression model : %s", lasso.bestlam)
 
 #Calculate test errors for the lasso model
 lasso.pred = predict(lasso.cv.out, s=lasso.bestlam, newx=X[testSet,])
 lasso.error = mean((lasso.pred - y[testSet])^2)
 
 lasso.out=glmnet(X,y,alpha=1, lambda=grid)
 lasso.coef=predict (lasso.out ,type="coefficients",s= lasso.bestlam) [1:66,]
 lasso.size = nnzero(lasso.coef)
 sprintf("Features selected by the lasso: %s ", lasso.size - 1 )
 
 #5
 plot(coef(linear.mod)[-1], ylab = "coefficients", xlab = "features", pch=20, cex=1, type="o")
 points(lasso.coef[-1], pch=20, cex=1, col = "blue", type="o")
 legend("bottomright", c("Linear Regression", "Lasso Regression"), fill = c("black", "blue"))
 plot(coef(linear.mod)[-1], ylab = "coefficients", xlab = "features", pch=20, cex=1, type="o")
 points(ridge.coef[-1], pch=20, cex=1, col = "red", type="o")
 legend("bottomright", c("Linear Regression", "Ridge Regression"), fill = c("black", "red"))
 
 #6. 
 
 sprintf("The test errors for the Linear model: %s ", linear.error )
 sprintf("The test errors for the Ridge model: %s ", ridge.error )
 sprintf("The test errors for the Lasso model: %s ", lasso.error )
 

 #7
 plot(y[testSet],linear.pred,ylim=c(-500,2000),xlab="y_test",ylab="predicted")
 points(y[testSet],ridge.pred,col="red")
 points(y[testSet],lasso.pred,col="blue")
 abline(0,1)
 legend("topleft", c("Linear Regression", "Ridge Regression", "Lasso Regression"), fill = c("black", "red", "blue"))