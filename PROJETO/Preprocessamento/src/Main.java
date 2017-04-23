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
	public static String URL_DESPESAS = "D:/REPOSITORY/TCC/PROJETO/dados/despesas.csv";
	public static String URL_INSTANCIAS ="D:/REPOSITORY/TCC/PROJETO/dados/YYYY_original.csv";
	public static String OUTPUT_PATH = "D:/REPOSITORY/TCC/PROJETO/dados/OUTPUT/";
	public static String INPUT_PATH = "D:/REPOSITORY/TCC/PROJETO/dados/INPUT/";
	
	
	public static void main(String[] args) throws IOException {
		InputReader reader = new InputReader();
		TreeSet<Despesa> despesas = new TreeSet<Despesa>();
		int yearBegin = Integer.valueOf( args.length > 1 ? args[0] : "2013" );
		int yearEnd = Integer.valueOf( args.length > 1? args[1] : "2015");
		ArrayList<Ano> anos = new ArrayList<Ano>() ;		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#0.00",symbols);	
		
		 //LENDO DESPESAS
		  reader.readDespesas(despesas,URL_DESPESAS);
		
		//lendo instancias 2013 a 2015
		for(int ano = yearBegin ; ano <= yearEnd ; ano++)
			reader.readAnoRecente(despesas,anos, URL_INSTANCIAS.replace("YYYY", String.valueOf(ano)), 4, ano);							
		
		//Medias e desvio padrao para populacoes e despesas
		for(Ano a : anos)
			a.calcMeanAndDeviation(despesas);	

		
		anos.get(1).print(true, true, df, false, "D:/output_2014.csv");
			
			
		


	}



	
	

}
