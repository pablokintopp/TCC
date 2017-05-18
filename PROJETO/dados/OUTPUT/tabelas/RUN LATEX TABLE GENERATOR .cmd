copy ..\..\..\Preprocessamento\bin\LatexTableGenerator.class LatexTableGenerator.class
set NAME=tabExp3.2
set CAPTION=Resultados_LOF_Bienio_2014-2015
set INPUT=%NAME%.csv
set OUTPUT=%NAME%.bib
java LatexTableGenerator true %NAME% %CAPTION% < %INPUT% > %OUTPUT%

pause