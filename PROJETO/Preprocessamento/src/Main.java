import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeSet;

import helper.InputReader;
import model.Ano;
import model.Despesa;
import model.DespesaHelper;
import model.Multienio;
import model.Prefeitura;

public class Main {
	public static String EXIT = "";
	public static String URL_DESPESAS = "D:/REPOSITORY/TCC/PROJETO/dados/INPUT/despesas.csv";	
	public static String OUTPUT_PATH = "D:/REPOSITORY/TCC/PROJETO/dados/OUTPUT/";
	public static String ELKI_RESULTS = "D:/REPOSITORY/TCC/PROJETO/dados/OUTPUT_ELKI/";
	public static String INPUT_PATH = "D:/REPOSITORY/TCC/PROJETO/dados/INPUT/";
	
	
	public static void main(String[] args) throws IOException {
		InputReader reader = new InputReader();
		TreeSet<Despesa> despesas = new TreeSet<Despesa>();		
		ArrayList<Ano> anos = new ArrayList<Ano>() ;		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#0.00",symbols);	
		String folder = args[0]+="/";
		OUTPUT_PATH+=folder;
		ELKI_RESULTS+=folder;
		boolean isAno = args[1].equals("ano");
		String ano = args[2];
		String anoEnd ="";
		boolean anoLoop =ano.contains("-");
		if(anoLoop){
			anoEnd= ano.split("-")[1];
			ano = ano.split("-")[0];
			
		}
		int anoInt = Integer.valueOf(ano);
		int anoIntEnd = anoLoop ? Integer.valueOf(anoEnd): -1 ;
		
		int commentsLine = anoInt > 2012 ?4 : 1;
		boolean isLastStep = args[3].equals("last");
		String originalFile = args[4];
		 //LENDO DESPESAS
		  reader.readDespesas(despesas,URL_DESPESAS);
		
		//lendo instancia
		  if(anoLoop){
			  for(int i = anoInt ; i <= anoIntEnd; i++){
				  commentsLine = anoInt > 2012 ?4 : 1;
				  String file = originalFile.toUpperCase().replace("XXXX", String.valueOf(i));
				  reader.readAnoRecente(despesas,anos, (INPUT_PATH+file), commentsLine, i); 
			  }
			  	
			
		  }else{
			  reader.readAnoRecente(despesas,anos, INPUT_PATH+originalFile, commentsLine, anoInt);
		  }
	if(isAno){
			
		if(!isLastStep){
				
			//PRIMEIRO PASSO, GERENDO ARQUIVO PARA APLICAR NO ELKI 
		
			
			  for(Ano a : anos)
				a.calcMeanAndDeviation(despesas);	
	
			boolean useCode = true;
			boolean minCategory = true;
			boolean hasHeader = true; 
	        boolean printExpenditureTOtal= false;			
			boolean printScores = false;
			boolean printName = false;
			anos.get(0).print(Ano.VALOR_RELATIVO_NORMALIZADO,printExpenditureTOtal,
					useCode,printName,minCategory, hasHeader, df, printScores, OUTPUT_PATH + ano+"_REL_NOR.csv");	
	    	anos.get(0).print(Ano.VALOR_SUAVIZADO_NORMALIZADO,printExpenditureTOtal,
	    			useCode,printName,minCategory, hasHeader, df, printScores, OUTPUT_PATH + ano+"_SUA_NOR.csv");	
	    	
	    
		}else{
			
			//LENDO SCORES	
			
				reader.readScores(anos.get(0), ELKI_RESULTS+"ELKI_"+ano+"_REL_NOR.txt","REL_NOR");			
				reader.readScores(anos.get(0), ELKI_RESULTS+"ELKI_"+ano+"_SUA_NOR.txt","SUA_NOR");		
				boolean useCode = true;
				boolean minCategory = true;
				boolean hasHeader = true; 
				boolean printExpenditureTOtal= true;
				boolean printScores = true;
				boolean printName = true;
				anos.get(0).print(Ano.VALOR_ABSOLUTO,printExpenditureTOtal,useCode,printName,
						minCategory, hasHeader, df, printScores, OUTPUT_PATH + ano+"_SCORED.csv");
		}		
	    			
		
	
		}else {//bienio ou trienio
			Multienio multienio = new Multienio();
			if(!isLastStep){
				for(Ano a : anos){
					a.calcMeanAndDeviation(despesas);
					multienio.addANo(a);
				}
				
				multienio.print(true, true,false, df, Ano.VALOR_RELATIVO_NORMALIZADO, false,OUTPUT_PATH +"_REL_NOR.csv");
				multienio.print(true, true,false, df, Ano.VALOR_SUAVIZADO_NORMALIZADO, false,OUTPUT_PATH +"_SUA_NOR.csv");
				
			}else{
				for(Ano a : anos){					
					multienio.addANo(a);
				}
				reader.readScores(multienio.getAnos().get(0), ELKI_RESULTS+"ELKI"+"_REL_NOR.txt","REL_NOR");			
				reader.readScores(multienio.getAnos().get(0), ELKI_RESULTS+"ELKI"+"_SUA_NOR.txt","SUA_NOR");	
				
				multienio.print(true, true,true, df, Ano.VALOR_ABSOLUTO, true,OUTPUT_PATH +"_SCORED.csv");
			}
			
		}
	}



	
	

}
