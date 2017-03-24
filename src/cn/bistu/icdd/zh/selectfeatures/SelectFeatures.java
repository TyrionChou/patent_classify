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
	
	//输入待数据路径
	final static String filePath = "C:/program/GitHub/训练处理数据";
	//输出特征值路径
	final static String outFilePath = "./source/features.txt";
	//分类的类别数
	final static double classNum = 8;
	//分类的类别名称
	final static String className = "A B C D E F G H";
	//每一类的文章总数
	final static double batchNum = 1000;
	
	//记录每一篇文章中出现的词
	static HashSet<String> word = new HashSet<String>();
	//记录每一个词的情况
	static HashMap<String,Entropy> words = new HashMap<String,Entropy>();
	
	public static void main(String[] args){
		selectFeaturesInit();
		countWords(filePath);
		selectFeatures(filePath);
		System.out.println("Finish");
	}
	
	//Entropy类初始化一些静态参数
	public static void selectFeaturesInit(){
		//设置分类的类别数
		Entropy.setClassNum(classNum);
		//设置分类的类别名称
		String[] classesName = className.split(" ");
		for(int i = 0; i < Entropy.getClassNum(); i++){
			Entropy.addClassName(i, classesName[i]);
		}
		//设置每一类的文章总数
		for(int i = 0; i < Entropy.getClassNum(); i++){
			Entropy.addBatchsNum(i, batchNum);
		}
		//计算所有类文章实例总数
		Entropy.countTotalNum();
		//计算类别初始熵，保留四位小数
		Entropy.countClassEntropy();
	}
	//递归将所有文本中出现过的单词放入HashMap<String,Entropy> words中
	public static void countWords(String filePath){
		File file = new File(filePath);
		if(!file.isDirectory()){
			//读文本内容
			String content = RWFile.readFileContent(filePath);
			//分解文本内容
			String[] wordsContent = content.split(" ");
			//循环判断是否words中含有单词，有，则跳过，没有，则加入words
			for(int i = 0; i < wordsContent.length; i++){
				if(!words.containsKey(wordsContent[i])){
					Entropy entropy = new Entropy();
					entropy.setWordName(wordsContent[i]);
					words.put(wordsContent[i], entropy);
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
	public static void selectFeatures(String filePath) {
		//遍历所有文本，在文章中出现且在words中出现的词，在相应的类别文章数中加1
		countWordFrequency(filePath);
		//特征文件存在现将其删除
		File file = new File(outFilePath);
		file.delete();
		//循环每个词计算信息增益
		Iterator<Entry<String, Entropy>> iter = words.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Entropy> entry = (Map.Entry<String, Entropy>) iter.next();
			Entropy entropy = (Entropy) entry.getValue();
			//计算单词出现总数
			entropy.countTotalOccurredNum();
			//计算单词出现的概率，保留四位小数
			entropy.countWordOccurredProbability();
			//计算单词出现后，每一个类发生的概率，保留四位小数
			entropy.countWordOccurredCProbability();
			//计算单词未出现后，每一个类发生的概率，保留四位小数
			entropy.countWordNotOccurredCProbability();
			//计算信息增益值
			entropy.countIG();
//			RWFile.writeLineMultipleContent(outFilePath, entropy.getWordName() + " " 
//					+ entropy.getTotalOccurredNum() + " " +entropy.getIG());
			//信息增益大于0.008的词输出
			if(entropy.getIG() > 0.008){
				RWFile.writeLineMultipleContent(outFilePath, entropy.getWordName() + " " 
						+ entropy.getTotalOccurredNum() + " " +entropy.getIG());
			}
		}
	}
	
	//遍历所有文章，在文本中出现且在words中出现的词，在相应的类别文章数中加1
	public static void countWordFrequency(String filePath){
		File file = new File(filePath);
		if(!file.isDirectory()){
			//提取文本类别
			String fileType = filePath.split("/")[filePath.split("/").length - 1].substring(0, 1);
			String content = RWFile.readFileContent(filePath);
			String[] contentWords = content.split(" ");
			word.clear();
			//记录文本中出现的词
			for(int i = 0; i < contentWords.length; i++){
				if(!word.contains(contentWords[i])){
					word.add(contentWords[i]);
				}
			}
			//遍历每一个文本中出现的词，与words词表进行对比，如果有，则在words单词相应的类别文章数中加1
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
