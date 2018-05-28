library(utils)
library(earth)
library(ggplot2)
library(plyr)
library(stats)

options(scipen=999)

data <- read.csv("C:\\studiaMagisterskie\\projekty\\gis\\git\\graphApp\\result.csv");
#shuffle
data <- data[sample(nrow(data)),]
data[["dijkstraTime"]] <- data[["dijkstraTime"]] / 10.0^6; #zamiana na milisekundy
data[["suurballeTime"]] <- data[["suurballeTime"]] / 10.0^6; #zamiana na milisekundy
#create pseudorandom suurballe


 
plotToFile <- function(p, path){
  #ggsave(filename=path, plot = p);
  #plot(p)
}

alpha1 = 0.1;
alpha2 = 0.2;
alpha3 = 0.4;


g <- ggplot(data, aes(nodesCount, dijkstraTime, color=generatorType))
p <- g + geom_jitter(width = .99, size=1, alpha=alpha1)  +
  labs(
      title="Obserwacje dla algorytmu heurystycznego"
    , x="Ilosc wezlów"
    , y="Czas wykonania algorytmu (ms)") +
  scale_colour_discrete(name="Rodzaj generatora")
#graf 1 - heurystyczny czas

plotToFile(p, "g1.png")

g <- ggplot(data, aes(nodesCount, suurballeTime, color=generatorType))
p <- g + geom_jitter(width = .99, size=1, alpha=alpha1)  +
  labs(
    title="Obserwacje dla algorytmu Suurballe"
    , x="Ilosc wezlów"
    , y="Czas wykonania algorytmu (ms)") +
  scale_colour_discrete(name="Rodzaj generatora")
#graf 2 - suurballe czas

plotToFile(p, "g2.png")



#10-ty decyl
quantiles.dijkstra <- ddply(data, ~nodesCount, summarise,
                   qu=quantile(dijkstraTime, probs = seq(0, 1, 1/10))[10])
#10-ty decyl
quantiles.suurballe <- ddply(data, ~nodesCount, summarise,
                            qu=quantile(suurballeTime, probs = seq(0, 1, 1/10))[10])

data$dijkstraTreshold <-  sapply(data$nodesCount, function(y)quantiles.dijkstra[quantiles.dijkstra$nodesCount ==y,]$qu)
data$dijkstraOutliers = data$dijkstraTime > data$dijkstraTreshold;

data$suurballeTreshold <-  sapply(data$nodesCount, function(y)quantiles.suurballe[quantiles.suurballe$nodesCount ==y,]$qu)
data$suurballeOutliers = data$suurballeTime > data$suurballeTreshold;

g <- ggplot(data, aes(nodesCount, dijkstraTime, color=dijkstraOutliers))
p <- g + geom_jitter(width = .99, size=1, alpha=alpha1)  +
  labs(
      title="Obserwacje odstajace dla algorytmu heurystycznego"
    , x="Ilosc wezlów"
    , y="Czas wykonania algorytmu (ms)") +
  scale_colour_discrete(name="Decyle", labels=c("1-9", "10"))
#graf 3 - Obstajace z ostatniego decyla

plotToFile(p, "g3.png")

g <- ggplot(data, aes(nodesCount, suurballeTime, color=suurballeOutliers))
p <- g + geom_jitter(width = .99, size=1, alpha=alpha1)  +
  labs(
    title="Obserwacje odstajace dla algorytmu suurballe"
    , x="Ilosc wezlów"
    , y="Czas wykonania algorytmu (ms)") +
  scale_colour_discrete(name="Decyle", labels=c("1-9", "10"))
#graf 3 - Obstajace z ostatniego decyla

plotToFile(p, "g3-0.png")

dijkstraOutliersCount <- nrow(data[data$dijkstraOutliers,])
suurballeOutliersCount <- nrow(data[data$suurballeOutliers,])

dc <- nrow(data[data$dijkstraOutliers == TRUE | data$suurballeOutliers == TRUE,])
data <- data[data$dijkstraOutliers == FALSE,]
data <- data[data$suurballeOutliers == FALSE,]

compData <- rbind(
    data.frame(nodesCount=data$nodesCount, time=data$dijkstraTime, algorithmType="Dijkstra"),
    data.frame(nodesCount=data$nodesCount, time=data$suurballeTime, algorithmType="Suurballe") )
compData <- compData[sample(nrow(compData)),]

g <- ggplot(compData, aes(nodesCount, time, color=algorithmType))
p <- g + geom_jitter(width = .99, size=1, alpha=alpha3)  +
  labs(
      title="Obserwacje bez wartosci odstajacych"
    , x="Ilosc wezlów"
    , y="Czas wykonania algorytmu (ms)") +
  scale_colour_discrete(name="Algortytm") +
  geom_smooth(alpha=alpha1, method="auto", se=TRUE)
#graf 3a - obserwacje bez wartosci odstajacych


plotToFile(p, "g3A.png")

g <- ggplot(data, aes(nodesCount, dijkstraTime,
                      color=factor(round(data$edgesCount/(data$nodesCount*(data$nodesCount-1)/2), digits=1))))
p <- g + geom_jitter(width = .99, size=1, alpha=alpha3)  +
  labs(
    title="Czas dzialania w zaleznosci od gestosci dla algorytmu heurystycznego"
    , x="Ilosc wezlów"
    , y="Czas wykonania algorytmu (ms)") +
  scale_colour_discrete(name="Gestosc grafu")
#graf 4 - Czas dzialania w zaleznosci od gestosci"

plotToFile(p, "g4-h.png")
# 
g <- ggplot(data, aes(nodesCount, suurballeTime,
                      color=factor(round(data$edgesCount/(data$nodesCount*(data$nodesCount-1)/2), digits=1))))
p <- g + geom_jitter(width = .99, size=1, alpha=alpha3)  +
  labs(
    title="Czas dzialania w zaleznosci od gestosci dla algorytmu suurballe"
    , x="Ilosc wezlów"
    , y="Czas wykonania algorytmu (ms)") +
  scale_colour_discrete(name="Gestosc grafu")
#graf 4 - Czas dzialania w zaleznosci od gestosci suurballe"

plotToFile(p, "g4-s.png")



# 
lm_heuristic_fit <- lm(data$dijkstraTime ~ data$nodesCount*log(data$nodesCount))
lm_heuristic_fit2 <- lm(data$dijkstraTime ~ data$edgesCount*log(data$nodesCount))
predicted_df <- data.frame(time = predict(lm_heuristic_fit, data), nc=data$nodesCount)
#predicted_df <- aggregate(predicted_df, list(predicted_df$nc), mean);

g <- ggplot(data, aes(nodesCount, dijkstraTime))
p <- g + geom_jitter(width = .99, size=1, alpha=alpha3, color="darkblue")  +
  labs(
      title="Obserwacje dla algorytmu heurystycznego"
    , x="Ilosc wezlów"
    , y="Czas wykonania algorytmu (ms)") +
  theme(legend.position="none") +
  geom_line(color='red',data = predicted_df, aes(x=nc, y=time))

#graf 5 - Nalozona regresja heurystyczny

plotToFile(p, "g5-h.png")
ggsave("g5-h.png", plot = p);
# 
lm_suurballe_fit <- lm(data$suurballeTime ~ data$nodesCount*log(data$nodesCount))
lm_suurballe_fit2 <- lm(data$suurballeTime ~ data$edgesCount + data$nodesCount*log(data$nodesCount))
predicted_df <- data.frame(time = predict(lm_suurballe_fit, data), nc=data$nodesCount)
#predicted_df <- aggregate(predicted_df, list(predicted_df$nc), mean);

g <- ggplot(data, aes(nodesCount, suurballeTime))
p <- g + geom_jitter(width = .99, size=1, alpha=alpha3, color="darkgreen")  +
  labs(
    title="Obserwacje dla algorytmu Suurballe"
    , x="Ilosc wezlów"
    , y="Czas wykonania algorytmu (ms)") +
  scale_colour_discrete(name="Rodzaj generatora") +
  geom_line(color='red',data = predicted_df, aes(x=nc, y=time))

#graf 6 - Nalozona regresja z suurballe

plotToFile(p, "g6-s.png")


g <- ggplot(data, aes(nodesCount, suurballeTime-dijkstraTime, color="red"))
p <- g + geom_jitter(width = .99, size=1, alpha=alpha3)  +
  labs(
    title="Róznica w czasie wykonania algorytmu (dodatni czas kiedy alg. heurystyczny wykonuje sie szybciej)"
    , x="Ilosc wezlów"
    , y="Dodatkowy czas (ms)") +
   theme(legend.position="none")

#graf 7 - Róznica w stosunku do ilosci wezlów

plotToFile(p, "g7.png")


data$timeDifference <- sign(round(data$suurballeTime - data$dijkstraTime, digits=1))
g <- ggplot(data, aes(nodesCount, suurballeTime-dijkstraTime, color=factor(timeDifference)))
p <- g + geom_jitter(width = .99, size=1, alpha=alpha3)  +
  labs(
    title="Porównanie algorytmów - czas dzialania algorytmów (dodatni gdy wynik suurballe jest powolniejszy)"
    , x="Ilosc wezlów"
    , y="Róznica w czasie wykonania (ms)") +
  scale_colour_discrete(name="Alg. Suurballe \n wykonywal sie", labels=c("Szybciej", "Tak samo", "wolniej"))


#graf 8 - Róznica w szybkosci do ilosci wezlów

plotToFile(p, "g8.png")

# 
data <- data[sign(round(data$suurballeFirstPathWeight - data$dijkstraFirstPathWeight, digits=1)) > -1, ]
data$firstPathDifference <- sign(round(data$suurballeFirstPathWeight - data$dijkstraFirstPathWeight, digits=1))
g <- ggplot(data, aes(nodesCount, suurballeFirstPathWeight-dijkstraFirstPathWeight, color=factor(firstPathDifference)))
p <- g + geom_jitter(width = .99, size=1, alpha=alpha2)  +
  labs(
    title="Porównanie algorytmów - dlugosc pierwszej sciezki (dodatni gdy wynik suurballe znalazl dluzsza sciezke)"
    , x="Ilosc wezlów"
    , y="Róznica w dlugosci pierwszej sciezki") +
  scale_colour_discrete(name="Alg. Suurballe \n znalazl sciezke", labels=c("taka sama", "krótsza"))


#graf 8 - Róznica w szybkosci do ilosci wezlów

plotToFile(p, "g8b.png")


data$secondPathDifference <- sign(round(data$suurballeSecondPathWeight - data$dijkstraSecondPathWeight, digits=1))
g <- ggplot(data, aes(nodesCount, suurballeSecondPathWeight-dijkstraSecondPathWeight, color=factor(secondPathDifference)))
p <- g + geom_jitter(width = .99, size=1, alpha=alpha2)  +
  labs(
    title="Porównanie algorytmów - dlugosc drugiej sciezki (dodatni gdy wynik suurballe znalazl dluzsza sciezke)"
    , x="Ilosc wezlów"
    , y="Róznica w dlugosci drugiej sciezki") +
  scale_colour_discrete(name="Alg. Suurballe \n znalazl sciezke", labels=c("krótsza", "taka sama", "dluzsza"))


#graf 8 - Róznica w szybkosci do ilosci wezlów

plotToFile(p, "g8c.png")




palette = c("#89C5DA", "#DA5724", "#74D944", "#CE50CA", "#3F4921", "#C0717C", "#CBD588", "#5F7FC7",
            "#673770", "#D3D93E", "#38333E", "#508578", "#D7C1B1", "#689030", "#AD6F3B", "#CD9BCD",
            "#D14285", "#6DDE88", "#652926", "#7FDCC0", "#C84248", "#8569D5", "#5E738F", "#D1A33D",
            "#8A7C64", "#599861");


g <- ggplot(data,
            aes(suurballeTime-dijkstraTime,
                suurballeFirstPathWeight + suurballeSecondPathWeight-
                  dijkstraFirstPathWeight-dijkstraSecondPathWeight))
p <- g + geom_point(width = .99, size=1, alpha=alpha3)  +
  labs(
    title="Porównanie algorytmów - suma dlugosci sciezek (dodatni gdy wynik suurballe jest wiekszy)"
    , x="Róznica w czasie wykonania (ms)"
    , y="Róznica w sumie dlugosci sciezek dlugosci") +
  theme(legend.position="none")

#graf 9 - Róznica w stosunku do ilosci wezlów

plotToFile(p, "g9.png")





g <- ggplot(data,
            aes(nodesCount,
                suurballeFirstPathWeight + suurballeSecondPathWeight-
                  dijkstraFirstPathWeight-dijkstraSecondPathWeight))
p <- g + geom_jitter(width = .99, size=1, alpha=alpha3)  +
  labs(
    title="Porównanie algorytmów - suma dlugosci sciezek (dodatni gdy wynik suurballe jest wiekszy)"
    , x="Ilosc wezlów (ms)"
    , y="Róznica w sumie dlugosci sciezek") +
  theme(legend.position="none")

#graf 9 - Róznica w stosunku do ilosci wezlów

plotToFile(p, "g10.png")


g <- ggplot(data,
            aes(suurballeTime-dijkstraTime,
                suurballeFirstPathWeight + suurballeSecondPathWeight-
                  dijkstraFirstPathWeight-dijkstraSecondPathWeight))
p <- g + geom_point(width = .99, size=1, alpha=alpha1, color="darkred")  +
  labs(
    title="Porównanie algorytmów - czas wykonania do dlugosci sciezek (dodatni gdy wynik suurballe jest wiekszy)"
    , x="Róznica w dlugosci wykonania (ms)"
    , y="Róznica w sumie dlugosci sciezek ") +
  theme(legend.position="none")

#graf 9 - Róznica w stosunku do ilosci wezlów

plotToFile(p, "g11.png")





data$density <- data$edgesCount/(data$nodesCount*(data$nodesCount-1)/2)

g <- ggplot(data,
            aes(data$density,
                suurballeFirstPathWeight + suurballeSecondPathWeight-
                  dijkstraFirstPathWeight-dijkstraSecondPathWeight))
p <- g + geom_point(color="darkblue", alpha=alpha2) +
  labs(
    title="Porównanie algorytmów - gestosc grafu do dlugosci sciezek (dodatni gdy wynik suurballe jest wiekszy)"
    , x="Gestosc grafu"
    , y="Róznica w sumie dlugosci sciezek ") +
  theme(legend.position="none")

#graf 9 - Róznica w stosunku do ilosci wezlów

plotToFile(p, "g12.png")

