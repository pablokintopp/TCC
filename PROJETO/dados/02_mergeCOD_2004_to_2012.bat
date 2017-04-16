	REM @ECHO off
	setlocal EnableDelayedExpansion
	copy ..\Preprocessamento\bin\Preprocessamento.class Preprocessamento.class
	FOR /L %%Y IN (2004,1,2012) DO (
		set FILENAME=_%%Y.csv
		REM set TEMP1=temp_file_1.csv
		set OUTPUT=%%Y.csv
		java Preprocessamento --fibraold ; < !FILENAME! > !OUTPUT!	
	)
	pause	








