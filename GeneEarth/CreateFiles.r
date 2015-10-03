CreateFiles <-
function(dr=getwd(), fileName = "Ovarian GE MAD Filtered.txt", target= getwd())
   #function(dr="C:\\Users\\user\\Documents\\Dartmouth\\14-15\\2015 Summer\\GeneRankNewCode(150729)",
    #        fileName = "Ovarian GE MAD Filtered.txt",
     #       target= "C:\\Users\\user\\Documents\\Dartmouth\\14-15\\2015 Summer\\GeneRankNewCode(150729)")
{
dump("CreateFiles",paste(dr,"\\CreateFiles.r",sep=""))

   dist = 1
   alpha.grade.view = 180
   beta.grade.view = 180
   
td = function(alpha.view, beta.view, x, y, z) # Projection of a 3D point (x,y,z) on the plane at the camera angle (alpha,beta)
{
    px = x * cos(alpha.view) * sin(beta.view) + y * sin(alpha.view) * sin(beta.view) - z * cos(beta.view)
    py = x * sin(alpha.view) - y * cos(alpha.view)
    xyc = list(-px, py)
    names(xyc) = c("x", "y")
    return(xyc)
}
n=100
r2.thresh=1-exp(-dist/10)


X = read.table(paste(dr,"\\",fileName,sep=""))

r = cor(X)
nX=nrow(r)
GR=colMeans(r^2);GR=GR/sqrt(sum(GR^2))


GR.tr=(GR[order(GR)])[0.9*nX]
R = as.matrix(r,ncol=nX,nrow=nX)

namgene=as.character(names(X))
X=as.matrix(X)
Xt=t(X);m=ncol(Xt)
for(i in 1:m) Xt[,i]=(X[i,]-mean(X[i,]))/sd(X[i,])
x3=svd(Xt)$u[,1:3]
for(i in 1:1500) x3[i,]=x3[i,]/sqrt(sum(x3[i,]^2))
alpha=atan(x3[,2]/x3[,1]);beta=asin(x3[,3])

x = cos(alpha) * cos(beta)
y = sin(alpha) * cos(beta)
z = sin(beta)

#Used to save the file of GR
   write.csv(GR,file=paste(target,"\\GR.txt",sep=""),row.names=F)
   write.csv(R,file=paste(target,"\\R.txt",sep=""),row.names=F)
   write.csv(cbind(alpha,beta),file=paste(target,"\\tf.txt",sep=""),row.names=F)
}
