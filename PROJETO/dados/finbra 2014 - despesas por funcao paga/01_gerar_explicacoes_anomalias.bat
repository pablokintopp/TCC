copy ..\..\Preprocessamento\bin\Preprocessamento.class Preprocessamento.class

:::: Preprocessamento --centroid vizinhos colPopulacao colId colStart-colEnd colLOF minLOF maxGoodLOF  < all_scores.csv> scores_percents.csv
java Preprocessamento --centroid 20 3 1 4-33 37 1.5 1.19   < all_scores.csv > temp_anomalies_suav.csv
java Preprocessamento --centroid 20 3 1 4-33 39 1.5 1.19  < all_scores.csv > temp_anomalies_rel.csv


java Preprocessamento --remove ; 34 35 36 38 39 < temp_anomalies_suav.csv > anomalies_suav_full.csv
java Preprocessamento --remove ; 34 35 36 37 38 < temp_anomalies_rel.csv > anomalies_rel_full.csv

del temp_*  /s

::::java Preprocessamento --ranking rankingSize colRange  < anomalies_suav_full.csv > anomalies_suav_ranked.csv
	java Preprocessamento --ranking 5 4-33 < anomalies_suav_full.csv > anomalies_suav_ranked.csv
	java Preprocessamento --ranking 5 4-33 < anomalies_rel_full.csv > anomalies_rel_ranked.csv


