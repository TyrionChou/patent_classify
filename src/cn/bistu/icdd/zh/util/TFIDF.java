package cn.bistu.icdd.zh.util;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 用来计算每一篇文章中单词的TF/IDF权值，输出将文章用特征词TF/IDF权值表示的向量
 */

public class TFIDF {
	
	//文本的类型
	private String fileType = "";
	
	//特征词总数量
	private static double featuresNum = 0;
		
	//记录文章中每个特征词的tf值
	private ArrayList<Double> tf = new ArrayList<Double>();
	
	
	//IDF:log(N/Nt+a)中的a
	private static double a = 0.01;
	//IDF:log(N/Nt+a)中的N，训练文本总数
	private static double n = 0;
	//IDF:log(N/Nt+a)中的Nt,文本集中含有特征t的文本的数量
	private static ArrayList<Double> nt = new ArrayList<Double>();
	
	//用于存储TF/IDF归一化值的分子
	private ArrayList<Double> tfIdfUp = new ArrayList<Double>();
	//用于存储TF/IDF归一化值的分母
	private double tfIdfDown = 0;
	//用于存储TF/IDF归一化的值
	private ArrayList<Double> tfIdf = new ArrayList<Double>();
	

	
	//初始化函数
	public TFIDF(){
		for(int i = 0; i < TFIDF.featuresNum; i++){
			//初始化存放每篇文章特征词出现次数
			tf.add((double)0);
			//初始化在文本集中包含某个特征词的文本数
			nt.add((double)0);
			//初始化某个特征词TF/IDF归一化值的分子
			tfIdfUp.add((double)0);
			//初始化某个特征词TF/IDF归一化的值
			tfIdf.add((double)0);
		}
	}
	
	
	//返回文件类型
	public String getFileType() {
		return fileType;
	}
	//设置文件类型
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	//返回特征词总数量
	public static double getFeaturesNum() {
		return featuresNum;
	}
	//设置特征词总数量
	public static void setFeaturesNum(double featuresNum) {
		TFIDF.featuresNum = featuresNum;
	}

	//返回文章中每个特征词的tf值
	public ArrayList<Double> getTf() {
		return tf;
	}
	//返回文章中某个特征词的tf值
	public double getTf(int index) {
		return tf.get(index);
	}
	//添加文章中每个特征词的tf值,用于初始化
	public void addTf(double wordNum) {
		tf.add(wordNum);
	}
	//设置文章中每个特征词的tf值
	public void setTf(int index, double wordNum) {
		tf.set(index, wordNum);
	}
	
	
	//返回IDF:log(N/Nt+a)中的a
	public static double getA() {
		return a;
	}
	//设置IDF:log(N/Nt+a)中的a
	public static void setA(double a) {
		TFIDF.a = a;
	}
	
	
	//返回IDF:log(N/Nt+a)中的N，训练文本总数
	public static double getN() {
		return n;
	}
	//设置IDF:log(N/Nt+a)中的N，训练文本总数
	public static void setN(double n) {
		TFIDF.n = n;
	}
	
	
	//返回IDF:log(N/Nt+a)中的Nt,文本集中含有特征t的文本的数量
	public static ArrayList<Double> getNt() {
		return nt;
	}
	//返回IDF:log(N/Nt+a)中的Nt,文本集中含有某个特征t的文本的数量
	public static double getNt(int index) {
		return nt.get(index);
	}
	//添加IDF:log(N/Nt+a)中的Nt,文本集中含有特征t的文本的数量,用于初始化
	public static void addNt(double wordNum) {
		nt.add(wordNum);
	}	
	//设置IDF:log(N/Nt+a)中的Nt,文本集中含有特征t的文本的数量
	public static void setNt(int index, double wordNum) {
		nt.set(index, wordNum);
	}	
	
	//返回TF/IDF归一化值的分子
	public ArrayList<Double> getTfIdfUp() {
		return tfIdfUp;
	}
	//返回某个特征词TF/IDF归一化值的分子
	public double getTfIdfUp(int index) {
		return tfIdfUp.get(index);
	}
	//添加某个特征词TF/IDF归一化值的分子,用于初始化
	public void addTfIdfUp(double value) {
		tfIdfUp.add(value);
	}
	//计算某个特征词TF/IDF归一化值的分子
	public void countTfIdfUp() {
		for(int i = 0; i < TFIDF.featuresNum; i++){
			double value = (tf.get(i)) * Math.log(n/nt.get(i)+a);
			tfIdfUp.set(i, value);
		}
	}
	
	//返回TF/IDF归一化值的分母
	public double gettfIdfDown() {
		return tfIdfDown;
	}
	//计算TF/IDF归一化值的分母
	public void counttfIdfDown() {
		for(int i = 0; i < TFIDF.featuresNum; i++){
			tfIdfDown += Math.pow(tfIdfUp.get(i), 2);
		}
		tfIdfDown = Math.sqrt(tfIdfDown);
	}
	
	
	//返回每个特征词TF/IDF的值
	public ArrayList<Double> getTfIdf() {
		return tfIdf;
	}
	//返回某个特征词TF/IDF的值
	public double getTfIdf(int index) {
		return tfIdf.get(index);
	}
	//添加某个特征词TF/IDF的值,用于初始化
	public void addTfIdf(double value) {
		tfIdf.add(value); 
	}
	//计算每个特征词TF/IDF的值
	public void countTfIdf() {
		for(int i = 0;i < TFIDF.featuresNum; i++){
			double num = tfIdfUp.get(i)/(tfIdfDown + 0.00000001);
			BigDecimal bd = new BigDecimal(num);
			num = bd.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			tfIdf.set(i, num);
		}
	}

}
