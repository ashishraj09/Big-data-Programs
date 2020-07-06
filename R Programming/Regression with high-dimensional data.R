#Question 1
 parkinsons = read.csv("parkinsons.csv", header = TRUE)
 X = model.matrix(parkinsons$UPDRS ~.,parkinsons)[, -1]
 X = scale(X)
 y = parkinsons$UPDRS
 set.seed(1)
 trainSet = sample(1:nrow(X), nrow(X)-12)
 testSet = -trainSet
 linear.mod = lm(y[trainSet] ~ X[trainSet,])
 linear.pred = coef(linear.mod)[1]+X[testSet,] %*% coef(linear.mod)[-1]
 linear.error = mean((linear.pred - y[testSet])^2)
 sprintf("The test errors for the Linear model: %s ", linear.error )
 summary(linear.mod)
 
 #Question 2
 install.packages("glmnet")
 library(glmnet)
 grid = 10^seq(3,-1,length=100)
 set.seed(1)
 cv.out= cv.glmnet(X[trainSet,],y[trainSet],alpha=1,lambda = grid, nfolds=40, thresh=1e-10)
 lasso.bestlam = cv.out$lambda.min
 sprintf("The optimal value of lambda : %s", lasso.bestlam)
 
 #Calculate test errors for the lasso model
 lasso.pred = predict(cv.out, s=lasso.bestlam, newx=X[testSet,])
 lasso.error = mean((lasso.pred - y[testSet])^2)
 sprintf("The test errors for the Lasso model: %s ", lasso.error )
 lasso.out=glmnet(X,y,alpha=1, lambda = grid)
 lasso.coef=predict (lasso.out ,type="coefficients",s= lasso.bestlam) [1:98,] 
 lasso.size = nnzero(lasso.coef)
 sprintf("Number of features selected by the lasso: %s ", lasso.size - 1 )

 #Question 3
 lasso.coef
 
#Question 4
set.seed(1)
trainSet = sample(1:nrow(X), nrow(X)/2)
testSet = -trainSet
cv.out= cv.glmnet(X[trainSet,],y[trainSet],alpha=1,lambda = grid, nfolds=30, thresh=1e-10)
lasso.bestlam = cv.out$lambda.min
sprintf("The optimal value of lambda : %s", lasso.bestlam)

#Calculate test errors for the lasso model
lasso.pred = predict(cv.out, s=lasso.bestlam, newx=X[testSet,])
lasso.error = mean((lasso.pred - y[testSet])^2)
sprintf("The test errors for the Lasso model: %s ", lasso.error )
lasso.out=glmnet(X,y,alpha=1, lambda = grid)
lasso.coef=predict (lasso.out ,type="coefficients",s= lasso.bestlam) [1:98,] 
lasso.size = nnzero(lasso.coef)
sprintf("Number of features selected by the lasso: %s ", lasso.size - 1 )
lasso.coef
