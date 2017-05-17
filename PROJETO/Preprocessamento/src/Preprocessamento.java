import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.print.DocFlavor;

import java.util.TreeMap;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/*
 * Pablo Mezzon Kintopp 
*/
public class Preprocessamento {
	
	//FINBRA Despesas Por Função - Pagas
	public static String FINBRA_DPF_PAGAS = "--finbra";
	
	//FINBRA Despesas Por Função - Pagas 2004 - 2012
		public static String FINBRA_DPF_PAGAS_OLD = "--fibraold";
		
	//para remover atributos nao necessarios
	public static String REMOVE_ATRIBUTES = "--remove";
	
	//para remover atributos nao necessarios
		public static String FIX_COD_IBGE = "--codigbe";
	
	//Para suavizar/normalizar os dados
	public static String NORMALIZE = "--normalize";
	
	//Para criar os dados com valores relativos
	public static String RELATIVE ="--relative";
	
	//Para concatenar colunas dos scores LOF de acordo com o ID da cidade
	public static String MATCH ="--match";
	
	//Para criar as linhas contento a media e desvio padrão
	public static String GENERATE_MEAN ="--generateMean";
	

    //Utiliza o centroid para gerar saida com porcentagens explicando anomalia
	 public static String CENTROID ="--centroid";
	 
	 //Utiliza o centroid para gerar saida com porcentagens explicando anomalia
	 public static String DEBUGG_CENTROID ="--debug_centroid";
	 
	 //rankear top x categorias anormais de cada instancia
	 public static String RANKING ="--ranking";
		
	//Substitui pontos por virgula
		public static String HELPER ="--helper";
	
		//Substitui pontos por virgula
				public static String DEBUG ="--debug";	
		
	//Buffer para ler a entrada de arquivo ou console
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	public DecimalFormat df;
	
	

	public static void main(String[] args) throws IOException {
		
		String  parametro;
		
		Preprocessamento p = new Preprocessamento();
		p.df = new DecimalFormat("#0.00");
		DecimalFormatSymbols s = new DecimalFormatSymbols(Locale.US);
		p.df.setDecimalFormatSymbols(s);
		
		try {
			parametro = args[0];
		} catch (Exception e) {
			e.printStackTrace();
			parametro = "nulo";
		}
		
		if (parametro.equals(FINBRA_DPF_PAGAS)) {			
			p.moduloFinbra(args[1].equals("true"));
		} 
		else if(parametro.equals(REMOVE_ATRIBUTES)) {
			p.moduloClear(args);
		}else if(parametro.equals(NORMALIZE)) {
			p.moduloNormalize(args);
		}else if(parametro.equals(RELATIVE)) {
			p.moduloRelative(args);
		}else if(parametro.equals(GENERATE_MEAN)) {
			p.moduloGenerateMean(args);
		}else if(parametro.equals(HELPER)) {
			p.moduloExcelHelper(args);
		}else if(parametro.equals(MATCH)) {
			p.moduloMatch(args);
		}else if(parametro.equals(CENTROID)) {
			p.moduloExplanation(args);
		}else if(parametro.equals(RANKING)) {
			p.moduloRanking(args);
		}else if(parametro.equals(FINBRA_DPF_PAGAS_OLD)) {
			p.moduloMergeCod(args);
		}else if(parametro.equals(FIX_COD_IBGE)) {
			p.moduloFixCodIbge(args);
		}else if(parametro.equals(DEBUG)) {
			p.moduloDebug(args);
		}else if(parametro.equals(DEBUGG_CENTROID)) {
			p.moduloDebugCentroid(args);
		}else{			
			System.out.println("Parametro inválido");
		}

	}
	
	
	private void moduloDebugCentroid(String[] args) throws IOException {
		String line;
		while((line=in.readLine())!=null) {
			String cols[] = line.split(";");
			System.out.println(cols.length);
		}
		
	}


	private void moduloDebug(String[] args) throws IOException {
		String line;
		while((line=in.readLine())!=null) {
			
			System.out.println(line);
		}
		
	}


	private void moduloFixCodIbge(String[] args) throws IOException {
		// TODO remove numero extra do ibge das colunas dos anos 20013 2014 e 2015
		
		String line;
		while((line=in.readLine())!=null) {
			String[] cols = line.split(";");
			if(cols[0].charAt(0) == '#'){
				System.out.println(line);		
				//senao se tiver no minimo o LOF que procuramos prossegue
			}else{
				cols[1] = cols[1].trim();
				cols[1] = cols[1].substring(0, cols[1].length()-1);
				String outputLine = "";
				for(int i = 0; i < cols.length ; i++){					
						outputLine = outputLine.length() > 1 ? outputLine+";"+cols[i] : outputLine+cols[i]; 					
				}
				System.out.println(outputLine);	
			}
			}
		
	}


	private void moduloMergeCod(String[] args) throws IOException {
		// merge coluna 0 e 1 para gerar cod ibge dessas instancias
		String line;
		while((line=in.readLine())!=null) {
			String[] cols = line.split(";");
			if(cols[0].charAt(0) == '#' || cols[0].charAt(0) == '"'|| cols[0].charAt(0) == 'C'){
				if(cols[0].charAt(0) != '#')
					cols[0] = "#"+cols[0];
				line = line.toLowerCase().replaceAll("cduf;cdmun", "CodIBGE");
				line = line.toLowerCase().replaceAll("cduf\";\"cdmun", "CodIBGE");
				System.out.println(line);		
				//senao se tiver no minimo o LOF que procuramos prossegue
			}else{
				if(cols[0].contains(",") ||cols[0].contains(".")  )
					cols[0] = cols[0].split("[,.]")[0];
				if(cols[1].contains(",") ||cols[1].contains(".")  )
					cols[1] = cols[1].split("[,.]")[0];
				
				String cod = cols[1].trim();
				while(cod.length() < 4)
					cod = "0"+cod;
				cols[1] = cols[0].trim()+ cod;
				String outputLine = "";
				for(int i = 1; i < cols.length ; i++){					
						outputLine = outputLine.length() > 1 ? outputLine+";"+cols[i] : outputLine+cols[i]; 					
				}
				System.out.println(outputLine);	
			}
			}
	}


	//MODULO GERAR OS RANKINGS DAS CATEGORIAS QUE MAIS INFLUENCIARAM PARA A INSTANCIA SER CONSIDERADA ANORMAL (DE ACORDO COM O METODO DO CENTROID)
	private void moduloRanking(String[] args) throws IOException  {
		
		int topSize = Integer.valueOf(args[1]);//TAMANHO DO RANKING EXEMPLO TOP 5 OU TOP 10 CATEGORIAS
		
		int colsInit = Integer.valueOf(args[2].split("-")[0]); // COL INICIAS DAS CATEGORIAS
		int colsEnd = Integer.valueOf(args[2].split("-")[1]); //COL FINAL DAS CATEGORIAS
		//int scoresIni = Integer.valueOf(args[3].split("-")[0]);
		//int scoresEnd = Integer.valueOf(args[3].split("-")[1]);
		ArrayList<String> arrayOut = new ArrayList<>(); // ARRAY PARA SAIDA DOS DADOS
		TreeMap<String, String> arrayAux = new TreeMap<>(Collections.reverseOrder()); // ARVORE PARA AUXILIAR EM MANTER DADOS ORDENADOS POR PORCENTAGEM
		ArrayList<String> header = new ArrayList<>(); // CABEÇALHO ORIGINAL PARA AUXILIAR NO PROCESSO
		String line ="";
		int countLine = 1;
		while((line=in.readLine())!=null) {
			String[] cols = line.split(";");
			if(countLine == 1){
				countLine++;
				//gerar headers de acordo
				arrayOut = new ArrayList<>();			
				
				for(int i = 0; i < cols.length ; i++){					
					header.add(cols[i]);					
				}
				
				for(int i = 0; i < colsInit ; i++){					
					arrayOut.add(cols[i]);
					
				}
				//GERANDO CABEÇALHOS TOP X
				for(int i = 1, j = colsInit; j <= colsEnd ; i++, j++){	
					String colunm = "Top "+i+" Prefeitura / Vizinhos / Diferenca";
					arrayOut.add(colunm);
//					
				}
				
				for(int i = colsEnd+1; i < cols.length ; i++){					
					arrayOut.add(cols[i]);
				}
				
				printArray(arrayOut);
			}else{
				arrayOut = new ArrayList<>();
				arrayAux = new TreeMap<>(Collections.reverseOrder());
				for(int i = 0; i < colsInit ; i++){					
					arrayOut.add(cols[i]);
				}
				
				for(int i = colsInit; i <= colsEnd ; i++){
					String colResult[] = cols[i].split("/");
					String diff = String.valueOf( df.format(new Double(colResult[1]) - new Double(colResult[2])));
					colResult[1] = colResult[1].trim()+" /";
					colResult[2] = colResult[2].trim()+" /";
					diff = diff;
					String value = "";
					
					if(colResult[0].trim().length() == 5)
						colResult[0]= "0"+colResult[0].trim();
				
					
					colResult[0] = colResult[0].trim()+" "+header.get(i);
					
					for(String col : colResult)
						value+=col+" ";
					value+=diff;
					
					arrayAux.put(colResult[0].trim()+" "+header.get(i), value);
					
				}
				
				
				for(Entry<String, String> entry : arrayAux.entrySet()){					
					String value = entry.getValue();					
					arrayOut.add(value);	
				}
				
				for(int i = colsEnd+1; i < cols.length ; i++){					
					arrayOut.add(cols[i].replace(",", "."));
				}
				
				printArray(arrayOut);
				
			}
			}
		
	}

	//Modulo para gerar saida mostrando em porcentagem os valores que fizeram com que a instancia fosse considerada anomalia.
	private void moduloExplanation(String[] args) throws IOException {
		int colPopulation = Integer.valueOf(args[2]);
		int colId = Integer.valueOf(args[3]);
		int neighbors =  Integer.valueOf(args[1]);
		int colValuesInit = Integer.valueOf(args[4].split("-")[0]);
		int colValuesEnd= Integer.valueOf(args[4].split("-")[1]);
		int colLOF = Integer.valueOf(args[5]);
		double minLOF= Double.valueOf(args[6]);
		double maxGoodLOF= Double.valueOf(args[7]);
		
		//com o treeMap teremos a tabela ordenada pelo parametro key, que sera a populacao
		TreeMap<Double,ArrayList<String>> treeMap = new TreeMap<>();
		
		//este ira armazenar todas instancias
		TreeMap<Double,ArrayList<String>> treeMapAll = new TreeMap<>();
		
		
		
		ArrayList<String> aux = new ArrayList<>();
		String line = "";
		
		while((line=in.readLine())!=null) {
			String[] cols = line.split(";");
			if(cols[0].charAt(0) == '#'){
				System.out.println(line);		
				//senao se tiver no minimo o LOF que procuramos prossegue
			}else{
				//considerando casos de populacao repetida somaremos o log10 do id da instancia - 6 para gerar um double unico
				Double key = new Double( cols[colPopulation]) + (Math.log10( new Double(cols[colId]))-6);
				aux = new ArrayList<>();
				for(int i = 0; i< cols.length ; i++){
					aux.add(cols[i]);
				}
				treeMapAll.put(key, aux);	
				
				//caso seja anomalia add a treeMap 
				if(Double.valueOf(cols[colLOF]) >= minLOF){						
					treeMap.put(key, aux);			
				
				}
				
			} 
				
				
			
		}
		int pos = 0;
		//array para auxiliar nas posicoes das keys
		//Double[] mapKeys = new Double[treeMap.size()];
		
		Double[] mapKeysAll = new Double[treeMapAll.size()];
		//salvando chaves ja ordenadas pelo treeMap para poder percorrer array usando posicoes em seguida
		/*for(Entry<Double,ArrayList<String>> entry : treeMap.entrySet()){
			Double key = entry.getKey();	
			mapKeys[pos++] = key;	
		}*/
		
		//salva as chaves de todos instancias
		for(Entry<Double,ArrayList<String>> entry : treeMapAll.entrySet()){
			Double key = entry.getKey();	
			mapKeysAll[pos++] = key;	
		}
		
		
		
		//para cada instancia verifica se é uma anomalia, se sim verifica os X vizinhos depois e os X vizinhos antes
		for(int i = 0 ; i< mapKeysAll.length ; i++){
			Double iKey = mapKeysAll[i];
			if(treeMap.containsKey(iKey)){
				//array dos centroids
				ArrayList<Double> centroids = new ArrayList<>();
				//inicializa array ddos centroids desta instancia
				for(int col = colValuesInit; col <= colValuesEnd; col++){
					centroids.add( 0.0);
					
				}				
				
				int toCheckAfter = neighbors/2;
				int toCheckBefore = neighbors/2;
				
				int iAnomaly = i; //index anomaly
				
				//check  X neighbors after
				int iNext = iAnomaly + 1;
				while(iNext < mapKeysAll.length && toCheckAfter > 0 ){
					Double iNextKey = mapKeysAll[iNext];
					ArrayList<String> iNextValue = treeMapAll.get(iNextKey);
					//verifica se possui LOF baixo aceitavel
					if(Double.valueOf( iNextValue.get(colLOF)) <= maxGoodLOF){
						//para cada coluna faz a somatoria dos valores armazenando no array do centroid
						for(int col = colValuesInit, k = 0; col <= colValuesEnd; col++, k++){
							centroids.set(k, centroids.get(k)+ new Double (iNextValue.get(col)));						
						}						
						toCheckAfter--;
						
					}
					
					iNext++;
					
				}
				
				//caso terminou array e possui pendencias para checar
				toCheckBefore += toCheckAfter;
				iNext = iAnomaly-1;
				
				//agora vamos checar as instancias que vem antes da anomalia
				while(iNext > 0 && toCheckBefore > 0 ){
					Double iNextKey = mapKeysAll[iNext];
					ArrayList<String> iNextValue = treeMapAll.get(iNextKey);
					//verifica se possui LOF baixo aceitavel
					if(Double.valueOf( iNextValue.get(colLOF)) <= maxGoodLOF){
						//para cada coluna faz a somatoria dos valores armazenando no array do centroid
						for(int col = colValuesInit, k = 0; col <= colValuesEnd; col++, k++){
							centroids.set(k, centroids.get(k)+ new Double (iNextValue.get(col)));						
						}						
						toCheckBefore--;
						
					}
					
					iNext--;
					
				}
				//caso nao conseguiu checar todos
				int total = neighbors - toCheckBefore;
				
				//soma dos valores é dividida pelo total obtendo assim a média/centroid
				for(int j = 0; j < centroids.size(); j++){
					centroids.set(j, centroids.get(j)/total);
				}
				//System.out.println(centroids.toString());
				
				//map auxiliar para atualizar o tree map
				ArrayList<String> anomaly = new ArrayList<>(treeMapAll.get(mapKeysAll[iAnomaly]));
				
				Double sum = 0.0;
				//subtrai cada valor pelo centroid			
				for(int x = colValuesInit, y = 0; x <= colValuesEnd; x++, y++){
					Double actualValue = new Double(anomaly.get(x));
					Double difValue = Math.abs(actualValue - centroids.get(y));
					anomaly.set(x, String.valueOf(difValue));
					sum+= difValue;
					
				}
				
				//cada valor recebe ele mesmo dividico pela soma 
				for(int x = colValuesInit , y = 0; x <= colValuesEnd; x++, y++){
					Double actualValue = new Double(anomaly.get(x));
					String value = String.valueOf(df.format(actualValue*100/sum));	
					value+="% / "+treeMapAll.get(mapKeysAll[i]).get(x)+" / "+df.format(centroids.get(y));
					anomaly.set(x, value);					
					
				}
				
				//saida
				
				printArray(anomaly);
				
			}
			
		}	
		
		
	}
	
	private void printArray(ArrayList<String> array ){
		 String valueLine = array.toString().replace("[", "");
		  valueLine = valueLine.toString().replace("]", "");
		   valueLine = valueLine.toString().replace(",", ";");
		    System.out.println(valueLine);
	}

	//METODO LE E ARMAZENA SCORES LOF PARA CADA ID DA CIDADE
	//EM SEGUIDA ADICIONA UMA COLUNA A CADA CIDADE(LINHA) OLHANDO PELO ID SEU SCORE
	private void moduloMatch(String[] args) throws IOException {
		String line = "";
		String colHeader = args[1];
		HashMap<String, String> scoreMap = new HashMap<>();
		while((line=in.readLine())!=null) {
			String[] cols = line.split(";");
			if(cols.length == 2){
				scoreMap.put(cols[0],cols[1]);
				
			}else{
				if(cols[0].charAt(0) == '#'){
					System.out.println(line+";"+colHeader);
				}else{
					System.out.println(line+";"+df.format(new Double(scoreMap.get(cols[1]))) );
				}
				
			}
		}
		
	}

	//METODO QUE CALCULAR A MEDIA E DESVIO PADRAO PARA AS COLUNAS.
	//IMPRIMINDO AO FINAL UMA LINHA PARA MEDIA E UMA PARA O DESV. P. NO INICIO DO ARQUIVO APOS O HEADER
	private void moduloGenerateMean(String[] args) throws IOException {
		String line = "";		
		ArrayList<Integer> ignoreCols = getColsToIgnore(args[1]);
		
		ArrayList<String> mean = new ArrayList<>();
		ArrayList<String> deviation = new ArrayList<>();
		
		ArrayList<ArrayList<String>> table = new ArrayList<>();		
		
		while((line=in.readLine())!=null) {
			line = line.replace(",", ".");
			String[] cols = line.split(";");
			ArrayList<String> aux = new ArrayList<>();
			if(cols[0].charAt(0)!= '#'){
				//percorre cada coluna
				for(int i = 0 ; i < cols.length ; i++ ){
					aux.add(cols[i]);
					String meanValue = ""; 					
					//caso seja uma celuna do parametro ignore[x,y] coloca um * no valor senao atualiza a somatoria 
					if(!ignoreCols.contains(new Integer(i))){
						meanValue = cols[i];
						if(mean.size()<= i){
							mean.add(meanValue);
						}else{
							String value = String.valueOf( new Double(mean.get(i)) + new Double(meanValue));
							mean.set(i, value);
						}
						
					}else{
						meanValue = "*";
						if(mean.size()<= i){
							mean.add(meanValue);
						}
						
					}				
					
					
				}
				table.add(aux);
				
			}else{
				System.out.println(line);
			}
		}
		//divide somatoria pelo numero de instancias oque nos tras a media para cada gasto
		
		int total = table.size();
		
		for(int i = 0 ; i< mean.size(); i++ ){
			if(!ignoreCols.contains(new Integer(i))){
				mean.set(i, String.valueOf(df.format(new Double(mean.get(i))/total)));
				
				//aproveitando o loopa para fazer o calculo do desvio padrao para cada coluna i e linha j
				double devStd = (double) 0;
				for(int j = 0 ; j< total ; j++){
					devStd+= Math.pow(Double.valueOf(table.get(j).get(i)) - Double.valueOf(mean.get(i)), 2);
				}
				devStd = devStd/total ;
				devStd = Math.sqrt(devStd);
				deviation.add(String.valueOf(df.format(devStd)));
				
			}else{
				deviation.add("*");
			}
		}
		
		
		//saida dos dados		
		String lineOut ="";
		for(int i = 0 ; i< mean.size(); i++ )
			lineOut = i == 0 ? "#Media"+mean.get(i): lineOut + ";"+mean.get(i);			
			
		System.out.println(lineOut);
		
		lineOut ="";
		
		for(int i = 0 ; i< deviation.size(); i++ )
			lineOut = i == 0 ? "#Desvio Padrao"+deviation.get(i): lineOut + ";"+deviation.get(i);			
			
		System.out.println(lineOut);
		
		lineOut ="";
		
		for(ArrayList<String> row : table){
			for(int i = 0 ; i< row.size(); i++ ){
				lineOut = i == 0 ? row.get(i): lineOut + ";"+row.get(i);
			}
			System.out.println(lineOut);
		}
			
		
	}
	

	//MODULO PARA GERAR SAIDA COM VALORES DOS GASTOS RELATIVOS COM POPULACAO
	public void moduloRelative(String[] args) throws IOException {
		
		int relativeCol = Integer.valueOf(args[1]);		
		
		ArrayList<Integer> ignoreCols = getColsToIgnore(args[2]); 
		
		
		String line = "";
		while((line=in.readLine())!=null) {
			String[] cols = line.split(";");
			String lineOut = "";			
			float value;
			//se nao for cabecalho procede senao apenas imprime o cabecalho
			if(cols[0].charAt(0)!= '#'){
				float relativeValue = new Float(cols[relativeCol]);
				for(int i = 0 ; i< cols.length ; i++){
					if(i!= relativeCol){
						if(i > 0)
							lineOut+=";";
						if(!ignoreCols.contains(new Integer(i))){
							float realValue = new Float(cols[i]);
							lineOut+= df.format( realValue/relativeValue);
						}else{
							lineOut+= cols[i];
						}
					}
				}
			}else{
				lineOut = line.replace(cols[relativeCol]+";", "");	
				
			}
			
			System.out.println(lineOut);
		}	
	}
	
	
	//MODULO NORMALIZACAO PARA SUAVIZACAO DOS VALORES POR LOG, OU MEDIA E DESVIO PADRAO
	private void moduloNormalize(String[] args) throws IOException {
		String operation = args[1];
		String ignoreParams = args[2].replace("ignore[", "");
		ignoreParams = ignoreParams.replace("]", "");		
		String ignoreList[] = ignoreParams.split(",");
		ArrayList<Integer> ignoreCols = new ArrayList<>();
		for(String item : ignoreList)
			ignoreCols.add(new Integer(item));
		
		String line = "";
		int lineCount = 0;
		
		ArrayList<String> mean = new ArrayList<>();
		ArrayList<String> deviation = new ArrayList<>();
		while((line=in.readLine())!=null) {
			lineCount++;
			String[] cols = line.split(";");
			String lineOut = "";			
			double value;
			//se nao for cabecalho procede senao apenas imprime o cabecalho
			if(cols[0].charAt(0)!= '#'){
				for(int i = 0 ; i< cols.length ; i++){
					//se colunao nao estiver na lista para ignorar
					if(!ignoreCols.contains(new Integer(i))){						
						switch (operation) {
							case "log":
								value = Float.valueOf( cols[i]);
								if(value > 0){
									value =  (double) Math.log10(value);																		
								}
								break;
								
							case "devMean":
								value = Double.valueOf( cols[i]);								
									double meanValue = Double.valueOf(mean.get(i));
									double devValue = Double.valueOf(deviation.get(i));
									value =  (value - meanValue)/ devValue ;																		
								
								break;
	
							default:
								value = new Double(cols[i]);
								break;
						}
						if(i>0)
							lineOut+= ";"+ df.format(value);
						else
							lineOut+= df.format(value);
					}else{
						lineOut += cols[i];
						
					}					
				}
			}else{
				lineOut = line;				
					if(lineCount == 2){
						for(String col : cols){
							mean.add(col);
						}
						//lineOut = lineOut.replace(".", ",");
					}
					else if(lineCount == 3){
						for(String col : cols){
							deviation.add(col);
						}
						//lineOut = lineOut.replace(".", ",");
					}
				
			}
			
			System.out.println(lineOut);
		}
		
	}

	
	//MODULO PARA REMOVER COLUNAS NAO NECESSARIAS
	public void moduloClear(String[] args) throws IOException {		
		ArrayList<Integer> atributesIndex = new ArrayList<>();
		boolean isLof = args[1].contains("-lof");
		//separar a string por ; ou espaço em branco
		String separator = args[1].replace("-lof", " ");
		
		if(!isLof)
			for(int i = 2 ; i< args.length ; i++)
				atributesIndex.add(new Integer(args[i]));
		
		String line = "";
		
		
		while((line=in.readLine())!=null) {
			String[] cols = line.split(separator);
			String outputLine = "";			
			//pula linhas que nao sao instancias (ou seja nao possuem atributos)
			if(cols.length > 1){
				
				if(cols[0].charAt(0)== '#')
					outputLine = "#";
				
				if(!isLof){
					for(int i = 0; i < cols.length ; i++){
						if(!atributesIndex.contains(Integer.valueOf(i))){
							outputLine = outputLine.length() > 1 ? outputLine+";"+cols[i] : outputLine+cols[i]; 
						}
					}
				}else{
					outputLine =cols[cols.length-2] +";"+cols[cols.length-1].replace("lof-outlier=", "");  
				}
				
				System.out.println(outputLine);
				
			}
			
		}
		
	}
	
	
	//MODULO PARA ajudar ao abrir no excel
	public void moduloExcelHelper(String[] args) throws IOException {
		String line = "";	
		while((line=in.readLine())!=null) {
			String[] cols = line.split(";");
			if(cols[0].charAt(0) == '#'){
				System.out.println(line);
			}else{
				System.out.println(line.replace(".", ","));
			}
		}
		while((line=in.readLine())!=null) {
			String[] cols = line.split(";");
			if(cols[0].charAt(0) == '#'){
				System.out.println(line);
			}else{
				System.out.println(line.replace(".", ","));
			}
		}
		
	}

	//MODULO PARA GERAR SAIDA COM DIMENSAO N SENDO QUE N = (DIMENSAO ATUAL + NUMERO DE CATEGORIAS). 
	public void moduloFinbra(boolean ignoreSpecifics) throws IOException{
		//string que ira armazenar a leitura das linhas
		String line = "";
		//estrutura que ira conter os dados
		HashMap<String, TreeMap<String,String>> table = new HashMap<String,TreeMap<String,String>>();
		//estrutura auxiliar para armazenar os dados
		TreeMap<String,String> aux;		
		//flag para controle se a linha eh um cabecalho
		boolean isHeader = true;
		//palavra chave do cabecalho
		String headerKey ="";
		ArrayList<String> headerAtributes = new ArrayList<>();
		//enquanto entrada nao eh nulo le proxima linha
		while((line=in.readLine())!=null) {
			
			String[] cols = line.split(";");
			aux = new TreeMap<String,String>();
			String key = "";
			//verifica pelo tamanho se a linha é uma instancia ou apenas um comentario
			if(cols.length > 1){
				//verifica se a linha nao é o cabeçalho
				if(!isHeader){
					boolean ignore = true;
					if(ignoreSpecifics){
						if(!cols[4].contains(".")){
							ignore = false;
						}
					}else {
						ignore = false;
						
					}
					
					//prossegue de acordo com o parametro ignoreSpecifics
					if(!ignore){
						key = cols[0];
						//verifica se essa instancia ja existe no hashmap
						if(!table.containsKey(key)){
							int i = 1;
							for(String attribute : headerAtributes){
								aux.put(table.get(headerKey).get(attribute), cols[i]);								
								i++;
							}
							/*for(int i = 1; i < 4; i++){
								aux.put(table.get(headerKey).get("0 - "+cols[i]), cols[i]);
							}*/
							table.put(cols[0], aux);
						}
						//verifica se a categoria ja existe para essa instancia
						if(!table.get(cols[0]).containsKey(cols[4])){
							table.get(cols[0]).put(cols[4], cols[5]);
							if(!table.get(headerKey).containsKey(cols[4])){
								table.get(headerKey).put(cols[4], cols[4]);							
							}
						}else{
							//caso ja exista um valor para esta categoria este valor é incrementado
							Double d1 = new Double(cols[5]);
							Double d2 = new Double(table.get(cols[0]).get(cols[4]));						
							table.get(cols[0]).put(cols[4], String.valueOf(d1+d2));
						}
					}					
				}
				//caso seja o header
				else{	
					for(int i = 1; i < 4; i++){
						String atributeKey = cols[i];
						aux.put(atributeKey, atributeKey);
						headerAtributes.add(atributeKey);
					}
					table.put(cols[0], aux);
					headerKey = cols[0];
					isHeader = false;
				}
			}
			
		}
		
		//Saida
		
		System.out.println("#Total intâncias: "+table.size()+" Atributos: "+table.get(headerKey).size());
		
		//header
		  System.out.print("#"+headerKey);
		  
		  for (String atribute : headerAtributes ){
			  System.out.print(";"+table.get(headerKey).get(atribute));			
		  }
		  
		  for (Entry<String,String> entry : table.get(headerKey).entrySet())
			  if(!headerAtributes.contains(entry.getValue()))
				  System.out.print(";"+entry.getValue());
		  
		  System.out.println();
		  
		//instancias  
		for (Entry<String, TreeMap<String, String>> entry : table.entrySet()) {
		    String key = entry.getKey();
		    TreeMap value = entry.getValue();
		    
		    //caso seja o header, ignora pois ele ja foi impresso
		    if(!key.equals(headerKey)){
		    	System.out.print(key);
		    	for (String atribute : headerAtributes ){
					  System.out.print(";"+table.get(key).get(atribute));			
				  }
		    	//os valores do header sao as chaves para cada instancia.
		    	for (Entry<String,String> entryAux : table.get(headerKey).entrySet()){
		    		if(!headerAtributes.contains(entryAux.getValue())){
			    		String keyAux = entryAux.getValue();
			    		String valueAux ="0";
			    		//caso esta instancia possua um valor para a categoria atualiza valueAux senao fica NONE
					    if(value.containsKey(keyAux)){
					    	valueAux = (String) value.get(keyAux).toString().replace(",", ".");
					    }
					    System.out.print(";"+valueAux);
		    		}
		    	}
		    	System.out.println();
		    }
		}
		
	}
	
	//METODO PARA IDENTIFICAR NO ARGUMENTO DE PARAMETRO (ignore[x,y,z]) AS COLUNAS A SEREM IGNORADAS 
		public ArrayList<Integer> getColsToIgnore(String arg){
			String ignoreParams = arg.replace("ignore[", "");
			ignoreParams = ignoreParams.replace("]", "");	
			String ignoreList[] = ignoreParams.split(",");
			ArrayList<Integer> ignoreCols = new ArrayList<>();
			for(String item : ignoreList)
				ignoreCols.add(new Integer(item));
			return ignoreCols;
		}

}
