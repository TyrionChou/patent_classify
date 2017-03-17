package cn.bistu.icdd.zh.procprocessor;

import java.io.File;

import cn.bistu.icdd.zh.util.RWFile;

/**
 * txt文件预处理类
 */

public class ProcProcessor {
	
	public static void main(String[] args){
		procProcess("C:/program/GitHub/训练数据", "./source/stopwords.txt");
	}
	
	
	//输出所在文件夹名称
	final static String dirName = "预处理数据";
	
	public static void procProcess(String filePath,String tablePath){
		NlpirSegment.instanceInit();
		fileProcess(filePath,tablePath);
		NlpirSegment.instanceExit();
	}
	
	//递归读取文件夹下的文件，进行文本预处理后，输出到相应的文件夹下
	public static void fileProcess(String filePath,String tablePath){
		File file = new File(filePath);
		if(!file.isDirectory()){
			//计算新产生路径
			String newFilePath = countNewFilePath(filePath);
			String content = "";
			//读文档内容
			content = RWFile.readFileContent(filePath);
			//进行预处理
			content = process(content,tablePath);
			//写入文档
			RWFile.writeFileContent(newFilePath,content);
		}else if(file.isDirectory()){
			String[] fileList = file.list();
			for(int i = 0; i < fileList.length ; i++){
				fileProcess(filePath + "/" + fileList[i],tablePath);
			}
		}
	}
	
	//计算输出文件的路径
	public static String countNewFilePath(String filePath){
		
		String[] path = filePath.split("/");
		//记录路径中不变的路径  C:/训练数据/A/XXX
		String constantPath = path[path.length -2] + "/" + path[path.length -1];
		String newFilePath = "";
		for(int i = 0; i < path.length - 3 ; i++){
			newFilePath += path[i] + "/";
		}
		//记录路径中不变的路径  C:/预处理数据/A/XXX
		newFilePath += dirName + "/" + constantPath;
		System.out.println(newFilePath);
		return newFilePath;
	}
	
	//文件预处理
	public static String process(String content, String tablePath){
		content = NlpirSegment.process(content);
		content = WordsFilter.process(content, tablePath);
		return content;
	}
	
}
