import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

import model.Despesa;
import model.Prefeitura;

public class Main {
	public static String EXIT = "";
	public static String URL_DESPESAS = "D:/REPOSITORY/TCC/PROJETO/dados/despesas.csv";
	public static String URL_INSTANCIAS ="D:/REPOSITORY/TCC/PROJETO/dados/YYYY_original.csv";
	public static String URL_SCORES = "D:/REPOSITORY/TCC/PROJETO/dados/input.csv";
	
	
	
	public static void main(String[] args) throws IOException {
		TreeSet<Despesa> despesas = new TreeSet<Despesa>();
		//TreeSet<Prefeitura> prefeituras = new TreeSet<Prefeitura>();
		
		FileReader in = new FileReader(URL_DESPESAS);
		BufferedReader buffer = new BufferedReader(in);
		String line;
		
		 //LENDO DESPESAS
		while((line=buffer.readLine())!=null){
			//boolean isComment = line.trim().charAt(0)== '#';
			String cols[] = line.split("-");
			cols[0] = cols[0].trim();
					
			cols[1] = cols[1].trim();
			
			Despesa aux = new Despesa(cols[0], cols[1], 0.0);
			despesas.add(aux);
			
		}
		//lendo instancias 2013 a 2015
		for(int ano = 2013 ; ano <= 2015 ; ano++){
			in = new FileReader(URL_INSTANCIAS.replace("YYYY", String.valueOf(ano)));
			buffer = new BufferedReader(in);
			//4 linhas comentarios
			//Instituição;Cod.IBGE;UF;População;Conta;Valor
			int lineNumber = 0;
			ArrayList<Prefeitura> prefeituras = new ArrayList<>();
			while((line=buffer.readLine())!=null){
				if(lineNumber >= 4){
					line = line.replace("\"", "");
					String cols[] = line.split(";");
					String name = cols[0].split(" - ")[0].toUpperCase().replaceAll("PREFEITURA MUNICIPAL DE ", "");
					String codigo = cols[1].trim().substring(0, 5);
					String UF = cols[2].trim();
					String population = cols[3].trim();
					String despAux [] =  cols[4].toLowerCase().contains("intra") ? new String[]{cols[4]} : cols[4].split("-");
					
					String despesa = despAux.length > 1 ? despAux[0].trim() : despAux[0].toLowerCase().contains("exceto") ? "00": "29";
					String despValor = cols[5].trim().replace(",", ".");
					Prefeitura aux = new Prefeitura(Integer.valueOf(codigo), name, UF, Integer.valueOf(population));
					int indexPrefeitura = prefeituras.indexOf(aux);
					if(indexPrefeitura < 0){				
						aux.setDespesas(despesas);
						prefeituras.add(aux);		
						indexPrefeitura = prefeituras.indexOf(aux);						
						
					}
					Despesa auxDesp = new Despesa(despesa, " ", 0.0);					
					int indexDespesa = prefeituras.get(indexPrefeitura).getDespesas().indexOf(auxDesp);
					if(indexDespesa > 0){
						prefeituras.get(indexPrefeitura).getDespesas().get(indexDespesa).setValor(Double.valueOf(despValor));
						
					}else{
						System.out.println("###Index not found despesa: "+despesa);
						
					}
					
				}
				lineNumber++;
				
			}
			for(Prefeitura p : prefeituras)
				System.out.println(p.toString());
			
		}
//		for(Despesa d : despesas){
//			System.out.println(d.getCodigo()+"-"+d.getNome());
//			
//		}

	}

}
