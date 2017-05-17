package model;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class Multienio {

	private String nome;
	private List<Ano> anos;
	private TreeMap<Prefeitura,Score> scores;
	private boolean ajusted;
	
	
	public Multienio() {
		this.nome = "";
		this.anos = new ArrayList<Ano>();
		this.scores = new TreeMap<Prefeitura, Score>();
		this.ajusted = false;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<Ano> getAnos() {
		return anos;
	}
	public void setAnos(List<Ano> anos) {
		this.anos = anos;
		
	}
	public void addANo(Ano ano){
		anos.add(ano);	
				
		anos.sort(new Comparator<Ano>() {

			@Override
			public int compare(Ano o1, Ano o2) {
				// TODO Auto-generated method stub
				return o1.getValor() < o2.getValor() ? -1 : 1 ;
			}
		});
		updateName();
		
	}	
	
	
	public void updateName(){
		this.nome = "";
		for(int i = 0; i< anos.size() ; i++)
			nome+= i > 0 ? "_"+anos.get(i).getValor() :anos.get(i).getValor(); 
		
	}
	
	//Remove prefeituras que nao se repetem a todos anos
	public void ajustaPrefeituras(){
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
				this.ajusted =true;
		
	}
	//IMPRIME ESTE MULTIENIO
	public void print(boolean minCategory, boolean header,boolean printName, DecimalFormat df, int valueTipe, boolean printScores,String outputFile) throws IOException{
		if(!ajusted)
			ajustaPrefeituras();
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile.contains(".csv")? outputFile:outputFile+".csv"), "utf-8"));
		 
				
		System.out.println( "#Total instancias: "+anos.get(0).getPrefeituras().size());
		String output = "";
		if(header){
			output ="#";
			output +="Codigo";
			if(printName){			
				output +=";Cidade ";				
			}
			boolean showedPopulation = false;
			for(Ano a : anos){
				if(showedPopulation==false && (valueTipe !=Ano.VALOR_RELATIVO && valueTipe != Ano.VALOR_RELATIVO_NORMALIZADO)){
					output +=";População "+a.getValor();
					showedPopulation =true;
				}
				for(Despesa d : a.getPrefeituras().get(0).getDespesas())
					if(!d.getCodigo().equals("00"))
						if((minCategory && !d.getCodigo().contains(".")) || (!minCategory && d.getCodigo().contains("."))||(d.getCodigo().equals("29") ) )
							output +=";"+d.getNome()+"("+a.getValor()+")";				
				
			}
			
			if(printScores){
				for(Score s : this.getAnos().get(0).getPrefeituras().get(0).getScores()){
					output+=";"+s.getAlgoritmo().toUpperCase()+"_"+s.getDetalhe();
					
				}
				
			}
			
			writer.write(output);
			writer.newLine();
			
		}	
		
		for(Prefeitura p : anos.get(0).getPrefeituras()){
			output ="";
			output +=p.getCodigo();
			if(printName){			
				output +=";"+p.getNome()+" - "+p.getUf();				
			}
			boolean showedPopulation = false;
			for(Ano a : anos){
				int indexPrefeitura = a.getPrefeituras().indexOf(p);
				if(indexPrefeitura >= 0){
					Prefeitura instancia = a.getPrefeituras().get(indexPrefeitura);
					if(showedPopulation==false && (valueTipe !=Ano.VALOR_RELATIVO && valueTipe != Ano.VALOR_RELATIVO_NORMALIZADO)){
						double valorPop = 0; 
						if(valueTipe==Ano.VALOR_RELATIVO_NORMALIZADO )
							valorPop =  instancia.getPopulacao(); 
						if(valueTipe== Ano.VALOR_SUAVIZADO_NORMALIZADO)	
							valorPop = instancia.getPopulacaoNormalizada();
						if(valueTipe==Ano.VALOR_ABSOLUTO )
							valorPop = instancia.getPopulacao();
						if(valueTipe== Ano.VALOR_RELATIVO)	
							valorPop =instancia.getPopulacao();
						if(valueTipe== Ano.VALOR_SUAVIZADO)	
							valorPop = instancia.getPopulacaoSuavizada();
						output += ";"+valorPop;
							showedPopulation =true;
					}
					for(Despesa d : instancia.getDespesas())
						if(!d.getCodigo().equals("00"))
							if((minCategory && !d.getCodigo().contains(".")) || (!minCategory && d.getCodigo().contains("."))||(d.getCodigo().equals("29") ) ){
								double valor = 0; 
								if(valueTipe==Ano.VALOR_RELATIVO_NORMALIZADO )
									valor =  d.getValorRelativoNormalizado(); 
								if(valueTipe== Ano.VALOR_SUAVIZADO_NORMALIZADO)	
										valor = d.getValorSuavizadoNormalizado();
								if(valueTipe==Ano.VALOR_ABSOLUTO )
									valor =  d.getValor(); 
								if(valueTipe== Ano.VALOR_RELATIVO)	
										valor = d.getValorRelativo();
								if(valueTipe== Ano.VALOR_SUAVIZADO)	
									valor = d.getValorSuavizado();
								output += ";"+(df.format(valor));
							}
				}	else{
					System.out.println("#INDEX_NOT_FOUND prefeitura codigo:"+p.getCodigo()+" para ano: "+a.getValor());
					
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
		
		
	}
	public TreeMap<Prefeitura, Score> getScores() {
		return scores;
	}
	public void setScores(TreeMap<Prefeitura, Score> scores) {
		this.scores = scores;
	}
	
	
	
	
}
