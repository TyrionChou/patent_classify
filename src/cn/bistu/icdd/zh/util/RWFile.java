package cn.bistu.icdd.zh.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;


public class RWFile {
	
	//读取文件信息，并返回字符串
	public static String readFileContent(String filePath){
		String content = "";
		try {
			File file = new File(filePath);
			FileInputStream in = new FileInputStream(file);
			InputStreamReader read = new InputStreamReader(in);
			//方便将文件的内容以字符串的形式读取
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null; 
			//按行读取内容
			while((lineTxt = bufferedReader.readLine()) != null){ 
	            content += lineTxt.trim()+" ";
	          } 
			bufferedReader.close();
			read.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
	
	//读取词表信息，词表信息的内部格式是一行只有一个词，用于读停用词典
	public static HashSet<String> readLineOneTableContent(String tablePath){
		HashSet<String> wordSet = new HashSet<String>();
		try {
			File file = new File(tablePath);
			FileInputStream in = new FileInputStream(file);
			InputStreamReader read = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null; 
			while((lineTxt = bufferedReader.readLine()) != null){ 
				wordSet.add(lineTxt);
	          } 
			bufferedReader.close();
			read.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wordSet;
	}
	
	//读取词表信息，词表信息的内部格式是一行包含多个词，用于读归一化词典
	public static ArrayList<ArrayList<String>> readLineMultipleTableContent(String filePath){
		ArrayList<ArrayList<String>> wordSet = new ArrayList<ArrayList<String>>();
		try {
			File file = new File(filePath);
			FileInputStream in = new FileInputStream(file);
			InputStreamReader read = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null; 
			String[] lineWordSet;
			while((lineTxt = bufferedReader.readLine()) != null){
				ArrayList<String> oneLineWordSet = new ArrayList<String>();
				lineWordSet = lineTxt.split(" ");
				//将每一行的内容存入一个ArrayList<String>
				for(int i = 0; i < lineWordSet.length; i++){
					oneLineWordSet.add(lineWordSet[i]);
				}
				//再讲ArrayList<String>存入ArrayList<ArrayList<String>>
				wordSet.add(oneLineWordSet);
	          } 
			bufferedReader.close();
			read.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wordSet;
	}
	
	//写入文件信息
	public static void writeFileContent(String filePath ,String content){
		File file = new File(filePath);
		try{
			//判断是否文件是否存在，如果有，则返回，如果没有，则创建文件。
			createFile(file);
			Writer out = new FileWriter(file);
			out.write(content);
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//根据分词工具返回的新词，写入用户词典文件
	public static void writetableContent(String filePath ,String content){
		File file = new File(filePath);
		try{
			//判断是否文件是否存在，如果有，则返回，如果没有，则创建文件。
			createFile(file);
			//新词是以"#"进行分割
			String[] newWords = content.split("#");
			
			Writer out = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(out);
			//每个词写一行
			for(int i = 0; i < newWords.length; i++){
				bw.write(newWords[i]);
				bw.newLine();
			}
			bw.close();
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//根据特征词计算后，将选取的特征词信息写入特征词表
	public static void writeLineMultipleContent(String filePath ,String content){
		File file = new File(filePath);
		try{
			//判断是否文件是否存在，如果有，则返回，如果没有，则创建文件。
			createFile(file);
			//追加写入
			Writer out = new FileWriter(file,true);
			BufferedWriter bw = new BufferedWriter(out);
			bw.append(content);
			bw.newLine();
			bw.close();
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// 创建单个文件
	public static boolean createFile(File file) {
		if (file.exists()) {// 判断文件是否存在
//			System.out.println("目标文件已存在" + file.getPath());
			return false;
		}
		if (file.getPath().endsWith(File.separator)) {// 判断文件是否为目录
			System.out.println("目标文件不能为目录！");
			return false;
		}
		if (!file.getParentFile().exists()) {// 判断目标文件所在的目录是否存在
			// 如果目标文件所在的文件夹不存在，则创建父文件夹
			System.out.println("目标文件所在目录不存在，准备创建它！");
			if (!file.getParentFile().mkdirs()) {// 判断创建目录是否成功
				System.out.println("创建目标文件所在的目录失败！");
				return false;
			}
		}
		try {
			if (file.createNewFile()) {// 创建目标文件
//				System.out.println("创建文件成功:" + filePath);
				return true;
			} else {
				System.out.println("创建文件失败！");
				return false;
			}
		} catch (Exception e) {// 捕获异常
			e.printStackTrace();
			System.out.println("创建文件失败！" + e.getMessage());
			return false;
		}
	}
}
