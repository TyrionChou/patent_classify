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

		// printf函数声明
		public int NLPIR_Init(byte[] sDataPath, int encoding,
				byte[] sLicenceCode);
		
		public int NLPIR_AddUserWord(String sWord);
		
		// 导入用户自定义词典：自定义词典路径，bOverwrite=true表示替代当前的自定义词典，false表示添加到当前自定义词典后
		public int NLPIR_ImportUserDict(String sFilename, boolean bOverwrite);  

		public boolean NLPIR_NWI_Start();
		
		public int NLPIR_NWI_AddFile(String sFilename);
		
		public String NLPIR_NWI_GetResult(boolean bWeightOut);//输出新词识别结果
		
		public int  NLPIR_NWI_Result2UserDict();
		
		public boolean NLPIR_NWI_Complete();
		
		public int NLPIR_SaveTheUsrDic();
		
		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);


		public void NLPIR_Exit();
	}
	//初始化Instance
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
	
	//释放Instance
	public static void instanceExit(){
		CLibrary.Instance.NLPIR_Exit();
	}
	
	//添加用户词典
	public static void userWordsImport(String sWord){
		CLibrary.Instance.NLPIR_AddUserWord(sWord);
	}
	
	//添加用户词典，返回用户词典中的词数量
	public static void userDictImport(String sFilename){
		CLibrary.Instance.NLPIR_ImportUserDict(sFilename, false);
	}
	//开始添加用户词典，返回是否成功
	public static void userDictAddStart(){
		CLibrary.Instance.NLPIR_NWI_Start();
	}
	//检测文章中的新词，返回是否成功
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
