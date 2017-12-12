package algorithm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import file.Directory;
import file.Txt;
import util.Util;

public class Bayes {
	
	public HashMap<String,Txt> trainMultiNomialNB(Directory dir, boolean stopwords)
	{
		List<String> voc = dir.getVocList(stopwords);
		
		int t_cnt = dir.getTotalCnt();		
		List<String> types = dir.getTypes();
		HashMap<String,Integer> type_cnt = dir.getTypeCnt();
		HashMap<String,Integer> term_cnt_map = new HashMap<String,Integer>();	
		HashMap<String,Txt> txt_map = new HashMap<String,Txt>();

		ListIterator<String> types_itr = types.listIterator();
		
		while(types_itr.hasNext())
		{
			int term_cnt = 0;
			String type = types_itr.next();
			int cnt = type_cnt.get(type);
			double prior = (double)cnt/t_cnt;
			HashMap<String,Double> condprob_map = new HashMap<String,Double>();	
			
			String text = dir.conTxtofAllDocsInType(type);
			
			ListIterator<String> voc_itr = voc.listIterator();
			while(voc_itr.hasNext())
			{
				String term = (String) voc_itr.next();				
				int count = Util.countTokensOfTerm(text,term);
				term_cnt_map.put(term, count+1);
				term_cnt += count+1;
			}
			
			voc_itr = voc.listIterator();
			while(voc_itr.hasNext())
			{
				String term = (String) voc_itr.next();
				condprob_map.put(term, (double)term_cnt_map.get(term)/term_cnt);
			}
			
			Txt txt = new Txt(voc,prior,condprob_map);
			txt_map.put(type, txt);
		}				
		return txt_map;
	}
	
	
	public String applyMultiNomialNB(HashMap<String,Txt> txt_map,String file)
	{
		
		HashMap<String, Integer> term_cnt_map;		

		Iterator<Entry<String, Txt>> itr = txt_map.entrySet().iterator();
		
		String type = "";
		double max = Double.NEGATIVE_INFINITY;
		
		while(itr.hasNext()){
			
			Map.Entry<String, Txt> pair = itr.next();
			Txt txt = pair.getValue();
			String current_type = pair.getKey();
			List<String> voc_list = txt.getVocList();
			HashMap<String, Double> condprob_map = txt.getCondprobMap();
			double prior = txt.getPrior();
			term_cnt_map = Util.extractTokensFromDoc(voc_list,file);
			
			double base = Math.log(prior)/Math.log(2);
			double current = base;
			
			Iterator<Entry<String, Integer>> term_itr = term_cnt_map.entrySet().iterator();
						
			while(term_itr.hasNext())
			{
				Entry<String, Integer> entry = term_itr.next();
				String term = entry.getKey();
				int count = entry.getValue();
				double condprob = condprob_map.get(term);
				
				current += count*Math.log(condprob)/Math.log(2);
			}
			
			if(current>max)
			{
				max = current;
				type = current_type;
			}		
		}
		
		return type;		
	}

}
