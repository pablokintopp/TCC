copy ..\..\..\Preprocessamento\bin\LatexTableGenerator.class LatexTableGenerator.class
set NAME=tabExp2.2
set INPUT=%NAME%.csv
set OUTPUT=%NAME%.bib
java LatexTableGenerator < %INPUT% > %OUTPUT%

pause