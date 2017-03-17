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
	
	//进行分词
	public static String process(String content){
		String nativeBytes = "";
		try {
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(content, 0);	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return nativeBytes;
	
	}
}
