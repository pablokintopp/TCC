xcopy ..\Preprocessamento\bin\helper /I /Y helper
xcopy ..\Preprocessamento\bin\model /I /Y model
copy ..\Preprocessamento\bin\Main.class Main.class
copy ..\Preprocessamento\bin\Preprocessamento.class Preprocessamento.class

SET YEAR=2014-2015
SET TYPE=multi
SET DIR=MULTI_2014_2015
SET ORIGINAL_FILE=XXXX.csv

SET FILE_REL=_REL_NOR.csv
SET FILE_SUA=_SUA_NOR.csv
SET FILE_ELKI=lof-outlier_order.txt
SET FILE_ELKI_SUA=ELKI_SUA_NOR.txt
SET FILE_ELKI_REL=ELKI_REL_NOR.txt
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

	
pause	






