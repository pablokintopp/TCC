import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeSet;
import model.Ano;
import model.Despesa;
import model.DespesaHelper;
import model.Prefeitura;

public class Main {
	public static String EXIT = "";
	public static String URL_DESPESAS = "D:/REPOSITORY/TCC/PROJETO/dados/despesas.csv";
	public static String URL_INSTANCIAS ="D:/REPOSITORY/TCC/PROJETO/dados/YYYY_original.csv";
	public static String URL_SCORES = "D:/REPOSITORY/TCC/PROJETO/dados/input.csv";
	
	
	
	public static void main(String[] args) throws IOException {
		TreeSet<Despesa> despesas = new TreeSet<Despesa>();
		int yearBegin = Integer.valueOf( args[0]);
		int yearEnd = Integer.valueOf( args[1]);
		ArrayList<Ano> anos = new ArrayList<>();
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		 DecimalFormat df = new DecimalFormat("##0.00",symbols);
		
		FileReader in = new FileReader(URL_DESPESAS);
		BufferedReader buffer = new BufferedReader(in);
		String line;
		
		 //LENDO DESPESAS
		while((line=buffer.readLine())!=null){
			//boolean isComment = line.trim().charAt(0)== '#';
			String cols[] = line.split("-");
			cols[0] = cols[0].trim();
					
			cols[1] = cols[1].trim();
			
			Despesa aux = new Despesa(cols[0], cols[1], 0.0f);
			despesas.add(aux);
			
		}
	
		
		//lendo instancias 2013 a 2015
		for(int ano = yearBegin ; ano <= yearEnd ; ano++){
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
		//Medias e desvio padrao para populacoes e despesas de cada ano e prefeitura
		for(Ano a : anos){
			double mediaPopulacao = 0.0d;
			double desvioPadraoPopulacao = 0.0d;
			String output ="";
			ArrayList<DespesaHelper> despesasHelper = new ArrayList<DespesaHelper>();
			
			for(Despesa d : despesas)
				despesasHelper.add(new DespesaHelper(d.getCodigo(), d.getNome(), d.getValor()));
			
			
			
			for(Prefeitura p : a.getPrefeituras()){
				p.setPopulacaoSuavizada((int)Math.log10(p.getPopulacao()));
				mediaPopulacao+= p.getPopulacaoSuavizada();				
				
				for(int i = 0; i < p.getDespesas().size() ; i++){
					if(p.getDespesas().get(i).getValor() > 0 )
							p.getDespesas().get(i).setValorSuavizado(Math.log10(p.getDespesas().get(i).getValor()));
					else if(p.getDespesas().get(i).getValor() < 0 )
							p.getDespesas().get(i).setValorSuavizado(-(Math.log10(-(p.getDespesas().get(i).getValor()))));
						 else	
							 p.getDespesas().get(i).setValorSuavizado(p.getDespesas().get(i).getValor());
					
					despesasHelper.get(i).setMedia(despesasHelper.get(i).getMedia()+ p.getDespesas().get(i).getValorSuavizado());
									
				}
				
				
			}
			
			//MEDIA Despesas
			for(int i = 0; i < despesasHelper.size() ; i++)
				despesasHelper.get(i).setMedia(despesasHelper.get(i).getMedia()/ a.getPrefeituras().size());
			
			//MEDIA POPULACAO
			mediaPopulacao= mediaPopulacao / a.getPrefeituras().size(); 
			
			
			//DIFERENCA DE CADA VALOR PARA COM A MEDIA
			for(Prefeitura p : a.getPrefeituras()){
				desvioPadraoPopulacao+= Math.pow((p.getPopulacaoSuavizada() - mediaPopulacao), 2);	
				
				for(int i = 0; i < p.getDespesas().size() ; i++){
					despesasHelper.get(i).setDesvioPadrao(despesasHelper.get(i).getDesvioPadrao() + Math.pow((p.getDespesas().get(i).getValorSuavizado() - despesasHelper.get(i).getMedia()), 2));
					
				}
			}
			//DESVIO PADRAO DESPESAS
			for(int i = 0; i < despesasHelper.size() ; i++){
				despesasHelper.get(i).setDesvioPadrao(Math.sqrt(despesasHelper.get(i).getDesvioPadrao()/a.getPrefeituras().size()));
				
			}
			
			//DESVIO PADRAO POPULACAO
			desvioPadraoPopulacao = Math.sqrt(desvioPadraoPopulacao/ a.getPrefeituras().size());
			
			//APLICANDO NORMALIÇÃO
			for(Prefeitura p : a.getPrefeituras()){
				//populacao
				p.setPopulacaoNormalizada((int)((p.getPopulacaoSuavizada() - mediaPopulacao)/desvioPadraoPopulacao));
				
				//despesas
				for(int i = 0; i < p.getDespesas().size() ; i++){
					p.getDespesas().get(i).setValorNormalizado((p.getDespesas().get(i).getValorSuavizado() - despesasHelper.get(i).getMedia())/despesasHelper.get(i).getDesvioPadrao());
					
				}
				
			}			

			
		}		
				
		 //FALSE = MOSTRAR SOMENTE CATEGORIAS GENERICAS , TRUE= MOSTRA CATEGORIAS ESPECIFICAS
		 boolean minCategories = true;
		 //PRINTAR HEADER
		 boolean header = true;
				
			//printYear(anos.get(1),minCategories,header,df);
		 
		 printYeasInLine(anos, minCategories, header, df,false);
		


	}


//IMPRIME SOMENTE TODAS INSTANCIAS DE UM ANO (a) COM CATEGORIAS GENERICAS OU ESPECIFICAS (minCategory) e COM OU SEM CABEÇALHO
	private static void printYear(Ano a, boolean minCategory, boolean header, DecimalFormat df) {
		String output ="";		
		if (header){
			output+="#";
			//output+="Ano";
			output+="Cidade";
			output+=";População";
			
			for(Despesa d : a.getPrefeituras().get(0).getDespesas()){
				if(minCategory){
					if((d.getCodigo().contains(".") == false && d.getCodigo().equals("00") == false )){					
						output+=";"+d.getNome();		
					}
				}else{
					if((d.getCodigo().contains(".")  || d.getCodigo().equals("29"))){					
						output+=";"+d.getNome();		
					}
					
				}
			}
			System.out.println(output);
			
		}
		for(Prefeitura p : a.getPrefeituras()){
			output ="";
			//output+=a.getValor();
			output+=p.getCodigo();
			output+=";"+p.getPopulacaoNormalizada();
			for(Despesa d : p.getDespesas()){
				if(minCategory){
					if((d.getCodigo().contains(".") == false && d.getCodigo().equals("00") == false )){					
						output+=";"+ df.format( d.getValorNormalizado());		
					}
				}else{
					if((d.getCodigo().contains(".") || d.getCodigo().equals("29"))){					
						output+=";"+ df.format(d.getValorNormalizado());		
					}
					
				}			
			}
			System.out.println(output);
		}
		
	}
	
	private static void printYeasInLine(ArrayList<Ano> anos, boolean minCategory, boolean header, DecimalFormat df, boolean absoluteValues, boolean printScores){
		
		//REMOVER PREFEITURAS QUE NAO SE REPETEM TODOS ANOS
		TreeSet<Prefeitura> removeList = new TreeSet<>();
		for(Ano a : anos){
			 for(Prefeitura p : a.getPrefeituras()){
				
				 for(Ano a2 : anos){
					 if(!a2.equals(a)){
						 if(a2.getPrefeituras().indexOf(p)< 0 ){
							 removeList.add(p);
							 break;
						 }
						 
					 }
				 }
				 
					
				 
			 }
			
		}
		for(Prefeitura p : removeList){
			for(Ano a : anos){
				a.getPrefeituras().remove(p);				
			}
			
		}
		
		System.out.println("#Total instancias: "+anos.get(0).getPrefeituras().size());
		String output ="";
		if(header){
			output ="#";
			output +="Codigo";
			
			for(Ano a : anos){
				output +=";População "+a.getValor();
				for(Despesa d : a.getPrefeituras().get(0).getDespesas())
					if(!d.getCodigo().equals("00"))
						if((minCategory && !d.getCodigo().contains(".")) || (!minCategory && d.getCodigo().contains("."))||(d.getCodigo().equals("29") ) )
							output +=";"+d.getNome()+"("+a.getValor()+")";				
				
			}
			
			System.out.println(output);
			
		}	
		
		for(Prefeitura p : anos.get(0).getPrefeituras()){
			output ="";
			output +=p.getCodigo();
			
			for(Ano a : anos){
				int indexPrefeitura = a.getPrefeituras().indexOf(p);
				if(indexPrefeitura >= 0){
					Prefeitura instancia = a.getPrefeituras().get(indexPrefeitura);
					output += absoluteValues ? ";"+instancia.getPopulacao() :";"+instancia.getPopulacaoNormalizada() ;
					for(Despesa d : instancia.getDespesas())
						if(!d.getCodigo().equals("00"))
							if((minCategory && !d.getCodigo().contains(".")) || (!minCategory && d.getCodigo().contains("."))||(d.getCodigo().equals("29") ) )
								output +=absoluteValues ?  ";"+df.format( d.getValor()) : ";"+df.format( d.getValorNormalizado());				
				}	else{
					System.out.println("#INDEX_NOT_FOUND prefeitura codigo:"+p.getCodigo()+" para ano: "+a.getValor());
					
				}
			}
			System.out.println(output);
		}
		
		
	}

}
