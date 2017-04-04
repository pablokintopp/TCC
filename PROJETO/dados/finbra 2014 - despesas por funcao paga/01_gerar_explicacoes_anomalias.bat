set OUTPUT=anomalies_1.3_70.csv
set INPUT=all_scores.csv
set LOF=36
set MIN=1.3
SET K=70

copy ..\..\Preprocessamento\bin\Preprocessamento.class Preprocessamento.class

:::: Preprocessamento --centroid vizinhos colPopulacao colId colStart-colEnd colLOF minLOF maxGoodLOF  < all_scores.csv> scores_percents.csv
java Preprocessamento --centroid %K% 3 1 4-32 %LOF% 0 %MIN%   < %INPUT% > temp_anomalies.csv

java Preprocessamento --remove ; 33 34 35 37 38 < temp_anomalies.csv > temp_anomalies_2.csv




::::java Preprocessamento --ranking rankingSize colRange  < anomalies_suav_full.csv > anomalies_suav_ranked.csv
	java Preprocessamento --ranking 5 4-32 < temp_anomalies_2.csv > %OUTPUT%


del temp_*  /s