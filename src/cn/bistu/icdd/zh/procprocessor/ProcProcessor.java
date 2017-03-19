package cn.bistu.icdd.zh.procprocessor;

import java.io.File;
import java.util.HashSet;

import cn.bistu.icdd.zh.util.RWFile;

/**
 * txt文件预处理类 分词，去除非中文字符，去除停用词，归一化
 */

public class ProcProcessor {
	
	public static void main(String[] args){
		procProcessInit("C:/program/GitHub/训练数据", "./source/userdict.txt","./source/stopwords.txt","./source/synonym.txt");
		procProcess();
		System.out.println("Finish");
	}
	//输入所在文件夹名称
	final static String indirName = "训练数据";
	//输出所在文件夹名称
	final static String outdirName = "预处理数据";
	
	//数据路径
	static String dataDirPath = "";
	//用户词典路径
	static String userWordsDictPath = "";
	//停用词路径
	static String stopWordsDictPath = "";
	//归一化词路径
	static String synonymDictPath = "";
	
	static String newUserWords = "";
	
	public static void procProcessInit(String filePath, String userDictPath, String stopWordsPath, String synonymPath){
		dataDirPath = filePath;
		userWordsDictPath = userDictPath;
		stopWordsDictPath = stopWordsPath;
		synonymDictPath = synonymPath;
	}
	public static void procProcess(){
		
		NlpirSegment.instanceInit();
		userDictInit(userWordsDictPath);
		WordsFilter.stopWordsFilterInit(stopWordsDictPath);
//		WordsFilter.normailzeFilterInit(synonymDictPath);
		fileProcess(dataDirPath);
		NlpirSegment.instanceExit();
	}
	
	
	//加载用户词典
	public static void userDictInit(String userDictPath){
		File file = new File(userDictPath);
		HashSet<String> wordSet = new HashSet<String>();
		try {
			//用户词典不存在
			if(!file.exists()){			
			
				userDictProcess(dataDirPath);
				
			
				RWFile.writetableContent(userDictPath, newUserWords);
//					NlpirSegment.result2UserDict();
//					NlpirSegment.userDictSave();	
			}
			wordSet = RWFile.readLineOneTableContent(userDictPath);
			for(String str : wordSet){
				NlpirSegment.userWordsImport(str);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		//递归读取文件夹下的文件，进行添加新词
		public static void userDictProcess(String filePath){
			File file = new File(filePath);
			if(!file.isDirectory()){
				NlpirSegment.userDictAddStart();
				NlpirSegment.userDictAddFile(filePath);
				NlpirSegment.userDictAddComplete();
				newUserWords += NlpirSegment.newWordsGetResult();
			}else if(file.isDirectory()){
				String[] fileList = file.list();
				for(int i = 0; i < fileList.length ; i++){
					userDictProcess(filePath + "/" + fileList[i]);
				}
			}
		}	
	
	//递归读取文件夹下的文件，进行文本预处理后，输出到相应的文件夹下
	public static void fileProcess(String filePath){
		File file = new File(filePath);
		if(!file.isDirectory()){
			//计算新产生路径
			String newFilePath = countNewFilePath(filePath);
			String content = "";
			//读文档内容
			content = RWFile.readFileContent(filePath);
			//进行预处理
			content = process(content);
			//写入文档
			RWFile.writeFileContent(newFilePath,content);
		}else if(file.isDirectory()){
			String[] fileList = file.list();
			for(int i = 0; i < fileList.length ; i++){
				fileProcess(filePath + "/" + fileList[i]);
			}
		}
	}
	
	//计算输出文件的路径
	public static String countNewFilePath(String filePath){
//		String[] initPath = dataDirPath.split("/");
//		String[] nowFilePath = filePath.split("/");
//		int distance = nowFilePath.length - initPath.length;
//		
//		//保留两路径不一样的地方
//		String constantPath = "";
//		for(int i = nowFilePath.length - distance ; i < nowFilePath.length ; i++){
//			constantPath += nowFilePath[i] + "/";
//		}
//		//substring截取字符串不包含endIndex
//		constantPath = constantPath.substring(0,constantPath.length()-1);
//		
//		String newFilePath = "";
//		//去掉训练数据
//		for(int i = 0; i < initPath.length - 1 ; i++){
//			newFilePath += initPath[i] + "/";
//		}
//		
//		newFilePath += dirName + "/" + constantPath;
//		System.out.println(newFilePath);
		
		String[] path = filePath.split("/");
		for(int i = 0; i < path.length; i++){
			if(path[i].equals(indirName)){
				path[i]= outdirName;
			}
		}
		
		String newFilePath = "";
		for(int i = 0; i < path.length - 1 ; i++){
			newFilePath += path[i] + "/";
		}
		newFilePath += path[path.length - 1];
		System.out.println(newFilePath);
		return newFilePath;
	}
	
	//文件预处理
	public static String process(String content){
		content = WordsFilter.chineseFilter(content);
		content = NlpirSegment.process(content);
//		content = WordsFilter.process(content);
		content = WordsFilter.stopWordsFilter(content);
		return content;
	}
	
}
