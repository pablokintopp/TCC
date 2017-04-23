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

	//IMPRIME SOMENTE TODAS INSTANCIAS DE UM ANO (a) COM CATEGORIAS GENERICAS OU ESPECIFICAS (minCategory) e COM OU SEM CABEÇALHO
		public void print(boolean minCategory, boolean header, DecimalFormat df, boolean printScores,String outputFile) throws IOException {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile.contains(".csv")? outputFile:outputFile+".csv"), "utf-8"));
			String output ="";		
			if (header){
				output+="#";
				//output+="Ano";
				output+="Cidade";
				output+=";População";
				
				for(Despesa d : this.getPrefeituras().get(0).getDespesas()){
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
				writer.write(output);
				writer.newLine();
				
			}
			
			for(Prefeitura p : this.getPrefeituras()){
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
				p.setPopulacaoSuavizada((int)Math.log10(p.getPopulacao()));
				mediaPopulacao+= p.getPopulacaoSuavizada();				
				
				//NOSSA FORMULA DEFINIDA DE NORMALIZACAO, VALOR ENTRE 1 E -1 ENTAO ZERAR, SENAO SUAVIZAR NORMALMENTE (MANTENDO NEGATIVO CASO  SEJA)
				for(int i = 0; i < p.getDespesas().size() ; i++){
					if(p.getDespesas().get(i).getValor() > 1 )
							p.getDespesas().get(i).setValorSuavizado(Math.log10(p.getDespesas().get(i).getValor()));
					else if(p.getDespesas().get(i).getValor() < 1 )
							p.getDespesas().get(i).setValorSuavizado(-(Math.log10(-(p.getDespesas().get(i).getValor()))));
						 else	
							 p.getDespesas().get(i).setValorSuavizado(0);
					
					despesasHelper.get(i).setMedia(despesasHelper.get(i).getMedia()+ p.getDespesas().get(i).getValorSuavizado());
									
				}
				
				
			}
			
			//MEDIA Despesas
			for(int i = 0; i < despesasHelper.size() ; i++)
				despesasHelper.get(i).setMedia(despesasHelper.get(i).getMedia()/ this.getPrefeituras().size());
			
			//MEDIA POPULACAO
			mediaPopulacao= mediaPopulacao / this.getPrefeituras().size(); 
			
			
			//DIFERENCA DE CADA VALOR PARA COM A MEDIA
			for(Prefeitura p : this.getPrefeituras()){
				desvioPadraoPopulacao+= Math.pow((p.getPopulacaoSuavizada() - mediaPopulacao), 2);	
				
				for(int i = 0; i < p.getDespesas().size() ; i++){
					despesasHelper.get(i).setDesvioPadrao(despesasHelper.get(i).getDesvioPadrao() + Math.pow((p.getDespesas().get(i).getValorSuavizado() - despesasHelper.get(i).getMedia()), 2));
					
				}
			}
			//DESVIO PADRAO DESPESAS
			for(int i = 0; i < despesasHelper.size() ; i++){
				despesasHelper.get(i).setDesvioPadrao(Math.sqrt(despesasHelper.get(i).getDesvioPadrao()/this.getPrefeituras().size()));
				
			}
			
			//DESVIO PADRAO POPULACAO
			desvioPadraoPopulacao = Math.sqrt(desvioPadraoPopulacao/ this.getPrefeituras().size());
			
			//APLICANDO NORMALIÇÃO
			for(Prefeitura p : this.getPrefeituras()){
				//populacao
				p.setPopulacaoNormalizada((int)((p.getPopulacaoSuavizada() - mediaPopulacao)/desvioPadraoPopulacao));
				
				//despesas
				for(int i = 0; i < p.getDespesas().size() ; i++){
					p.getDespesas().get(i).setValorNormalizado((p.getDespesas().get(i).getValorSuavizado() - despesasHelper.get(i).getMedia())/despesasHelper.get(i).getDesvioPadrao());
					
				}
				
			}		
			
		}
}
