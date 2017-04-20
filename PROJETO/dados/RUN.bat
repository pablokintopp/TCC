
xcopy ..\Preprocessamento\bin\model /I /Y model
copy ..\Preprocessamento\bin\Main.class Main.class

:: java Main YEAR_START YEAR_END > %OUTPUT%
SET OUTPUT=2013_2014_2015_min.csv
java Main 2013 2015 > %OUTPUT%

SET OUTPUT=2013_2014_min.csv
java Main 2013 2014 > %OUTPUT%


SET OUTPUT=2014_2015_min.csv
java Main 2014 2015 > %OUTPUT%
		
pause		
	
	
	







