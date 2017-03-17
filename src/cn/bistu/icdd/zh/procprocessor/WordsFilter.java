package cn.bistu.icdd.zh.procprocessor;
import java.util.HashSet;

import cn.bistu.icdd.zh.util.RWFile;

/**
 * 用于分词后的非中文字符过滤及去停用词
 */


public class WordsFilter {
	
	public static String process(String content, String filePath){
		content = chineseFilter(content);
		content = stopWordsFilter(content, filePath);
		return content;
	}
	
	//去非中文字符
	public static String chineseFilter(String content){
		String[] segmentWords = content.split(" ");
		for(int i =0 ; i < segmentWords.length; i++){
			if(!isChinese(segmentWords[i])){
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
	
	//去停用词
	public static String stopWordsFilter(String content, String filePath){
		HashSet<String> stopWordSet = new HashSet<String>();
		stopWordSet = RWFile.readTableContent(filePath);
		
		String[] segmentWords = content.split(" ");
		
		for(int i = 0; i < segmentWords.length; i++ ){
			for(String segmentWord : stopWordSet){
				if(segmentWords[i].contains(segmentWord)){
					segmentWords[i] = "";
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
}
