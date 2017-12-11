copy ..\..\..\Preprocessamento\bin\LatexTableGenerator.class LatexTableGenerator.class
set NAME=tabExp4.2
set CAPTION=Resultados_do_experimento_com_diferentes_algoritmos_usando_abordagem_suavizada
set INPUT=%NAME%.csv
set OUTPUT=%NAME%.bib
java LatexTableGenerator false %NAME% %CAPTION% < %INPUT% > %OUTPUT%

pause