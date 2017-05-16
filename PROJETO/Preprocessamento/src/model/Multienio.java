package model;

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
	public void print(boolean minCategory, boolean header, DecimalFormat df, int valueTipe, boolean printScores){
		if(!ajusted)
			ajustaPrefeituras();
		
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
			
			if(printScores){
				output += scores.firstEntry().getValue().getAlgoritmo();
				
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
					
					output += (valueTipe==Ano.VALOR_RELATIVO || valueTipe== Ano.VALOR_RELATIVO_NORMALIZADO || valueTipe == Ano.VALOR_ABSOLUTO ) ?
							";"+instancia.getPopulacao() : valueTipe == Ano.VALOR_SUAVIZADO ?  ";"+instancia.getPopulacaoSuavizada(): ";"+instancia.getPopulacaoNormalizada();
					for(Despesa d : instancia.getDespesas())
						if(!d.getCodigo().equals("00"))
							if((minCategory && !d.getCodigo().contains(".")) || (!minCategory && d.getCodigo().contains("."))||(d.getCodigo().equals("29") ) ){
								double valor = valueTipe==Ano.VALOR_RELATIVO_NORMALIZADO ? d.getValorRelativoNormalizado() : d.getValorSuavizadoNormalizado();
								output += (df.format(valor));
							}
				}	else{
					System.out.println("#INDEX_NOT_FOUND prefeitura codigo:"+p.getCodigo()+" para ano: "+a.getValor());
					
				}
			}
			if(printScores){
				output += df.format(scores.get(p).getValor());				
			}
			System.out.println(output);
		}
		
		
	}
	public TreeMap<Prefeitura, Score> getScores() {
		return scores;
	}
	public void setScores(TreeMap<Prefeitura, Score> scores) {
		this.scores = scores;
	}
	
	
	
	
}
