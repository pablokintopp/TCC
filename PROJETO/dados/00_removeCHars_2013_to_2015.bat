	rem @ECHO off
	setlocal EnableDelayedExpansion
	copy ..\Preprocessamento\bin\Preprocessamento.class Preprocessamento.class
	FOR /L %%Y IN (2013,1,2015) DO (
		REM set FILENAME=_%%Y.csv		
		REM set OUTPUT=%%Y.csv
		java Preprocessamento --codigbe < _%%Y.csv> %%Y.csv
	)
	pause	







