package helper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import model.Ano;
import model.Despesa;
import model.Prefeitura;

public class InputReader {
	private FileReader in ;
	private BufferedReader buffer ;
	private String line;
	
	public void readDespesas(TreeSet<Despesa> despesas,String url) throws IOException{
		in = new FileReader(url);
		buffer = new BufferedReader(in);
		while((line=buffer.readLine())!=null){
			//boolean isComment = line.trim().charAt(0)== '#';
			String cols[] = line.split("-");
			cols[0] = cols[0].trim();
					
			cols[1] = cols[1].trim();
			
			Despesa aux = new Despesa(cols[0], cols[1], 0.0f);
			despesas.add(aux);
			
		}
	}
	
	//PARA 2013, 2014 e 2015
	public void readAnoRecente(TreeSet<Despesa> despesas,ArrayList<Ano> anos, String url, int commentLines, int ano) throws IOException{
		in = new FileReader(url);
		buffer = new BufferedReader(in);
		int lineNumber = 0;
		ArrayList<Prefeitura> prefeituras = new ArrayList<>();
		while((line=buffer.readLine())!=null){
			if(lineNumber >= commentLines){
				line = line.replace("\"", "");
				String cols[] = line.split(";");
				String name = cols[0].split(" - ")[0].toUpperCase().replaceAll("PREFEITURA MUNICIPAL DE ", "");
				String codigo = cols[1].trim().substring(0,6);
				String UF = cols[2].trim();
				String population = cols[3].trim();
				String despesa = "";
				//caso nao seja despesa exceto intra ou despesa intraorcamentaria
				if(cols[4].toUpperCase().contains("INTRA")){
					if(cols[4].toUpperCase().contains("EXCETO")){
						despesa = "00";
					}else{
						despesa = "29";							
					}
					
				}else{
					String despAux [] =  cols[4].split("-");
					despesa = despAux[0].trim();
				}
				
				String despValor = cols[5].trim().replace(",", ".");
				Prefeitura aux = new Prefeitura(Integer.valueOf(codigo), name, UF, Integer.valueOf(population));
				int indexPrefeitura = prefeituras.indexOf(aux);
				if(indexPrefeitura < 0){
					TreeSet<Despesa> auxDespesas = new TreeSet<>();
					for(Despesa d : despesas){
						Despesa auxD = new Despesa(d.getCodigo(), d.getNome(), d.getValor());
						auxDespesas.add(auxD);
					}
					aux.setDespesas(auxDespesas);
					prefeituras.add(aux);		
					indexPrefeitura = prefeituras.indexOf(aux);						
					
				}
				Despesa auxDesp = new Despesa(despesa, " ", 0.0f);					
				int indexDespesa = prefeituras.get(indexPrefeitura).getDespesas().indexOf(auxDesp);
				if(indexDespesa > -1){
					prefeituras.get(indexPrefeitura).getDespesas().get(indexDespesa).setValor(Double.parseDouble(despValor));
					
				}else{
					System.out.println("###Index not found despesa: "+despesa+" LIne: "+lineNumber);
					
				}
				
			}
			//System.out.println(despesas.toString()); // NEVER EVER TRY IT HERE
			lineNumber++;
			
		}
		
		Ano auxYear = new Ano(ano);			
		auxYear.setPrefeituras(new ArrayList<Prefeitura>(prefeituras));			
		anos.add(auxYear);	
		
	}
	

}
