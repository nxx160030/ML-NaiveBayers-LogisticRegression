package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import util.Util;

public class Directory {
	private File dir;
	List<File> files = new ArrayList<File>();
	private int docCnt;
	private List<String> types = new ArrayList<String>();
	private HashMap<String,Integer> type_cnt = new HashMap<String,Integer>();
	List<String> vocList = new ArrayList<String>();
	boolean STOPWORDS;
	
	public Directory(String directory)
	{
		dir = new File(directory);
		listFile();			
	}
	
	private void listFile()
	{
		File[] dir_files = dir.listFiles();
		for(File file:dir_files)
		{
			if(file.isDirectory())
				recursiveGetFile(file.getPath());
			
			files.add(file);
				
		}
	}
	
	private void recursiveGetFile(String path)
	{
		File root = new File(path);
		File[] root_files = root.listFiles();
		
		for(File file:root_files)
		{
			if(file.isDirectory())
			{
				recursiveGetFile(file.getPath());
			}

			if(!files.contains(file))
				files.add(file);
		}
	}
	
	private List<String> extractVocabulary()
	{	

		List<String> stop_list = Util.readStopWords();
		
		for(File file:files)
		{
			if(!file.isDirectory())
			{
				String fileName = file.getPath();
				try {
					FileReader reader = new FileReader(fileName);
					BufferedReader bufferReader = new BufferedReader(reader);
					
					String line;
					while((line = bufferReader.readLine())!=null)
					{
						String[] str = line.split("\\s+");
						for(int i=0;i<str.length;i++)
						{
							if(str[i].matches("[a-zA-Z]+")&&(!vocList.contains(str[i])))
							{
								if(STOPWORDS&&stop_list.contains(str[i].toLowerCase()))
									continue;
								vocList.add(str[i]);
							}
						}
					}
					
					bufferReader.close();
					
				} catch (FileNotFoundException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}
				
		return vocList;		
	}
	
	private int countDocs()
	{
		docCnt = 0;
		
		for(File file:files)
		{
			if(!file.isDirectory())
				docCnt++;
		}
		
		return docCnt;
	}
	
	private int countDocsInClass(String type)
	{
		int typeCnt = 0;
		String type_dir = dir.getPath() + "/" + type;
		File type_files = new File(type_dir);
		
		File[] type_list = type_files.listFiles();
		
		for(File file:type_list)
		{
			if(!file.isDirectory())
				typeCnt++;
		}
		
		return typeCnt;
	}
	
	public List<String> getTypes()
	{
		if(!types.isEmpty())
		{
			return types;
		}
		else
		{
			for(File file:files)
			{
				if(file.isDirectory())
					types.add(file.getName());
			}
		}
		
		return types;
	}
	
	public int getTotalCnt()
	{
		if(docCnt!=0) 
			return docCnt;
		else
		{
			countDocs();
			return docCnt;
		}
	}
	
	public List<String> getVocList(boolean stopwords)
	{
		STOPWORDS = stopwords;
		
		if(!vocList.isEmpty())
			return vocList;
		else
		{
			extractVocabulary();		
			return vocList;
		}
	}
	
	
	public String conTxtofAllDocsInType(String type)
	{
		String type_dir = dir.getPath() + "/" + type;
		File[] files = new File(type_dir).listFiles();
		
		StringBuffer strBuffer = new StringBuffer("");
		
		for(File file:files)
		{
			if(!file.isDirectory())
			{
				String fileName = file.getPath();
				try {
					FileReader reader = new FileReader(fileName);
					BufferedReader bufferReader = new BufferedReader(reader);
					
					String line;
					while((line = bufferReader.readLine())!=null)
					{
						String[] str = line.split("\\s+");
						for(int i=0;i<str.length;i++)
							strBuffer.append(str[i]);
					}
					
					bufferReader.close();
					
				} catch (FileNotFoundException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}		
		
		String str = strBuffer.toString();
		
		return str;
	}
	
	public HashMap<String,Integer> getTypeCnt()
	{
		if(!type_cnt.isEmpty())
		{
			return type_cnt;
		}
		else
		{
			ListIterator<String> type_itr = types.listIterator();
			while(type_itr.hasNext())
			{
				String type = type_itr.next();

				int type_count = countDocsInClass(type);
				type_cnt.put(type, type_count);
			}
			
			return type_cnt;
		}
	}
	
	public String getPath()
	{
		return dir.getPath();
	}
	
	public List<File> getFiles()
	{
		return files;
	}

}
