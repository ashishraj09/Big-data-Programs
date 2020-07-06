#Question 1
Credit = read.csv("Credit.csv", header = TRUE)[,-1]
#install.packages("gam")
library(gam)
set.seed(1234)
trainSet = sample(1:nrow(Credit), nrow(Credit)/2)
testSet = -trainSet
gam.mod_1 = gam(Balance~ns(Income,df=4)+ns(Age,df=1)+Student, data=Credit[trainSet,])
gam.mod_2 = gam(Balance~ns(Income,df=4)+ns(Age,df=2)+Student, data=Credit[trainSet,]) 
gam.mod_3 = gam(Balance~ns(Income,df=4)+ns(Age,df=3)+Student, data=Credit[trainSet,])
gam.mod_4 = gam(Balance~ns(Income,df=4)+ns(Age,df=4)+Student, data=Credit[trainSet,])
gam.mod_5 = gam(Balance~ns(Income,df=4)+ns(Age,df=5)+Student, data=Credit[trainSet,])
gam.mod_6 = gam(Balance~ns(Income,df=4)+ns(Age,df=6)+Student, data=Credit[trainSet,])
pred.mod_1 = predict(gam.mod_1, new = Credit[testSet,])
pred.mod_2 = predict(gam.mod_2, new = Credit[testSet,])
pred.mod_3 = predict(gam.mod_3, new = Credit[testSet,])
pred.mod_4 = predict(gam.mod_4, new = Credit[testSet,])
pred.mod_5 = predict(gam.mod_5, new = Credit[testSet,])
pred.mod_6 = predict(gam.mod_6, new = Credit[testSet,])
mse_1 = mean((pred.mod_1 - Credit$Balance[testSet])^2)
mse_2 = mean((pred.mod_2 - Credit$Balance[testSet])^2)
mse_3 = mean((pred.mod_3 - Credit$Balance[testSet])^2)
mse_4 = mean((pred.mod_4 - Credit$Balance[testSet])^2)
mse_5 = mean((pred.mod_5 - Credit$Balance[testSet])^2)
mse_6 = mean((pred.mod_6 - Credit$Balance[testSet])^2)
c(mse_1, mse_2, mse_3, mse_4, mse_5, mse_6)

plot(c(mse_1, mse_2, mse_3, mse_4, mse_5, mse_6), type = "o", col = "red", xlab = "Degree of Freedom", 
 ylab = "Mean Square Error")

#Question 2
par(mfrow=c(1,2))
plot(gam.mod_1,col='red', lwd=2)

#Question 3
sprintf("The square root of the mean square error (RMSE) : %s", sqrt(mse_1))