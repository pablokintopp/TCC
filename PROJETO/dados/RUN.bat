SET OUTPUT=2013_to_2015_min.csv
xcopy ..\Preprocessamento\bin\model /I /Y model
copy ..\Preprocessamento\bin\Main.class Main.class
	
java Main  > %OUTPUT%
		
pause		
	
	
	







