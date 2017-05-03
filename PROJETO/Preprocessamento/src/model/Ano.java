package model;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;



public class Ano {
	public static int VALOR_ABSOLUTO = 1;
	public static int VALOR_SUAVIZADO = 2;
	public static int VALOR_RELATIVO = 3;
	public static int VALOR_SUAVIZADO_NORMALIZADO = 4;
	public static int VALOR_RELATIVO_NORMALIZADO = 5;
	
	private int valor;
	private int totalPrefeituras;
	private List<Prefeitura> prefeituras;	
	
	
	
	public Ano(int valor) {		
		this.valor = valor;
		this.prefeituras = new ArrayList<Prefeitura>();
		
	}
	public int getValor() {
		return valor;
	}
	public void setValor(int valor) {
		this.valor = valor;
	}
	public int getTotalPrefeituras() {
		return totalPrefeituras;
	}
	public void setTotalPrefeituras(int totalPrefeituras) {
		this.totalPrefeituras = totalPrefeituras;
	}
	public List<Prefeitura> getPrefeituras() {
		return prefeituras;
	}
	public void setPrefeituras(List<Prefeitura> prefeituras) {
		this.prefeituras = prefeituras;
		this.totalPrefeituras = this.prefeituras.size();
	}
	
	@Override
	public boolean equals(Object o) {
		
		if(!(o instanceof Ano)) return false;
		
		Ano other = (Ano) o;
	    
	    return (this.valor == other.valor);
	}

	//IMPRIME SOMENTE TODAS INSTANCIAS DE UM ANO (value é entre valor absoluto, relativo , suavizado e normalizado) (a) COM CATEGORIAS GENERICAS OU ESPECIFICAS (minCategory) e COM OU SEM CABEÇALHO
		public void print(int value,boolean printTotalExpense,boolean useCode,boolean minCategory, boolean header, DecimalFormat df, boolean printScores,String outputFile) throws IOException {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile.contains(".csv")? outputFile:outputFile+".csv"), "utf-8"));
			String output ="";		
			if (header){
				output+="#";
				//output+="Ano";
				output+="Codigo";
				if(!useCode)
					output+=";Cidade";
				if(value != VALOR_RELATIVO && value != VALOR_RELATIVO_NORMALIZADO)
					output+=";População";
				
				
				
				for(Despesa d : this.getPrefeituras().get(0).getDespesas()){
					if(minCategory){
						if((d.getCodigo().contains(".") == false && (d.getCodigo().equals("00") == false || (d.getCodigo().equals("00") && printTotalExpense)) )){							
							output+=";"+d.getNome();		
						}
					}else{
						if((d.getCodigo().contains(".")  || d.getCodigo().equals("29"))){					
							output+=";"+d.getNome();		
						}
						
					}
				}
				
				if(printScores){
					for(Score s : this.getPrefeituras().get(0).getScores()){
						output+=";"+s.getAlgoritmo().toUpperCase()+"_"+s.getDetalhe();
						
					}
					
				}
				writer.write(output);
				writer.newLine();
				
			}
			
			for(Prefeitura p : this.getPrefeituras()){
				output ="";
				output+=p.getCodigo();
				
				if(!useCode)
					output+=";"+ p.getNome()+" - "+p.getUf();
				if(value != VALOR_RELATIVO && value != VALOR_RELATIVO_NORMALIZADO){
					output+=";"+ (value == VALOR_SUAVIZADO_NORMALIZADO ?  p.getPopulacaoNormalizada():
						value == VALOR_SUAVIZADO ? p.getPopulacaoSuavizada() :
							p.getPopulacao()); //absoluto
				}
				for(Despesa d : p.getDespesas()){
					double valor = value == VALOR_SUAVIZADO_NORMALIZADO ? d.getValorSuavizadoNormalizado() :
							value == VALOR_SUAVIZADO ? d.getValorSuavizado():
							value == VALOR_RELATIVO ? d.getValorRelativo() : 
							value == VALOR_RELATIVO_NORMALIZADO ? d.getValorRelativoNormalizado() :
							d.getValor();//senao absoluto
					if(minCategory){
						if((d.getCodigo().contains(".") == false && (d.getCodigo().equals("00") == false || (d.getCodigo().equals("00") && printTotalExpense)) )){					
							output+=";"+ df.format( valor);		
						}
					}else{
						if((d.getCodigo().contains(".") || d.getCodigo().equals("29"))){					
							output+=";"+ df.format(valor);		
						}
						
					}			
				}	
				
				if(printScores){
					for(Score s : p.getScores()){
						output+=";"+df.format(s.getValor());
						
					}
					
				}
			   
			   writer.write(output);
			   writer.newLine();
			
				
			}
			writer.close();
			
		}
	
		//Medias e desvio padrao para populacoes e despesas
		public void calcMeanAndDeviation(TreeSet<Despesa> despesas){
			double mediaPopulacao = 0.0d;
			double desvioPadraoPopulacao = 0.0d;
			String output ="";
			ArrayList<DespesaHelper> despesasHelper = new ArrayList<DespesaHelper>();
			
			for(Despesa d : despesas)
				despesasHelper.add(new DespesaHelper(d.getCodigo(), d.getNome(), d.getValor()));
			
			
			
			for(Prefeitura p : this.getPrefeituras()){
				p.setPopulacaoSuavizada(Math.log10(p.getPopulacao()));
				mediaPopulacao+= p.getPopulacaoSuavizada();				
				
				//NOSSA FORMULA DEFINIDA DE NORMALIZACAO, VALOR ENTRE 1 E -1 ENTAO ZERAR, SENAO SUAVIZAR NORMALMENTE (MANTENDO NEGATIVO CASO  SEJA)
				for(int i = 0; i < p.getDespesas().size() ; i++){
					double valor = 0;
					if(p.getDespesas().get(i).getValor() > 1 ){
						 valor = Math.log10(p.getDespesas().get(i).getValor());							
					}else if(p.getDespesas().get(i).getValor() < -1 ){
						valor = Math.log10(-(p.getDespesas().get(i).getValor()));
						valor *= -1;
					} 	
							 p.getDespesas().get(i).setValorSuavizado(valor);
					
					//SET VALOR RELATIVO
					p.getDespesas().get(i).setValorRelativo(p.getDespesas().get(i).getValor() / p.getPopulacao());
					
					//AUXILIAR SET DESPESA MEDIA
					despesasHelper.get(i).setMedia(despesasHelper.get(i).getMedia()+ p.getDespesas().get(i).getValorSuavizado());
					
					//AUXILIAR SET DESPESA RELATIVA MEDIA
					despesasHelper.get(i).setMediaRelativo(despesasHelper.get(i).getMediaRelativo()+ p.getDespesas().get(i).getValorRelativo());
									
				}
				
				
				
				
				
			}
			
			//MEDIA Despesas (relativa e suavizada)
			for(int i = 0; i < despesasHelper.size() ; i++){
				despesasHelper.get(i).setMedia(despesasHelper.get(i).getMedia()/ this.getPrefeituras().size());
				despesasHelper.get(i).setMediaRelativo(despesasHelper.get(i).getMediaRelativo()/ this.getPrefeituras().size());
			}
			//MEDIA POPULACAO
			mediaPopulacao= mediaPopulacao / this.getPrefeituras().size(); 
			
			
			//DIFERENCA DE CADA VALOR PARA COM A MEDIA
			for(Prefeitura p : this.getPrefeituras()){
				desvioPadraoPopulacao+= Math.pow((p.getPopulacaoSuavizada() - mediaPopulacao), 2);	
				
				for(int i = 0; i < p.getDespesas().size() ; i++){
					//SUAVIZADO
					despesasHelper.get(i).setDesvioPadrao(despesasHelper.get(i).getDesvioPadrao() + 
							Math.pow((p.getDespesas().get(i).getValorSuavizado() - 
									despesasHelper.get(i).getMedia()), 2));
					//RELATIVO
					despesasHelper.get(i).setDesvioPadraoRelativo(despesasHelper.get(i).getDesvioPadraoRelativo() + 
							Math.pow((p.getDespesas().get(i).getValorRelativo() - 
									despesasHelper.get(i).getMediaRelativo()), 2));
					
				}
			}
			//DESVIO PADRAO DESPESAS
			for(int i = 0; i < despesasHelper.size() ; i++){
				//SUAVIZADO
				despesasHelper.get(i).setDesvioPadrao(Math.sqrt(despesasHelper.get(i).getDesvioPadrao()/this.getPrefeituras().size()));
				
				
				//RELATIVO
				despesasHelper.get(i).setDesvioPadraoRelativo(Math.sqrt(despesasHelper.get(i).getDesvioPadraoRelativo()/this.getPrefeituras().size()));
			}
			
			//DESVIO PADRAO POPULACAO
			desvioPadraoPopulacao = Math.sqrt(desvioPadraoPopulacao/ this.getPrefeituras().size());
			
			//APLICANDO NORMALIÇÃO
			for(Prefeitura p : this.getPrefeituras()){
				//populacao
				p.setPopulacaoNormalizada(((p.getPopulacaoSuavizada() - mediaPopulacao)/desvioPadraoPopulacao));
				
				//despesas
				for(int i = 0; i < p.getDespesas().size() ; i++){
					
					//SUAVIZADO
					p.getDespesas().get(i).setValorSuavizadoNormalizado((p.getDespesas().get(i).getValorSuavizado() - 
							despesasHelper.get(i).getMedia())/despesasHelper.get(i).getDesvioPadrao());
					
					//RELATIVO
					p.getDespesas().get(i).setValorRelativoNormalizado((p.getDespesas().get(i).getValorRelativo() - 
							despesasHelper.get(i).getMediaRelativo())/despesasHelper.get(i).getDesvioPadraoRelativo());
					
				}
				
			}		
			
		}
}
