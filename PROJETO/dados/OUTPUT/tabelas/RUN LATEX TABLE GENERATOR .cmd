set NAME=tabExp1.2
set INPUT=%NAME%.csv
set OUTPUT=%NAME%.bib
java LatexTableGenerator < %INPUT% > %OUTPUT%

pause