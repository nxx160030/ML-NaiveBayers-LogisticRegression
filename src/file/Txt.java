package file;

import java.util.HashMap;
import java.util.List;

public class Txt{
	
	List<String> voc_list;
	HashMap<String, Double> condprob_map;
	double prior_prob;

	public Txt(List<String> voc, double prior, HashMap<String, Double> map) {

		voc_list = voc;
		condprob_map = map;
		prior_prob = prior;		
	}
	
	public double getPrior()
	{
		return prior_prob;
	}
	
	public List<String> getVocList()
	{
		return voc_list;
	}
	
	public HashMap<String, Double> getCondprobMap()
	{
		return condprob_map;
	}


}
