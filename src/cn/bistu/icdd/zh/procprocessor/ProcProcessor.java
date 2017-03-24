package cn.bistu.icdd.zh.procprocessor;

import java.io.File;
import java.util.HashSet;

import cn.bistu.icdd.zh.util.RWFile;

/**
 * txt文件预处理类 分词，去除非中文字符，去除停用词，归一化
 */

public class ProcProcessor {
	
	//运行程序
	public static void main(String[] args){
		procProcessInit("C:/program/GitHub/训练数据", "./source/userdict.txt","./source/stopwords.txt","./source/synonym.txt");
		procProcess();
		procProcessInit("C:/program/GitHub/测试数据", "./source/userdict.txt","./source/stopwords.txt","./source/synonym.txt");
		procProcess();
		System.out.println("Finish");
	}
	
	//输入训练数据所在文件夹名称
	final static String inTrainDirName = "训练数据";
	//输出训练处理数据所在文件夹名称
	final static String outTrainDirName = "训练处理数据";
	
	//输入测试数据所在文件夹名称
	final static String inTestDirName = "测试数据";
	//输出测试处理数据所在文件夹名称
	final static String outTestDirName = "测试处理数据";
	
	//输入训练数据的路径
	static String dataDirPath = "";
	//用户词典路径
	static String userWordsDictPath = "";
	//停用词典路径
	static String stopWordsDictPath = "";
	//归一化词典路径
	static String synonymDictPath = "";
	
	//用于存储新词处理中产生的新词
	static String newUserWords = "";
	
	//预处理初始化训练数据路径，用户词典路径，停用词典路径，归一化词典路径
	public static void procProcessInit(String filePath, String userDictPath, String stopWordsPath, String synonymPath){
		dataDirPath = filePath;
		userWordsDictPath = userDictPath;
		stopWordsDictPath = stopWordsPath;
		synonymDictPath = synonymPath;
	}
	
	//训练数据预处理
	public static void procProcess(){
		
		//初始化分词工具
		NlpirSegment.instanceInit();
		//用户词典初始化
//		userDictInit(userWordsDictPath);
		//停用词典初始化
		WordsFilter.stopWordsFilterInit(stopWordsDictPath);
//		WordsFilter.normailzeFilterInit(synonymDictPath);
		//训练数据预处理
		fileProcess(dataDirPath);
		//释放分词工具实例
		NlpirSegment.instanceExit();
	}
	
	//用户词典初始化
	public static void userDictInit(String userDictPath){
		File file = new File(userDictPath);
		HashSet<String> wordSet = new HashSet<String>();
		try {
			//用户词典不存在
			if(!file.exists()){	
				//建立用户词典
				userDictProcess(dataDirPath);
				//将用户词典写入用户词典路径
				RWFile.writetableContent(userDictPath, newUserWords);	
			}
			//读取用户词典信息
			wordSet = RWFile.readLineOneTableContent(userDictPath);
			//加载用户词典
			for(String str : wordSet){
				NlpirSegment.userWordsAdd(str);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//递归读取文件内容，调用分词工具，检测新词，记录到newUserWords中
	public static void userDictProcess(String filePath){
		File file = new File(filePath);
		if(!file.isDirectory()){
			//每次都进行开启和关闭的操作，是因为分词工具它本身存储新词的缓存区只有几十k空间
			//开启检测新词
			NlpirSegment.userDictAddStart();
			//检测文章中的新词
			NlpirSegment.userDictAddFile(filePath);
			//结束检测新词
			NlpirSegment.userDictAddComplete();
			//返回文章中的新词
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
			//计算预处理输出数据路径
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
	
	//根据输入文件的路径计算预处理输出数据路径
	public static String countNewFilePath(String filePath){
		//分解输入数据路径
		String[] path = filePath.split("/");
		//将其中包含的“训练数据”字符串文件夹变换为“预处理数据文件夹”
		for(int i = 0; i < path.length; i++){
			if(path[i].equals(inTrainDirName)){
				path[i]= outTrainDirName;
			}else if(path[i].equals(inTestDirName)){
				path[i]= outTestDirName;
			}
		}
		//拼装预处理输出路径
		String newFilePath = "";
		for(int i = 0; i < path.length - 1 ; i++){
			newFilePath += path[i] + "/";
		}
		newFilePath += path[path.length - 1];
		System.out.println(newFilePath);
		return newFilePath;
	}
	
	//文件预处理，返回处理后的字符串
	public static String process(String content){
		//去掉字符串中的非中文字符，并且用" "代替
		content = WordsFilter.chineseFilter(content);
		//对字符串进行中文分词
		content = NlpirSegment.process(content);
//		content = WordsFilter.process(content);
		//去掉字符串中的停用词
		content = WordsFilter.stopWordsFilter(content);
		return content;
	}
	
}
