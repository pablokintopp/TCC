	setlocal EnableDelayedExpansion
	copy ..\Preprocessamento\bin\Preprocessamento.class Preprocessamento.class
	FOR /L %%Y IN (2013,1,2015) DO (
		copy ..\Preprocessamento\bin\Preprocessamento.class Preprocessamento.class
		java Preprocessamento --finbra false < %%Y_original.csv > temp_file_1.csv
		java Preprocessamento --remove ; 32 < temp_file_1.csv > _%%Y.csv
		del temp*  /s
	)
	pause
	
	







