package cn.bistu.icdd.zh.procprocessor;
import java.util.ArrayList;
import java.util.HashSet;

import cn.bistu.icdd.zh.util.RWFile;

/**
 * 用于分词后的非中文字符过滤及去停用词
 */


public class WordsFilter {
	
	static HashSet<String> stopWordSet = new HashSet<String>();
	static ArrayList<ArrayList<String>> normalizeWordSet = new ArrayList<ArrayList<String>>();
	
	public static void stopWordsFilterInit(String tablePath){
		stopWordSet = RWFile.readLineOneTableContent(tablePath);
	}
	
	public static void normailzeFilterInit(String tablePath){
		normalizeWordSet = RWFile.readLineMultipleTableContent(tablePath);
	}
	
	public static String process(String content){
		content = chineseFilter(content);
		content = stopWordsFilter(content);
//		content = normalizeFilter(content);
		return content;
	}
	
//	//去非中文字符
//	public static String chineseFilter(String content){
//		String[] segmentWords = content.split(" ");
//		for(int i =0 ; i < segmentWords.length; i++){
//			if(!isChinese(segmentWords[i])){
//				segmentWords[i] = "";
//			}
//		}
//		content = "";
//		for(int i = 0; i < segmentWords.length; i++ ){
//			if(segmentWords[i] != ""){
//				content += segmentWords[i] + " ";
//			}
//		}
//		return content;
//	}
	
	//去非中文字符
	public static String chineseFilter(String content){
		char[] contentArray = content.toCharArray();
		for (int i = 0; i < contentArray.length; i++) {
            if (!isChinese(contentArray[i])){
            	contentArray[i] = 0x20;
            }
        }
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
	
	
	
	//去停用词
	public static String stopWordsFilter(String content){

		
		String[] segmentWords = content.split(" ");
		
		for(int i = 0; i < segmentWords.length; i++ ){
			if(stopWordSet.contains(segmentWords[i])){
				segmentWords[i] = "";
			}
		}
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

		
		String[] segmentWords = content.split(" ");
		
		for(int i = 0; i < segmentWords.length; i++ ){
			for(ArrayList<String> oneLineWordSet : normalizeWordSet){
				if(oneLineWordSet.contains(segmentWords[i])){
					segmentWords[i] = oneLineWordSet.get(0);
					break;
				}
			}
		}
		content = "";
		for(int i = 0; i < segmentWords.length; i++ ){
			if(segmentWords[i] != ""){
				content += segmentWords[i] + " ";
			}
		}
		return content;
	}

}
