copy ..\..\Preprocessamento\bin\Preprocessamento.class Preprocessamento.class
java Preprocessamento --finbra true < finbra_original.csv > finbra_preprocessado_absoluto.csv
java Preprocessamento --remove ; 0 2 < finbra_preprocessado_absoluto.csv > finbra_preprocessado_absoluto_com_pop.csv
java Preprocessamento --remove ; 0 2 3 < finbra_preprocessado_absoluto.csv > finbra_preprocessado_absoluto_sem_pop.csv
java Preprocessamento --normalize log ignore[0] < finbra_preprocessado_absoluto_com_pop.csv > finbra_preprocessado_absoluto_com_pop_suavizado.csv
java Preprocessamento --relative 1 ignore[0] < finbra_preprocessado_absoluto_com_pop.csv > finbra_preprocessado_relativo.csv
java Preprocessamento --generateMean ignore[0] < finbra_preprocessado_relativo.csv > finbra_preprocessado_relativo_media.csv
java Preprocessamento --normalize devMean ignore[0] < finbra_preprocessado_relativo_media.csv > finbra_preprocessado_relativo_normalizado.csv
del finbra_preprocessado_relativo_media.csv
java Preprocessamento --generateMean ignore[0] < finbra_preprocessado_absoluto_com_pop_suavizado.csv > finbra_preprocessado_absoluto_com_pop_suavizado_media.csv
java Preprocessamento --normalize devMean ignore[0] < finbra_preprocessado_absoluto_com_pop_suavizado_media.csv > finbra_preprocessado_absoluto_com_pop_suavizado_normalizado.csv
del finbra_preprocessado_absoluto_com_pop_suavizado_media.csv

java -jar ../../tools/elki.jar KDDCLIApplication -dbc.in "finbra_preprocessado_absoluto_com_pop.csv" -parser.labelIndices 0 -time -algorithm outlier.lof.LOF -lof.k 70 -resulthandler ResultWriter -out "outputs_elki/finbra2014_absoluto_com_populacao" -out.silentoverwrite
java -jar ../../tools/elki.jar KDDCLIApplication -dbc.in "finbra_preprocessado_absoluto_sem_pop.csv" -parser.labelIndices 0 -time -algorithm outlier.lof.LOF -lof.k 70 -resulthandler ResultWriter -out "outputs_elki/finbra2014_absoluto_sem_populacao" -out.silentoverwrite
java -jar ../../tools/elki.jar KDDCLIApplication -dbc.in "finbra_preprocessado_absoluto_com_pop_suavizado.csv" -parser.labelIndices 0 -time -algorithm outlier.lof.LOF -lof.k 70 -resulthandler ResultWriter -out "outputs_elki/finbra2014_absoluto_com_populacao_suavizado" -out.silentoverwrite
java -jar ../../tools/elki.jar KDDCLIApplication -dbc.in "finbra_preprocessado_relativo.csv" -parser.labelIndices 0 -time -algorithm outlier.lof.LOF -lof.k 70 -resulthandler ResultWriter -out "outputs_elki/finbra2014_relativo" -out.silentoverwrite
java -jar ../../tools/elki.jar KDDCLIApplication -dbc.in "finbra_preprocessado_relativo_normalizado.csv" -parser.labelIndices 0 -time -algorithm outlier.lof.LOF -lof.k 70 -resulthandler ResultWriter -out "outputs_elki/finbra2014_relativo_normalizado" -out.silentoverwrite
java -jar ../../tools/elki.jar KDDCLIApplication -dbc.in "finbra_preprocessado_absoluto_com_pop_suavizado_normalizado.csv" -parser.labelIndices 0 -time -algorithm outlier.lof.LOF -lof.k 70 -resulthandler ResultWriter -out "outputs_elki/finbra2014_absoluto_com_populacao_suavizado_normalizado" -out.silentoverwrite

mkdir temp

java Preprocessamento --remove -lof < outputs_elki\finbra2014_absoluto_com_populacao\lof-outlier_order.txt > outputs_elki\finbra2014_absoluto_com_populacao\lof.csv
copy /a outputs_elki\finbra2014_absoluto_com_populacao\lof.csv + finbra_preprocessado_absoluto.csv temp\fibra_abs_pop_lof.csv
java Preprocessamento --match lof_absoluto_com_pop < temp\fibra_abs_pop_lof.csv > all_scores.csv

java Preprocessamento --remove -lof < outputs_elki\finbra2014_absoluto_sem_populacao\lof-outlier_order.txt > outputs_elki\finbra2014_absoluto_sem_populacao\lof.csv
copy /a outputs_elki\finbra2014_absoluto_sem_populacao\lof.csv + all_scores.csv temp\fibra_abs_sem_pop_lof.csv
java Preprocessamento --match lof_absoluto_sem_pop < temp\fibra_abs_sem_pop_lof.csv > all_scores.csv

java Preprocessamento --remove -lof < outputs_elki\finbra2014_absoluto_com_populacao_suavizado\lof-outlier_order.txt > outputs_elki\finbra2014_absoluto_com_populacao_suavizado\lof.csv
copy /a outputs_elki\finbra2014_absoluto_com_populacao_suavizado\lof.csv + all_scores.csv temp\fibra_abs_pop_suav_lof.csv
java Preprocessamento --match lof_absoluto_suav < temp\fibra_abs_pop_suav_lof.csv > all_scores.csv

java Preprocessamento --remove -lof < outputs_elki\finbra2014_absoluto_com_populacao_suavizado_normalizado\lof-outlier_order.txt > outputs_elki\finbra2014_absoluto_com_populacao_suavizado_normalizado\lof.csv
copy /a outputs_elki\finbra2014_absoluto_com_populacao_suavizado_normalizado\lof.csv + all_scores.csv temp\fibra_abs_pop_suav_norm_lof.csv
java Preprocessamento --match lof_absoluto_suav_norm < temp\fibra_abs_pop_suav_norm_lof.csv > all_scores.csv

java Preprocessamento --remove -lof < outputs_elki\finbra2014_relativo\lof-outlier_order.txt > outputs_elki\finbra2014_relativo\lof.csv
copy /a outputs_elki\finbra2014_relativo\lof.csv + all_scores.csv temp\fibra_rel_lof.csv
java Preprocessamento --match lof_relativo < temp\fibra_rel_lof.csv > all_scores.csv

java Preprocessamento --remove -lof < outputs_elki\finbra2014_relativo_normalizado\lof-outlier_order.txt > outputs_elki\finbra2014_relativo_normalizado\lof.csv
copy /a outputs_elki\finbra2014_relativo_normalizado\lof.csv + all_scores.csv temp\fibra_rel_med_lof.csv
java Preprocessamento --match lof_relativo_norm < temp\fibra_rel_med_lof.csv > all_scores.csv

::del temp\*.* /s /q

rmdir  temp  /s /q
rmdir  outputs_elki  /s /q
del finbra_pre*  /s




:::: Preprocessamento --centroid vizinhos colPopulacao colId colStart-colEnd colLOF minLOF maxGoodLOF  < all_scores.csv> scores_percents.csv
::java Preprocessamento --centroid 20 3 1 4-33 37 1.5 1.19   < all_scores.csv > anomalies_suav.csv
::java Preprocessamento --centroid 20 3 1 4-33 39 1.5 1.19  < all_scores.csv > anomalies_rel.csv





