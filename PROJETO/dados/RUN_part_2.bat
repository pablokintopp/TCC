xcopy ..\Preprocessamento\bin\helper /I /Y helper
xcopy ..\Preprocessamento\bin\model /I /Y model
copy ..\Preprocessamento\bin\Main.class Main.class
copy ..\Preprocessamento\bin\Preprocessamento.class Preprocessamento.class


SET TYPE=ano



SET YEAR=2014
SET ORIGINAL_FILE=%YEAR%.csv
SET DIR=ANO_%YEAR%
set COL_RANGE=3-32
SET SCORE_COL=34
SET RANKING=10
SET K=20
set MIN=0
SET MAX=1000
SET INPUT=OUTPUT\%DIR%\_SCORED.csv
set OUTPUT_TEMP=OUTPUT\%DIR%\_SCORED_TEMP.csv
set OUTPUT_RANKED=OUTPUT\%DIR%\_SCORED_RANKED.csv

java Preprocessamento --centroid %K% 2 0 %COL_RANGE% %SCORE_COL% %MIN% %MAX%  <  %INPUT% > %OUTPUT_TEMP%

::::java Preprocessamento --ranking rankingSize colRange  < anomalies_suav_full.csv > anomalies_suav_ranked.csv
	java Preprocessamento --ranking %RANKING% %COL_RANGE% < %OUTPUT_TEMP% > %OUTPUT_RANKED%
	
del "%cd%\%OUTPUT_TEMP%"
	
pause	






