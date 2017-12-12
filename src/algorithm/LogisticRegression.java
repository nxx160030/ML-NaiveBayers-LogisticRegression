package algorithm;


import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import file.Directory;
import util.Util;

public class LogisticRegression {
	
	private double eta = 0.01;
	private double LAMBDA;
	private double[] weights;
	List<String> voc;
	List<String> types;
	
	public LogisticRegression(double lambda)
	{
		LAMBDA = lambda;		
	}
	
	public void train(Directory dir, boolean stopwords)
	{
		voc = dir.getVocList(stopwords);	
		
		types = dir.getTypes();	

		ListIterator<String> types_itr = types.listIterator();
		weights = new double[voc.size()+1];
		Arrays.fill(weights, 0);
		
		while(types_itr.hasNext())
		{
			String type = types_itr.next();
			// go through sub directory
			Directory type_dir = new Directory(dir.getPath() + "/" + type);			
			List<File> type_files = type_dir.getFiles();
			Iterator<File> file_itr = type_files.listIterator();
			
			while(file_itr.hasNext())
			{
				File file = file_itr.next();
				if(file.isDirectory()) continue;
				
				HashMap<String,Integer> cnt_map = new HashMap<String,Integer>();
				
				String text = Util.conTxt(file.getPath());
				
				ListIterator<String> voc_itr = voc.listIterator();
				while(voc_itr.hasNext())
				{
					String term = (String) voc_itr.next();				
					int count = Util.countTokensOfTerm(text,term);
					cnt_map.put(term, count);
				}
				
				int ITERATION = 200;
                double prob = 0;

            	// "spam" is 0; "ham" is 1
                int delta;
                if(type.equals("ham"))
                {
                	delta = 1;
                }
                else
                {
                 	delta = 0;
                }
                
				try {
					prob = prob(cnt_map);					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// iterate weights			
		        do {
		        		weights[0] = weights[0] + (eta * 1 * ((double) delta - prob))
	                		  - (eta * LAMBDA * weights[0]);
	            	  	int j=1;
		                //Estimate weights	
		        		voc_itr = voc.listIterator();
		        		while(voc_itr.hasNext())
		        		{
		        			  String term = voc_itr.next();
			                  weights[j] = weights[j] + (eta * (double) cnt_map.get(term) * ((double) delta - prob))
			                		  - (eta * LAMBDA * weights[j]);
			                  j++;
		        		}
		        } while (ITERATION-- > 0);
			}		
		}
		
	}
	
	public String apply(String file, String type, String otherType)
	{
		
		HashMap<String, Integer> term_cnt_map;		

		term_cnt_map = Util.extractTokensFromDoc(voc,file);
		double prob = 0;

		try {
			prob = prob(term_cnt_map);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return (prob>0)?otherType:type;		
	}
	
    private double prob(HashMap<String, Integer> file) throws Exception {
    	
        double parameterValue = weights[0];

        int i = 1;
        ListIterator<String> voc_itr = voc.listIterator();
        while(voc_itr.hasNext())
        {
        	String term = voc_itr.next();
            parameterValue += (weights[i] * (double) file.get(term));
            i++;
        }
        

        double expVal = Math.exp(-parameterValue);
 
        return  1 / ((double) 1 + expVal);

    }

}
