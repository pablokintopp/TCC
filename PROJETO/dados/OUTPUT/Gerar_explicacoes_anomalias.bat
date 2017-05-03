copy ..\..\Preprocessamento\bin\Preprocessamento.class Preprocessamento.class
SET NAME=2013_LOF
set INPUT=%NAME%.csv
SET OUTPUT=%NAME%_EXPLAINED.csv
SET OUTPUT_RANKED=%NAME%_RANKED.csv
SET RANKING=29
SET K=20
set MIN=0
SET MAX=1000
:::: Preprocessamento --centroid vizinhos colPopulacao colId colStart-colEnd colLOF minLOF maxGoodLOF  < all_scores.csv> scores_percents.csv
java Preprocessamento --centroid %K% 2 0 4-32 34 %MIN% %MAX%  < %INPUT% > %OUTPUT%

::::java Preprocessamento --ranking rankingSize colRange  < anomalies_suav_full.csv > anomalies_suav_ranked.csv
	java Preprocessamento --ranking %RANKING% 4-32 < %OUTPUT% > %OUTPUT_RANKED%
	

pause
