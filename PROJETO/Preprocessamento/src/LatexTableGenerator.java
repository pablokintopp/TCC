import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LatexTableGenerator {

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String line, output;
		boolean isHeader = true;
		while((line=in.readLine())!=null) {
			String[] cols = line.split(";");
			output = "";
			if(isHeader){
				System.out.println("\\begin{table}[htb]"); 
				System.out.println("\\centering");
				System.out.println("\\caption{CHANGE-ME}");
				System.out.println("\\label{tabCHANGE-ME}");
				String columns = "{";
				
				for(int i = 0; i < cols.length; i++){
					columns += i > 0 ? "|c" : "c";
					output+= i > 0 ? "\t & { \\scriptsize \\textbf{ "+cols[i].trim()+"}}" : "{ \\scriptsize \\textbf{ "+cols[i].trim()+"}}";
				}
					
				columns+="}";
				output+="\\\\ \\hline";
				System.out.println("\\begin{tabular}"+columns);
				System.out.println("\\hline");
				
					
				System.out.println(output);
					
				isHeader = false;
			}else{
				for(int i = 0; i < cols.length; i++){					
					output+= i > 0 ? "\t & { \\scriptsize "+cols[i].trim()+"}" : "{ \\scriptsize "+cols[i].trim()+"}";
				}
				output+=" \\\\ \\hline";
				System.out.println(output);
				
			}		
			
			
		}
		System.out.println("\\end{tabular}");
		System.out.println("\\end{table}");
		
		

	}

}
