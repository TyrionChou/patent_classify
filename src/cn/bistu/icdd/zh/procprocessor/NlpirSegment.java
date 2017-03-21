package cn.bistu.icdd.zh.procprocessor;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * 用于文本分词的类
 */

public class NlpirSegment {
	
	// 定义接口CLibrary，继承自com.sun.jna.Library
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量
		//相对路径
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				"./source/NLPIR", CLibrary.class);

		//初始化分词工具
		public int NLPIR_Init(byte[] sDataPath, int encoding,
				byte[] sLicenceCode);
		
		//将词添加到用户词典
		public int NLPIR_AddUserWord(String sWord);
		
		//开启检测新词
		public boolean NLPIR_NWI_Start();
		
		//检测文章中的新词
		public int NLPIR_NWI_AddFile(String sFilename);
		
		//返回文章中的新词
		public String NLPIR_NWI_GetResult(boolean bWeightOut);//输出新词识别结果
		
		//结束检测新词
		public boolean NLPIR_NWI_Complete();
		
		//分词
		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		//析构分词工具
		public void NLPIR_Exit();
	}
	
	//初始化分词工具
	public static void instanceInit(){
		//相对路径
		String argu = "";
		String system_charset = "GBK";
		int charset_type = 1;
		int init_flag = 0;
		try {
			init_flag = CLibrary.Instance.NLPIR_Init(argu
					.getBytes(system_charset), charset_type, "0"
					.getBytes(system_charset));
			if (0 == init_flag) {
				System.err.println("初始化失败！");
				return;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	//析构分词工具
	public static void instanceExit(){
		CLibrary.Instance.NLPIR_Exit();
	}
	
	//添加用户词典
	public static void userWordsAdd(String sWord){
		CLibrary.Instance.NLPIR_AddUserWord(sWord);
	}
	
	//开启检测新词
	public static void userDictAddStart(){
		CLibrary.Instance.NLPIR_NWI_Start();
	}
	
	//检测文章中的新词
	public static void userDictAddFile(String sFilename){
		CLibrary.Instance.NLPIR_NWI_AddFile(sFilename);
	}
	
	//检测文章中的新词，返回新词结果
	public static String newWordsGetResult(){
		return CLibrary.Instance.NLPIR_NWI_GetResult(false);
	}
	
	//结束添加用户词典，返回是否成功
	public static void userDictAddComplete(){
		CLibrary.Instance.NLPIR_NWI_Complete();
	}
	
	//进行分词
	public static String process(String content){
		String nativeBytes = "";
		try {
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(content, 0);
			//将多个空格变为一个空格
			nativeBytes = nativeBytes.replaceAll("\\s+", " ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return nativeBytes;
	
	}
}
