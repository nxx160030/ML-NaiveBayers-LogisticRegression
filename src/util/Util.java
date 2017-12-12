package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class Util {
	
	public static int countTokensOfTerm(String text,String term)
	{
		int count = 0;
		int idx = 0;
		
		while ((idx = text.indexOf(term, idx)) != -1)
	     {
	        idx++;
	        count++;
	     }
		
		return count;
	}
    
    
	public static HashMap<String, Integer> extractTokensFromDoc(List<String> voc_list, String file)
	{
		HashMap<String, Integer> term_cnt_map = new HashMap<String, Integer>();
		ListIterator<String> itr = voc_list.listIterator();
		
		String text = conTxt(file);
				
		while(itr.hasNext())
		{
			String term = itr.next();
			int count = countTokensOfTerm(text,term);			

			term_cnt_map.put(term, count);
		}
		
		return term_cnt_map;
	}
	
	public static  String conTxt(String filePath)
	{
		FileReader reader;
		StringBuffer strBuffer = new StringBuffer("");
		
		try {
			reader = new FileReader(filePath);

			BufferedReader bufferReader = new BufferedReader(reader);
	
			String line;
			while((line = bufferReader.readLine())!=null)
			{
				String[] str = line.split("\\s+");
				for(int i=0;i<str.length;i++)
					strBuffer.append(str[i]);
			}		
		
			bufferReader.close();
		
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		String str = strBuffer.toString();
		
		return str;
	}
	
	public static List<String> readStopWords()
	{
		String 	STOP_WORDS = "./stopwords.txt";
		List<String> stopwords_list = new ArrayList<String>();
		FileReader reader;
		
		try {
			reader = new FileReader(STOP_WORDS);

			BufferedReader bufferReader = new BufferedReader(reader);
	
			String line;
			while((line = bufferReader.readLine())!=null)
			{
				String[] str = line.split("\\s+");
				for(int i=0;i<str.length;i++)
					stopwords_list.add(str[i].toLowerCase());
			}		
		
			bufferReader.close();
		
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return stopwords_list;
		
	}

}
