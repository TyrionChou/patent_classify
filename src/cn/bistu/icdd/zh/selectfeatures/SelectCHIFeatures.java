package cn.bistu.icdd.zh.selectfeatures;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.bistu.icdd.zh.util.CHI;
import cn.bistu.icdd.zh.util.RWFile;

public class SelectCHIFeatures {

	//输入待数据路径
	final static String filePath = "C:/program/GitHub/训练处理数据";
	//输出特征值路径
	final static String outFilePath = "./source/chifeatures.txt";
	//分类的类别数
	final static double classNum = 8;
	//分类的类别名称
	final static String className = "A B C D E F G H";
	//每一类的文章总数
	final static double batchNum = 1000;
	
	//记录每一篇文章中出现的词
	static HashSet<String> word = new HashSet<String>();
	//记录每一个词的情况
	static HashMap<String, CHI> words = new HashMap<String, CHI>();
	
	
	public static void main(String[] args){
		selectFeaturesInit();
		countWords(filePath);
		selectFeatures(filePath);
		System.out.println("Finish");
	}
	
	//CHI类初始化一些静态参数
	public static void selectFeaturesInit(){
		//设置分类的类别数
		CHI.setClassNum(classNum);
		//设置分类的类别名称
		String[] classesName = className.split(" ");
		for(int i = 0; i < CHI.getClassNum(); i++){
			CHI.addClassName(i, classesName[i]);
		}
		//设置每一类的文章总数
		for(int i = 0; i < CHI.getClassNum(); i++){
			CHI.addBatchsNum(i, batchNum);
		}
		//计算所有类文章实例总数
		CHI.countTotalNum();
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
					CHI chi = new CHI();
					chi.setWordName(wordsContent[i]);
					words.put(wordsContent[i], chi);
				}
			}
		}else if(file.isDirectory()){
			String[] fileList = file.list();
			for(int i = 0; i < fileList.length ; i++){
				countWords(filePath + "/" + fileList[i]);
			}
		}
	}
	
	
	//计算每个词的chi值
	public static void selectFeatures(String filePath) {
		//遍历所有文本，在文章中出现且在words中出现的词，在相应的类别文章数中加1
		countWordFrequency(filePath);
		//特征文件存在现将其删除
		File file = new File(outFilePath);
		file.delete();
		//循环每个词计算chi
		Iterator<Entry<String, CHI>> iter = words.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, CHI> entry = (Map.Entry<String, CHI>) iter.next();
			CHI chi = (CHI) entry.getValue();
			////计算单词出现在所有的文章总数
			chi.countTotalOccurredNum();
			//计算包含单词ti属于某一ci类中文本的频数
			chi.countA();
			//计算包含单词ti不属于某一ci类中文本的频数
			chi.countB();
			//计算不包含单词ti属于某一个ci类中文本的频数
			chi.countC();
			//计算不包含单词ti不属于某一ci类中文本的频数
			chi.countD();
			//计算ti对于每一个类的chi值
			chi.countChi();
			//计算ti单词ti最大的chi值
			chi.countMaxChi();
			if(chi.getMaxChi() > 100){
				RWFile.writeLineMultipleContent(outFilePath, chi.getWordName() + " " 
						+ chi.getTotalOccurredNum() + " " +chi.getMaxChi());
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
					CHI chi = words.get(str);
					for(int i = 0; i < CHI.getClassName().size(); i++){
						if(fileType.equals(CHI.getClassName(i))){
							chi.setBatchOccurredNum(i, (chi.getBatchOccurredNum(i) + 1));
							break;
						}
					}
					words.put(str, chi);
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
