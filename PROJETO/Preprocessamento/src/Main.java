import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;
import model.Ano;
import model.Despesa;
import model.Prefeitura;

public class Main {
	public static String EXIT = "";
	public static String URL_DESPESAS = "D:/REPOSITORY/TCC/PROJETO/dados/despesas.csv";
	public static String URL_INSTANCIAS ="D:/REPOSITORY/TCC/PROJETO/dados/YYYY_original.csv";
	public static String URL_SCORES = "D:/REPOSITORY/TCC/PROJETO/dados/input.csv";
	
	
	
	public static void main(String[] args) throws IOException {
		TreeSet<Despesa> despesas = new TreeSet<Despesa>();
		ArrayList<Ano> anos = new ArrayList<>();
		
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
		String output1 = "";
		for(Despesa d : despesas){
			if(d.getCodigo().contains(".") == false && d.getCodigo().equals("00") == false){
				output1+=";"+d.getValor();		
			}
		}
		System.out.println(output1);
		
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
							Despesa auxD = d;
							auxDespesas.add(auxD);
						}
						aux.setDespesas(auxDespesas);
						prefeituras.add(aux);		
						indexPrefeitura = prefeituras.indexOf(aux);						
						
					}
					Despesa auxDesp = new Despesa(despesa, " ", 0.0);					
					int indexDespesa = prefeituras.get(indexPrefeitura).getDespesas().indexOf(auxDesp);
					if(indexDespesa > -1){
						prefeituras.get(indexPrefeitura).getDespesas().get(indexDespesa).setValor(Double.valueOf(despValor));
						
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
			
//			for(Prefeitura p : auxYear.getPrefeituras()){
//				String output ="";
//				output+= ano;
//				output+=";"+p.getCodigo();
//				output+=";"+p.getPopulacao();
//				for(Despesa d : p.getDespesas()){
//					if(d.getCodigo().contains(".") == false && d.getCodigo().equals("00") == false){
//						output+=";"+d.getValor();		
//					}
//				}
//				System.out.println(output);
//			}
			
		}
		//header
		
//		String output ="#";
//		output+="Ano";
//		output+=";Cidade";
//		output+=";População";
//		for(Despesa d : anos.get(0).getPrefeituras().get(0).getDespesas()){
//			if(d.getCodigo().contains(".") == false && d.getCodigo().equals("00") == false){
//				output+=";"+d.getNome();		
//			}
//		}
//		System.out.println(output);
//		
//		for(Ano a : anos){
//			for(Prefeitura p : a.getPrefeituras()){
//				output ="";
//				output+=a.getValor();
//				output+=";"+p.getCodigo();
//				output+=";"+p.getPopulacao();
//				for(Despesa d : p.getDespesas()){
//					if(d.getCodigo().contains(".") == false && d.getCodigo().equals("00") == false){
//						output+=";"+d.getValor();		
//					}				
//				}
//				System.out.println(output);
//			}
//		}
		String output = "";
		for(Despesa d : despesas){
			if(d.getCodigo().contains(".") == false && d.getCodigo().equals("00") == false){
				output+=";"+d.getValor();		
			}
		}
		System.out.println(output);


	}

}
