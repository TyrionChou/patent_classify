package cn.bistu.icdd.zh.textrepresentation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.bistu.icdd.zh.util.RWFile;
import cn.bistu.icdd.zh.util.TFIDF;

public class TextRepresentation {
	
//	//特征词总数量
//	final static double featuresNum = 731;	
	//输入特征值路径
	final static String featuresFilePath = "./source/chifeatures.txt";
	//输入训练数据的路径
	final static String dataTrainDirPath = "C:/program/GitHub/训练处理数据";
	final static String dataTestDirPath = "C:/program/GitHub/测试处理数据";
	//输出训练数据样本
	final static String outTrainFilePath = "./source/train.txt";
	//输出测试数据样本
	final static String outTestFilePath = "./source/test.txt";
	//输入训练样本文本总数
	final static double totalNum = 8000;
	
	
	//用于记录特征词的数据
	static ArrayList<ArrayList<String>> featuresSet = new ArrayList<ArrayList<String>>();
	//用于记录特征词的数据，这样比ArrayList访问速度快
	static HashMap<String, Double> words = new HashMap<String, Double>();
	//用于记录特征词的数据位置信息
	static HashMap<String, Integer> wordPostion = new HashMap<String, Integer>();
	//用于存储每篇文章的TFIDF
	static TFIDF tfidf;
	
	public static void main(String[] args){
		initTextRepresentation();
		representText(dataTrainDirPath, outTrainFilePath);
		representText(dataTestDirPath, outTestFilePath);
		System.out.println("Finish");
	}
	
	//TFIDF类初始化一些静态参数
	public static void initTextRepresentation(){

		File file = new File(featuresFilePath);
		if(file.exists()){
			//读出特征词表中的词，
			featuresSet = RWFile.readLineMultipleTableContent(featuresFilePath);
			//初始化特征词总数量
			TFIDF.setFeaturesNum(featuresSet.size());
			//初始化训练样本文本总数
			TFIDF.setN(totalNum);
			
			//初始化每个特征词的文本出现数
			for(int i = 0; i < TFIDF.getFeaturesNum(); i++){
				TFIDF.addNt(0);
			}
			//读出特征词表中的每一个词 ，例如修剪 43.0 0.00974125
			int wordNum = 0;
			for(ArrayList<String> word : featuresSet){
				//将每一个词都存入hashmap，并记录位置信息，以便以后记录特征词tf值
				words.put(word.get(0), Double.valueOf(word.get(1)));
				wordPostion.put(word.get(0), wordNum);
				TFIDF.setNt(wordNum, Double.valueOf(word.get(1)));
				wordNum++;
			}
			
		}
	}
	
	//将文本根据特征词表示为向量
	public static void representText(String filePath, String outPath){
		File file = new File(outPath);
		file.delete();
		processFile(filePath, outPath);
	}
	
	//递归读取文本，每一篇文章的tf/idf值
	public static void processFile(String filePath, String outPath){
		File file = new File(filePath);
		if(!file.isDirectory()){
			tfidf = new TFIDF();
			String fileType = filePath.split("/")[filePath.split("/").length - 1].substring(0, 1);
			tfidf.setFileType(confirmType(fileType));
			//读文档内容
			String content = RWFile.readFileContent(filePath);
			//分解文章内容
			String[] wordsContent = content.split(" ");
			//循环判断文本中的词是否在wordPostion中，有，则给这个特征词的位置上加1，没有，则跳过
			for(int i = 0; i < wordsContent.length; i++){
				if(wordPostion.containsKey(wordsContent[i])){
					tfidf.setTf(wordPostion.get(wordsContent[i]), 
							(tfidf.getTf(wordPostion.get(wordsContent[i]))+1));
				}
			}
			//计算某个特征词TF/IDF归一化值的分子
			tfidf.countTfIdfUp();
			//计算TF/IDF归一化值的分母
			tfidf.counttfIdfDown();
			//计算每个特征词TF/IDF的值
			tfidf.countTfIdf();
			String vsmLine = tfidf.getFileType() + " "; 
			for(int i = 0; i < TFIDF.getFeaturesNum(); i++){
				if(tfidf.getTfIdf(i) != 0.0){
				vsmLine += String.valueOf(i) + ":" + tfidf.getTfIdf(i) + " ";
				}
			}
			RWFile.writeLineMultipleContent(outPath, vsmLine);
		}else if(file.isDirectory()){
			String[] fileList = file.list();
			for(int i = 0; i < fileList.length ; i++){
				processFile(filePath + "/" + fileList[i], outPath);
			}
		}
	}
	
	//将类型转化为数字，例如将A转化为0
	public static String confirmType(String type){
		switch(type){
		case "A":
			type = "0";
			break;
		case "B":
			type = "1";
			break;
		case "C":
			type = "2";
			break;
		case "D":
			type = "3";
			break;
		case "E":
			type = "5";
			break;
		case "F":
			type = "6";
			break;
		case "G":
			type = "7";
			break;
		case "H":
			type = "8";
			break;
		}
		return type;
	}
}
