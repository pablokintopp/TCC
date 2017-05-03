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
		int yearBegin = Integer.valueOf( args.length > 1 ? args[0] : "2014" );
		int yearEnd = Integer.valueOf( args.length > 1? args[1] : "2014");
		ArrayList<Ano> anos = new ArrayList<Ano>() ;		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#0.00",symbols);	
		
		 //LENDO DESPESAS
		  reader.readDespesas(despesas,URL_DESPESAS);
		
		//lendo instancias 2013 a 2015
		for(int ano = yearBegin ; ano <= yearEnd ; ano++)
			reader.readAnoRecente(despesas,anos, URL_INSTANCIAS.replace("YYYY", String.valueOf(ano)), 4, ano);	
		
		//LENDO SCORES
			reader.readScores(anos.get(0), ELKI_RESULTS+"LOF_2014_ABS.txt","2014_ABS");
			reader.readScores(anos.get(0), ELKI_RESULTS+"LOF_2014_REL.txt","2014_REL");
			reader.readScores(anos.get(0), ELKI_RESULTS+"LOF_2014_REL_NOR.txt","2014_REL_NOR");
			reader.readScores(anos.get(0), ELKI_RESULTS+"LOF_2014_SUA.txt","2014_SUA");
			reader.readScores(anos.get(0), ELKI_RESULTS+"LOF_2014_SUA_NOR.txt","2014_SUA_NOR");
		
		//Medias e desvio padrao para populacoes e despesas
//		for(Ano a : anos)
//			a.calcMeanAndDeviation(despesas);	
//
//		boolean useCode = true;
//		boolean minCategory = true;
//		boolean hasHeader = true; 
//      boolean printExpenditureTOtal= false;			
//		boolean printScores = false;
//		
//		anos.get(0).print(Ano.VALOR_ABSOLUTO,printExpenditureTOtal,useCode,minCategory, hasHeader, df, printScores, OUTPUT_PATH + "2014_ABS.csv");	
//		anos.get(0).print(Ano.VALOR_RELATIVO,printExpenditureTOtal,useCode,minCategory, hasHeader, df, printScores, OUTPUT_PATH + "2014_REL.csv");	
//		anos.get(0).print(Ano.VALOR_RELATIVO_NORMALIZADO,printExpenditureTOtal,useCode,minCategory, hasHeader, df, printScores, OUTPUT_PATH + "2014_REL_NOR.csv");	
//		anos.get(0).print(Ano.VALOR_SUAVIZADO,printExpenditureTOtal,useCode,minCategory, hasHeader, df, printScores, OUTPUT_PATH + "2014_SUA.csv");	
//		anos.get(0).print(Ano.VALOR_SUAVIZADO_NORMALIZADO,printExpenditureTOtal,useCode,minCategory, hasHeader, df, printScores, OUTPUT_PATH + "2014_SUA_NOR.csv");	
//			
		
		boolean useCode = false;
		boolean minCategory = true;
		boolean hasHeader = true; 
		boolean printExpenditureTOtal= true;
		boolean printScores = true;
		anos.get(0).print(Ano.VALOR_ABSOLUTO,printExpenditureTOtal,useCode,minCategory, hasHeader, df, printScores, OUTPUT_PATH + "2014_LOF_ALL.csv");


	}



	
	

}
