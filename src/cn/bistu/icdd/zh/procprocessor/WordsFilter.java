package cn.bistu.icdd.zh.procprocessor;
import java.util.ArrayList;
import java.util.HashSet;

import cn.bistu.icdd.zh.util.RWFile;

/**
 * 用于分词后的非中文字符过滤及去停用词
 */
public class WordsFilter {
	
	//用来存储停用词典
	static HashSet<String> stopWordSet = new HashSet<String>();
	//用来存储归一化词典
	static ArrayList<ArrayList<String>> normalizeWordSet = new ArrayList<ArrayList<String>>();
	
	//根据停用词典路径读取停用词
	public static void stopWordsFilterInit(String tablePath){
		stopWordSet = RWFile.readLineOneTableContent(tablePath);
	}
	
	//根据归一化词典路径读取归一化词
	public static void normailzeFilterInit(String tablePath){
		normalizeWordSet = RWFile.readLineMultipleTableContent(tablePath);
	}
	
	//去掉字符串中的非中文字符，并且用" "代替
	public static String chineseFilter(String content){
		//将字符串转换为Unicode字符数组
		char[] contentArray = content.toCharArray();
		//字符如果是非中文字符，将其替换为" "
		for (int i = 0; i < contentArray.length; i++) {
            if (!isChinese(contentArray[i])){
            	contentArray[i] = 0x20;
            }
        }
		//将Unicode字符数组转换为字符串
		content = String.valueOf(contentArray);
		return content;
	}
	
	// 判断一个字符是否是中文
    public static boolean isChinese(char c) {
        return c >= 0x4E00 &&  c <= 0x9FA5;// 根据字节码判断
    }
    
    // 判断一个字符串是否含有中文
    public static boolean isChinese(String str) {
        if (str == null) return false;
        for (char c : str.toCharArray()) {
            if (isChinese(c)) return true;// 有一个中文字符就返回
        }
        return false;
    }
	
    //去掉字符串中的停用词
	public static String stopWordsFilter(String content){
		//将待处理字符串进行分割
		String[] segmentWords = content.split(" ");
		//根据停用词典对停用词进行剔除
		for(int i = 0; i < segmentWords.length; i++ ){
			if(stopWordSet.contains(segmentWords[i])){
				segmentWords[i] = "";
			}
		}
		//将数组拼接为字符串并返回
		content = "";
		for(int i = 0; i < segmentWords.length; i++ ){
			if(segmentWords[i] != ""){
				content += segmentWords[i] + " ";
			}
		}
		return content;
	}
	
	//归一化
	public static String normalizeFilter(String content){
		//将待处理字符串进行分割
		String[] segmentWords = content.split(" ");
		//根据归一化词典对相同的词进行替换
		for(int i = 0; i < segmentWords.length; i++ ){
			for(ArrayList<String> oneLineWordSet : normalizeWordSet){
				if(oneLineWordSet.contains(segmentWords[i])){
					segmentWords[i] = oneLineWordSet.get(0);
					break;
				}
			}
		}
		//将数组拼接为字符串并返回
		content = "";
		for(int i = 0; i < segmentWords.length; i++ ){
			if(segmentWords[i] != ""){
				content += segmentWords[i] + " ";
			}
		}
		return content;
	}

}
