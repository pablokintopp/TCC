copy ..\..\Preprocessamento\bin\Preprocessamento.class Preprocessamento.class
SET NAME=2014_LOF_ALL
set INPUT=%NAME%.csv
SET OUTPUT=%NAME%_EXPLAINED.csv
SET OUTPUT_RANKED=%NAME%_RANKED.csv
SET RANKING=29
SET K=20
set MIN=0
SET MAX=1000
:::: Preprocessamento --centroid vizinhos colPopulacao colId colStart-colEnd colLOF minLOF maxGoodLOF  < all_scores.csv> scores_percents.csv
::java Preprocessamento --centroid %K% 2 0 3-31 36 %MIN% %MAX%  < %INPUT% > %OUTPUT%

::::java Preprocessamento --ranking rankingSize colRange  < anomalies_suav_full.csv > anomalies_suav_ranked.csv
	java Preprocessamento --ranking %RANKING% 2-30 < %OUTPUT% > %OUTPUT_RANKED%
	

pause
