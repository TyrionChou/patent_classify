package cn.bistu.icdd.zh.selectfeatures;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;


import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.bistu.icdd.zh.util.RWFile;
import cn.bistu.icdd.zh.util.Entropy;

/**
 * 用来选取特征值
 */
public class SelectFeatures {

	//待数据路径
	final static String filePath = "C:/program/GitHub/预处理数据";
	//数据路径
	final static String outFilePath = "C:/program/GitHub/特征值表.txt";
	//分类的类别
	final static double classNum = 8;
	//分类的类别名称
	final static String className = "A B C D E F G H";
	//每一类的文章总数
	final static double batchNum = 1000;
	
	//记录特征词的数量
	static HashSet<String> word = new HashSet<String>();
//	//记录特征词的IG
	static HashMap<String,Entropy> words = new HashMap<String,Entropy>();
	
	public static void main(String[] args){
		selectFeaturesInit();
		countWords(filePath);
		selectFeatures(filePath);
	}
	
	public static void selectFeaturesInit(){
		
		Entropy.setClassNum(classNum);
		String[] classesName = className.split(" ");
		for(int i = 0; i < Entropy.getClassNum(); i++){
			Entropy.setClassName(i, classesName[i]);
		}
		for(int i = 0; i < Entropy.getClassNum(); i++){
			Entropy.setBatchsNum(i, batchNum);
		}
		Entropy.setTotalNum();
		Entropy.setClassEntropy();
	}
	//递归取单词
	public static void countWords(String filePath){
		File file = new File(filePath);
		if(!file.isDirectory()){
			//读文档内容
			String content = RWFile.readFileContent(filePath);
//			String content ="机器 机器";
			//进行预处理
			String[] wordsContent = content.split(" ");
			for(int i =0 ; i < wordsContent.length; i++){
				if(!words.containsKey(wordsContent[i])){
					Entropy entropy = new Entropy();
					entropy.setWordName(wordsContent[i]);
					words.put(wordsContent[i].trim(), entropy);
				}
			}
		}else if(file.isDirectory()){
			String[] fileList = file.list();
			for(int i = 0; i < fileList.length ; i++){
				countWords(filePath + "/" + fileList[i]);
			}
		}
	}
	
	
	//计算每个词的信息增益
	public static void selectFeatures(String filePath){
		countWordFrequency(filePath);
		//循环每个词
		Iterator<Entry<String, Entropy>> iter = words.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Entropy> entry = (Map.Entry<String, Entropy>) iter.next();
			Entropy entropy = (Entropy) entry.getValue();
			entropy.setTotalOccurredNum();
			entropy.setWordOccurredProbability();
			entropy.setWordOccurredCProbability();
			entropy.setWordNotOccurredCProbability();
			entropy.setIG();
			if(entropy.getIG() > 0.008){
				RWFile.writeLineMultipleContent(outFilePath, entropy.getWordName() + " " 
						+ entropy.getTotalOccurredNum() + " " +entropy.getIG());
			}
		}
	}
	
	//递归文章中所有的词，在words中在相应的类别加1
	public static void countWordFrequency(String filePath){
		File file = new File(filePath);
		if(!file.isDirectory()){
			String fileType = filePath.split("/")[filePath.split("/").length - 1].substring(0, 1);
			String content = RWFile.readFileContent(filePath);
			String[] contentWords = content.split(" ");
			word.clear();
			for(int i = 0; i < contentWords.length; i++){
				if(!word.contains(contentWords[i])){
					word.add(contentWords[i]);
				}
			}
			for(String str : word){	
				if(words.containsKey(str)){
					Entropy entropy = words.get(str);
					for(int i = 0; i < Entropy.getClassName().size(); i++){
						if(fileType.equals(Entropy.getClassName(i))){
							entropy.setBatchOccurredNum(i, (entropy.getBatchOccurredNum(i) + 1));
							break;
						}
					}
					words.put(str, entropy);
				}
			}
			
			
		}else if(file.isDirectory()){
			String[] fileList = file.list();
			for(int i = 0; i < fileList.length ; i++){
				countWordFrequency(filePath + "/" + fileList[i]);
			}
		}
	}
}
