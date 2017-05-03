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
import model.Prefeitura;

public class Main {
	public static String EXIT = "";
	public static String URL_DESPESAS = "D:/REPOSITORY/TCC/PROJETO/dados/INPUT/despesas.csv";
	public static String URL_INSTANCIAS ="D:/REPOSITORY/TCC/PROJETO/dados/INPUT/YYYY_original.csv";
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
		String ano = "2013";
		int anoInt = 2013;
		boolean isLastStep = true;
		 //LENDO DESPESAS
		  reader.readDespesas(despesas,URL_DESPESAS);
		
		//lendo instancia
			reader.readAnoRecente(despesas,anos, URL_INSTANCIAS.replace("YYYY", ano), 4, anoInt);	
			
		
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
		
			reader.readScores(anos.get(0), ELKI_RESULTS+"LOF_"+ano+"_REL_NOR.txt",ano+"_REL_NOR");			
			reader.readScores(anos.get(0), ELKI_RESULTS+"LOF_"+ano+"_SUA_NOR.txt",ano+"_SUA_NOR");		
			boolean useCode = true;
			boolean minCategory = true;
			boolean hasHeader = true; 
			boolean printExpenditureTOtal= true;
			boolean printScores = true;
			boolean printName = true;
			anos.get(0).print(Ano.VALOR_ABSOLUTO,printExpenditureTOtal,useCode,printName,
					minCategory, hasHeader, df, printScores, OUTPUT_PATH + ano+"_LOF.csv");
	}		
    			
		
		
		//SEGUNDO PASSO, JA COM SCORES DO ELKI
		



	}



	
	

}
