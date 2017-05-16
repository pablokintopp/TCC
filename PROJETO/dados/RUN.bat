xcopy ..\Preprocessamento\bin\helper /I /Y helper
xcopy ..\Preprocessamento\bin\model /I /Y model
copy ..\Preprocessamento\bin\Main.class Main.class
copy ..\Preprocessamento\bin\Preprocessamento.class Preprocessamento.class

SET YEAR=2014
SET TYPE=ano
SET DIR=e
SET ORIGINAL_FILE=%DIR%.csv

SET FILE_REL=%YEAR%_REL_NOR.csv
SET FILE_SUA=%YEAR%_SUA_NOR.csv
SET FILE_ELKI=lof-outlier_order.txt
SET FILE_ELKI_SUA=ELKI_%YEAR%_SUA_NOR.txt
SET FILE_ELKI_REL=ELKI_%YEAR%_REL_NOR.txt
SET OUTPUT_ELKI=D:\REPOSITORY\TCC\PROJETO\dados\OUTPUT_ELKI\%DIR%\
RMDIR "D:\REPOSITORY\TCC\PROJETO\dados\OUTPUT\%DIR%" /S /Q

md "D:\REPOSITORY\TCC\PROJETO\dados\OUTPUT\%DIR%"
if not exist "%OUTPUT_ELKI%" md "%OUTPUT_ELKI%"
del /q "%OUTPUT_ELKI%*"
::java Main [ano ou bienio] [XXXX ou XXXX-XXXX] [first ou last] > %OUTPUT%

java Main %DIR% %TYPE% %YEAR% first %ORIGINAL_FILE%

java -jar ../tools/elki.jar KDDCLIApplication -verbose -verbose -dbc.in "D:\\REPOSITORY\\TCC\\PROJETO\\dados\\OUTPUT\\%DIR%\\%FILE_REL%" -parser.labelIndices 0 -time -algorithm outlier.lof.LOF -lof.k 70 -evaluator NoAutomaticEvaluation -resulthandler ResultWriter -out "D:\\REPOSITORY\\TCC\\PROJETO\\dados\\OUTPUT_ELKI\\%DIR%\\" -out.silentoverwrite
	
rename "%OUTPUT_ELKI%%FILE_ELKI%" %FILE_ELKI_REL%

java -jar ../tools/elki.jar KDDCLIApplication -verbose -verbose -dbc.in "D:\\REPOSITORY\\TCC\\PROJETO\\dados\\OUTPUT\\%DIR%\\%FILE_SUA%" -parser.labelIndices 0 -time -algorithm outlier.lof.LOF -lof.k 70 -evaluator NoAutomaticEvaluation -resulthandler ResultWriter -out "D:\\REPOSITORY\\TCC\\PROJETO\\dados\\OUTPUT_ELKI\\%DIR%\\" -out.silentoverwrite
	
rename "%OUTPUT_ELKI%%FILE_ELKI%" %FILE_ELKI_SUA%

java Main %DIR% %TYPE% %YEAR% last %ORIGINAL_FILE%

SET RANKING=29
SET K=20
set MIN=0
SET MAX=1000
SET INPUT=OUTPUT\%DIR%\%YEAR%_SCORED.csv
set OUTPUT_TEMP=OUTPUT\%DIR%\%YEAR%_SCORED_TEMP.csv
set OUTPUT_RANKED=OUTPUT\%DIR%\%YEAR%_SCORED_RANKED.csv

java Preprocessamento --centroid %K% 2 0 4-32 34 %MIN% %MAX%  < %INPUT% > %OUTPUT_TEMP%

::::java Preprocessamento --ranking rankingSize colRange  < anomalies_suav_full.csv > anomalies_suav_ranked.csv
	java Preprocessamento --ranking %RANKING% 4-32 < %OUTPUT_TEMP% > %OUTPUT_RANKED%
	
del "D:\REPOSITORY\TCC\PROJETO\dados\%OUTPUT_TEMP%"
	
pause		
	







