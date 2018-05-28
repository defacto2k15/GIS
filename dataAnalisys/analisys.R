library(utils)
library(earth)
library(ggplot2)
library(plyr)

options(scipen=999)

data <- read.csv("C:\\studiaMagisterskie\\projekty\\gis\\git\\graphApp\\result.csv");
#shuffle
data <- data[sample(nrow(data)),]
data[["dijkstraTime"]] <- data[["dijkstraTime"]] / 10.0^6; #zamiana na milisekundy
#create pseudorandom suurballe

data$suurballeFirstPathWeight <- data$dijkstraFirstPathWeight *  rnorm(data$dijkstraFirstPathWeight, mean=0.7, sd=0.2) 
data$suurballeSecondPathWeight <- data$dijkstraSecondPathWeight *  rnorm(data$dijkstraSecondPathWeight, mean=0.7, sd=0.2) 
data$suurballeTime <- data$dijkstraTime *  rnorm(data$suurballeTime, mean=1.4, sd=0.2) 

plotToFile <- function(p, path){
  #png(filename);
  ggsave(filename=path, plot = p);
  #dev.off();
}



#data <- data[ data$generatorType == "Watts", ]
#data <- data[ data$dijkstraFirstPathWeight == 0, ]
#data <- data[ data$dijkstraSecondPathWeight != 0, ]

roundUp <- function(x) round(x/10)*10


# p <- qplot(data$nodesCount,
#            data$dijkstraTime,
#            geom=c("boxplot"),
#            #aes()
#            color=factor(roundUp(data$nodesCount)),
#            trim=FALSE,
#            )
# 
# g <- ggplot(data, aes(nodesCount, dijkstraTime, color=generatorType))
# p <- g + geom_jitter(width = .99, size=1)  +
#   labs( 
#       title="Obserwacje dla algorytmu heurystycznego"
#     , x="Ilosc wezlów"
#     , y="Czas wykonania algorytmu (ms)") +
#   scale_colour_discrete(name="Rodzaj generatora")
# #graf 1 - heurystyczny czas
# plot(p)  
# plotToFile(p, "g1.png")
# 
# g <- ggplot(data, aes(nodesCount, suurballeTime, color=generatorType))
# p <- g + geom_jitter(width = .99, size=1)  +
#   labs( 
#     title="Obserwacje dla algorytmu Suurballe"
#     , x="Ilosc wezlów"
#     , y="Czas wykonania algorytmu (ms)") +
#   scale_colour_discrete(name="Rodzaj generatora")
# #graf 2 - suurballe czas
# plot(p)
# plotToFile(p, "g2.png")



#10-ty decyl
quantiles <- ddply(data, ~nodesCount, summarise, 
                   qu=quantile(dijkstraTime, probs = seq(0, 1, 1/10))[10],
                   sd=sd(dijkstraTime))
data$treshold <-  sapply(data$nodesCount, function(y)quantiles[quantiles$nodesCount ==y,]$qu)
data$outliers = data$dijkstraTime > data$treshold;

# g <- ggplot(data, aes(nodesCount, dijkstraTime, color=outliers))
# p <- g + geom_jitter(width = .99, size=1)  +
#   labs(
#       title="Obserwacje odstajace dla algorytmu heurystycznego"
#     , x="Ilosc wezlów"
#     , y="Czas wykonania algorytmu (ms)") +
#   scale_colour_discrete(name="Decyle", labels=c("1-9", "10"))
# #graf 3 - Obstajace z ostatniego decyla
# plot(p)
# plotToFile(p, "g3.png")

outliersCount <- nrow(data[data$outliers,])
data <- data[data$outliers == FALSE,]

# g <- ggplot(data, aes(nodesCount, dijkstraTime, color=generatorType))
# p <- g + geom_jitter(width = .99, size=1)  +
#   labs(
#       title="Obserwacje dla algorytmu heurystycznego"
#     , x="Ilosc wezlów"
#     , y="Czas wykonania algorytmu (ms)") +
#   scale_colour_discrete(name="Wykres bez wartosci odstajacych")
# #graf 1 - heurystyczny czas
# plot(p)
# plotToFile(p, "g3A.png")

# g <- ggplot(data, aes(nodesCount, dijkstraTime,
#                       color=factor(round(data$edgesCount/(data$nodesCount*(data$nodesCount-1)/2), digits=1))))
# p <- g + geom_jitter(width = .99, size=1)  +
#   labs(
#     title="Obserwacje dla algorytmu heurystycznego"
#     , x="Ilosc wezlów"
#     , y="Czas wykonania algorytmu (ms)") +
#   scale_colour_discrete(name="Gestosc grafu")
# #graf 4 - Czas dzialania w zaleznosci od gestosci"
# plot(p)
# plotToFile(p, "g4.png")

# lm_heuristic_fit <- lm(data$dijkstraTime ~ data$nodesCount*log(data$nodesCount))
# predicted_df <- data.frame(time = predict(lm_heuristic_fit, data), nc=data$nodesCount)
# 
# 
# g <- ggplot(data, aes(nodesCount, dijkstraTime, color=generatorType))
# p <- g + geom_jitter(width = .99, size=1)  +
#   labs(
#       title="Obserwacje dla algorytmu heurystycznego"
#     , x="Ilosc wezlów"
#     , y="Czas wykonania algorytmu (ms)") +
#   scale_colour_discrete(name="Rodzaj generatora") +
#   geom_line(color='red',data = predicted_df, aes(x=nc, y=time))
# 
# #graf 5 - Nalozona regresja
# plot(p)
# plotToFile(p, "g5.png")


# lm_suurballe_fit <- lm(data$suurballeTime ~ data$nodesCount*log(data$nodesCount))
# predicted_df <- data.frame(time = predict(lm_suurballe_fit, data), nc=data$nodesCount)
# 
# 
# g <- ggplot(data, aes(nodesCount, suurballeTime, color=generatorType))
# p <- g + geom_jitter(width = .99, size=1)  +
#   labs(
#     title="Obserwacje dla algorytmu Suurballe"
#     , x="Ilosc wezlów"
#     , y="Czas wykonania algorytmu (ms)") +
#   scale_colour_discrete(name="Rodzaj generatora") +
#   geom_line(color='red',data = predicted_df, aes(x=nc, y=time))
# 
# #graf 6 - Nalozona regresja z suurballe
# plot(p)
# plotToFile(p, "g6.png")

# g <- ggplot(data, aes(nodesCount, suurballeTime-dijkstraTime, color="red"))
# p <- g + geom_jitter(width = .99, size=1)  +
#   labs(
#     title="Róznica w czasie wykonania algorytmu (dodatni czas kiedy alg. heurystyczny wykonuje sie szybciej)"
#     , x="Ilosc wezlów"
#     , y="Dodatkowy czas (ms)") +
#    theme(legend.position="none")
# 
# #graf 7 - Róznica w stosunku do ilosci wezlów
# plot(p)
# plotToFile(p, "g7.png")


g <- ggplot(data, aes(suurballeFirstPathWeight-dijkstraFirstPathWeight, suurballeTime-dijkstraTime, color="red"))
p <- g + geom_jitter(width = .99, size=1)  +
  labs(
    title="Porównanie algorytmów - dlugosc pierwszej sciezki (dodatni gdy wynik suurballe jest wiekszy)"
    , x="Róznica w dlugosci pierwszej sciezki"
    , y="Róznica w czasie wykonania (ms)") +
  theme(legend.position="none")

#graf 8 - Róznica w stosunku do ilosci wezlów
plot(p)
plotToFile(p, "g8.png")

palette = c("#89C5DA", "#DA5724", "#74D944", "#CE50CA", "#3F4921", "#C0717C", "#CBD588", "#5F7FC7", 
            "#673770", "#D3D93E", "#38333E", "#508578", "#D7C1B1", "#689030", "#AD6F3B", "#CD9BCD", 
            "#D14285", "#6DDE88", "#652926", "#7FDCC0", "#C84248", "#8569D5", "#5E738F", "#D1A33D", 
            "#8A7C64", "#599861");


g <- ggplot(data, aes(suurballeSecondPathWeight-dijkstraSecondPathWeight, suurballeTime-dijkstraTime,
                      color=rep(palette, length.out=nrow(data))))
p <- g + geom_jitter(width = .99, size=1)  +
  labs(
    title="Porównanie algorytmów - dlugosc drugiej sciezki (dodatni gdy wynik suurballe jest wiekszy)"
    , x="Róznica w dlugosci drugiej sciezki"
    , y="Róznica w czasie wykonania (ms)") +
  theme(legend.position="none")

#graf 9 - Róznica w stosunku do ilosci wezlów
plot(p)
plotToFile(p, "g9.png")









data$groupedNodesCount <- as.factor(roundUp(data$nodesCount))

# p <- ggplot(data, 
#             aes(x=groupedNodesCount, y=dijkstraTime),
#             xlab="Ilosc wezlów",
#             ylab="Czas wykonania(ms)"
#             ) + geom_boxplot();




nc <- data[["nodesCount"]];
ne <- data[["edgesCount"]];
#fit <- lm(data[["dijkstraTime"]] ~ nc*log(nc) );


#y<-predict(fit);

#x<-seq(from=10,to=1200,length.out=length(y));
#matlines(nc,y,lwd=1)