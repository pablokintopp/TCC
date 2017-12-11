xcopy ..\Preprocessamento\bin\helper /I /Y helper
xcopy ..\Preprocessamento\bin\model /I /Y model
copy ..\Preprocessamento\bin\Main.class Main.class
copy ..\Preprocessamento\bin\Preprocessamento.class Preprocessamento.class

SET YEAR_START=2019
SET YEAR_END=2020

SET YEAR=%YEAR_START%-%YEAR_END%
SET TYPE=multi
SET DIR=MULTI_%YEAR_START%_%YEAR_END%
SET ORIGINAL_FILE=XXXX.csv

SET FILE_REL=_REL_NOR.csv
SET FILE_SUA=_SUA_NOR.csv
SET FILE_ELKI=lof-outlier_order.txt
SET FILE_ELKI_SUA=ELKI_SUA_NOR.txt
SET FILE_ELKI_REL=ELKI_REL_NOR.txt
SET OUTPUT_ELKI=\OUTPUT_ELKI\%DIR%\
RMDIR "%cd%\OUTPUT\%DIR%" /S /Q

md "%cd%\OUTPUT\%DIR%"
if not exist "%cd%%OUTPUT_ELKI%" md "%cd%%OUTPUT_ELKI%"
del /q "%cd%%OUTPUT_ELKI%*"
::java Main %cd% [ano ou bienio] [XXXX ou XXXX-XXXX] [first ou last] > %OUTPUT%

java Main %cd% %DIR% %TYPE% %YEAR% first %ORIGINAL_FILE%
	
java -jar ../tools/elki.jar KDDCLIApplication -verbose -verbose -dbc.in "%cd%\\OUTPUT\\%DIR%\\%FILE_REL%" -parser.labelIndices 0 -time -algorithm outlier.lof.LOF -lof.k 70 -evaluator NoAutomaticEvaluation -resulthandler ResultWriter -out "%cd%\\OUTPUT_ELKI\\%DIR%\\" -out.silentoverwrite
	
rename "%cd%%OUTPUT_ELKI%%FILE_ELKI%" %FILE_ELKI_REL%

java -jar ../tools/elki.jar KDDCLIApplication -verbose -verbose -dbc.in "%cd%\\OUTPUT\\%DIR%\\%FILE_SUA%" -parser.labelIndices 0 -time -algorithm outlier.lof.LOF -lof.k 70 -evaluator NoAutomaticEvaluation -resulthandler ResultWriter -out "%cd%\\OUTPUT_ELKI\\%DIR%\\" -out.silentoverwrite
	
rename "%cd%%OUTPUT_ELKI%%FILE_ELKI%" %FILE_ELKI_SUA%

java Main %cd% %DIR% %TYPE% %YEAR% last %ORIGINAL_FILE%

	
pause	






