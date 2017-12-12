package entry;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import algorithm.Bayes;
import algorithm.LogisticRegression;
import file.Directory;
import file.Txt;

public class MLAssign2 {

	private static String TRAIN_DIR = "./train";
	private static String TEST_HAM_DIR = "./test/ham";
	private static String TEST_SPAM_DIR = "./test/spam";
	private static boolean STOPWORDS = true;


	public static void main(String[] args) {
		
		System.out.println("********* Using Naive Bayes to train and test *********");
		bayes(STOPWORDS);
		bayes(!STOPWORDS);
		System.out.println();
		
		System.out.println("********* Using Logistic Regression to train and test *********");		
		
		logisticReg(0.001,STOPWORDS);	
		logisticReg(0.001,!STOPWORDS);
		System.out.println();
		
		logisticReg(0.035,STOPWORDS);
		logisticReg(0.035,!STOPWORDS);
		System.out.println();
		
		logisticReg(0.075,STOPWORDS);
		logisticReg(0.075,!STOPWORDS);
		System.out.println();

	}
	
	private static void bayes(boolean stopwords)
	{	
		if(stopwords)
			System.out.println("----------- Without Stopwords -----------");
		else
			System.out.println("----------- With Stopwords -----------");
		Bayes bayes = new Bayes();
		HashMap<String,Txt> txt_map = bayes.trainMultiNomialNB(new Directory(TRAIN_DIR), stopwords);
		HashMap<String,String> ham_map = new HashMap<String,String>();
		HashMap<String,String> spam_map = new HashMap<String,String>();
		
		// ham folder test
		File test_ham_dir = new File(TEST_HAM_DIR);
		File[] test_ham_files = test_ham_dir.listFiles();
		for(File file:test_ham_files)
		{
			if(!file.isDirectory())
			{
				String type = bayes.applyMultiNomialNB(txt_map, file.getPath());
				ham_map.put(file.getName(), type);
			}
		}
		
		System.out.println("-----------The test results of ham files are: -----------");
		Iterator<Entry<String, String>> ham_itr = ham_map.entrySet().iterator();
		int ham_cnt = 0;
		int spam_cnt = 0;
		int total_ham_cnt = 0;
		while(ham_itr.hasNext())
		{
			Entry<String, String> entry = ham_itr.next();
			String fileType = entry.getValue();
			if(fileType.equalsIgnoreCase("spam"))
				spam_cnt++;
			if(fileType.equalsIgnoreCase("ham"))
				ham_cnt++;
			total_ham_cnt++;
		}
		System.out.println("-----------There are " +  spam_cnt + " of "+ total_ham_cnt + " wrongly classified files ------------");
		System.out.println("-----------The accuracy of ham files is: " + (double)ham_cnt/total_ham_cnt*100 + "%------------");
		System.out.println();
		
		// spam folder test
		File test_spam_dir = new File(TEST_SPAM_DIR);
		File[] test_spam_files = test_spam_dir.listFiles();
		for(File file:test_spam_files)
		{
			if(!file.isDirectory())
			{
				String type = bayes.applyMultiNomialNB(txt_map, file.getPath());
				spam_map.put(file.getName(), type);
			}
		}
		
		System.out.println("-----------The test results of spam files are: -----------");
		Iterator<Entry<String, String>> spam_itr = spam_map.entrySet().iterator();
		spam_cnt = 0;
		ham_cnt = 0;
		int total_spam_cnt = 0;
		while(spam_itr.hasNext())
		{
			Entry<String, String> entry = spam_itr.next();
			String fileType = entry.getValue();
			if(fileType.equalsIgnoreCase("ham"))
				ham_cnt++;
			if(fileType.equalsIgnoreCase("spam"))
				spam_cnt++;
			total_spam_cnt++;
		}
		System.out.println("-----------There are " +  ham_cnt + " of "+ total_spam_cnt + " wrongly classified files ------------");
		System.out.println("-----------The accuracy of spam files is: " + (double)spam_cnt/total_spam_cnt*100 + "%-----------");
		System.out.println();
	}

	private static void logisticReg(double lambda, boolean stopwords)
	{
		if(stopwords)
			System.out.println("----------- Without Stopwords -----------");
		else
			System.out.println("----------- With Stopwords -----------");
		
		System.out.println("********* lambda is " + lambda+  " *********");
				
		LogisticRegression logistic = new LogisticRegression(lambda);
		
		logistic.train(new Directory(TRAIN_DIR), stopwords);
		
		HashMap<String,String> ham_map = new HashMap<String,String>();
		HashMap<String,String> spam_map = new HashMap<String,String>();
		
		// ham folder test
		File test_ham_dir = new File(TEST_HAM_DIR);
		File[] test_ham_files = test_ham_dir.listFiles();
		for(File file:test_ham_files)
		{
			if(!file.isDirectory())
			{
				String type = logistic.apply(file.getPath(),"ham","spam");
				ham_map.put(file.getName(), type);
			}
		}
		
		System.out.println("-----------The test results of ham files are: -----------");
		Iterator<Entry<String, String>> ham_itr = ham_map.entrySet().iterator();
		int ham_cnt = 0;
		int spam_cnt = 0;
		int total_ham_cnt = 0;
		while(ham_itr.hasNext())
		{
			Entry<String, String> entry = ham_itr.next();
			String fileType = entry.getValue();
			if(fileType.equalsIgnoreCase("spam"))
				spam_cnt++;
			if(fileType.equalsIgnoreCase("ham"))
				ham_cnt++;
			total_ham_cnt++;
		}
		System.out.println("-----------There are " +  spam_cnt + " of "+ total_ham_cnt + " wrongly classified files ------------");
		System.out.println("-----------The accuracy of ham files is: " + (double)ham_cnt/total_ham_cnt*100 + "%------------");
		System.out.println();
		
		// spam folder test
		File test_spam_dir = new File(TEST_SPAM_DIR);
		File[] test_spam_files = test_spam_dir.listFiles();
		for(File file:test_spam_files)
		{
			if(!file.isDirectory())
			{
				String type = logistic.apply(file.getPath(),"spam","ham");
				spam_map.put(file.getName(), type);
			}
		}
		
		System.out.println("-----------The test results of spam files are: -----------");
		Iterator<Entry<String, String>> spam_itr = spam_map.entrySet().iterator();
		ham_cnt = 0;
		spam_cnt = 0;
		int total_spam_cnt = 0;
		while(spam_itr.hasNext())
		{
			Entry<String, String> entry = spam_itr.next();
			String fileType = entry.getValue();
			if(fileType.equalsIgnoreCase("ham"))
				ham_cnt++;
			if(fileType.equalsIgnoreCase("spam"))
				spam_cnt++;
			total_spam_cnt++;
		}
		System.out.println("-----------There are " +  ham_cnt + " of "+ total_spam_cnt + " wrongly classified files ------------");
		System.out.println("-----------The accuracy of spam files is: " + (double)spam_cnt/total_spam_cnt*100 + "%-----------");
		System.out.println();
	}
}
