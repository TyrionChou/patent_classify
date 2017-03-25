package cn.bistu.icdd.zh.util;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 用来计算信息增益的类
 */

public class Entropy {
	
	//分类类别总数
	private static double classNum = 0;
	//每一类别的名称
	private static ArrayList<String> className = new ArrayList<String>();
	//每个类文章实例总数
	private static ArrayList<Double> batchNum = new ArrayList<Double>();
	//所有类文章实例总数
	private static double totalNum = 0;
	//不加入特征词类别初始熵
	private static double classentropy = 0;
	
	
	//单词名称
	private String wordName = "";
	//每一类中出现的文章总数
	private ArrayList<Double> batchOccurredNum = new ArrayList<Double>();
	//单词出现在所有的文章总数
	private double totalOccurredNum = 0;
	
	//单词出现的概率
	private double wordOccurredProbability = 0;
	//单词出现后，每一个类发生的概率
	private ArrayList<Double> wordOccurredCProbability = new ArrayList<Double>();
	//单词未出现后，每一个类发生的概率
	private ArrayList<Double> wordNotOccurredCProbability = new ArrayList<Double>();
	
	//信息增益
	private double iG = 0;
	
	//初始化函数
	public Entropy(){
		for(int i = 0; i < Entropy.classNum; i++){
			//初始化单词每一类中出现的次数，利用拉普拉斯平滑的理论，将其每个类中出现的次数初始化为1，防止OccurredCProbability=0
			batchOccurredNum.add((double)1);
			//初始化单词出现后，每一个类发生的概率
			wordOccurredCProbability.add((double)0);
			//初始化单词未出现后，每一个类发生的概率
			wordNotOccurredCProbability.add((double)0);
		}
	}
	//返回分类类别总数
	public static double getClassNum() {
		return classNum;
	}
	//设置分类类别总数
	public static void setClassNum(double classNum) {
		Entropy.classNum = classNum;
	}
	
	
	//返回分类类别名称的ArrayList<String>
	public static ArrayList<String> getClassName() {
		return className;
	}
	//返回与index相对应的分类类别名称
	public static String getClassName(int index) {
		return className.get(index);
	}
	//添加分类类别名称
	public static void addClassName(int index, String className) {
		Entropy.className.add(index, className);
	}
	//设置分类类别名称
	public static void setClassName(int index, String className) {
		Entropy.className.set(index, className);
	}

	
	//返回每个类文章实例总数的ArrayList<Double>
	public static ArrayList<Double> getBatchsNum(){
		return batchNum;
	}
	//添加每个类文章实例总数
	public static void addBatchsNum(int index, double batchNum){
		Entropy.batchNum.add(index, batchNum);
	}
	//设置每个类文章实例总数
	public static void setBatchsNum(int index, double batchNum){
		Entropy.batchNum.set(index, batchNum);
	}
	
	
	//返回所有类文章实例总数
	public static double getTotalNum(){
		return totalNum;
	}
	//计算所有类文章实例总数
	public static void countTotalNum(){
		for(double i : batchNum){
			totalNum += i;
		}
	}
	
	
	//返回类别初始熵
	public static double getClassEntropy(){
		return classentropy;
	}
	//计算类别初始熵，保留四位小数
	public static void countClassEntropy(){
		double sum = 0;
		for(int i = 0; i < classNum; i++){
			sum += (batchNum.get(i) / totalNum) 
					* (Math.log(batchNum.get(i) / totalNum) / Math.log(2)); 
		}
		BigDecimal bd = new BigDecimal(sum);
		sum = bd.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue(); 
		classentropy = 0 - sum;
	}
	
	
	//返回单词名称
	public String getWordName() {
		return wordName;
	}
	//设置单词名称
	public void setWordName(String wordName) {
		this.wordName = wordName;
	}
	
	
	//返回每一类中出现单词的文章总数
	public ArrayList<Double> getBatchOccurredNum(){
		return batchOccurredNum;
	}
	//根据index返回相应类中出现单词的文章次数
	public double getBatchOccurredNum(int index){
		return batchOccurredNum.get(index);
	}
	//设置每一类中出现单词的文章总数
	public void setBatchOccurredNum(int index, double batchOccurredNum){
		this.batchOccurredNum.set(index, batchOccurredNum);
	}
	
	
	//返回单词出现在所有的文章总数
	public double getTotalOccurredNum(){
		return totalOccurredNum;
	}
	//计算单词出现在所有的文章总数
	public void countTotalOccurredNum(){
		for(double i : batchOccurredNum){
			totalOccurredNum += i;
		}
	}
	
	
	//返回单词出现的概率
	public double getWordOccurredProbability() {
		return wordOccurredProbability;
	}
	//计算单词出现的概率，保留四位小数
	public void countWordOccurredProbability() {
		//用单词出现文章总数/所有文章总数
		wordOccurredProbability = totalOccurredNum / Entropy.totalNum;
		//保留四位小数
		BigDecimal bd = new BigDecimal(wordOccurredProbability);
		wordOccurredProbability = bd.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue(); 
	}
	
	
	//返回单词出现后，每一个类发生的概率的ArrayList<Double>
	public ArrayList<Double> getWordOccurredCProbability() {
		return wordOccurredCProbability;
	}
	//计算单词出现后，每一个类发生的概率，保留四位小数
	public void countWordOccurredCProbability() {
		double num = 0;
		//每一类中单词出现的数量/单词出现文章总数
		for(int i = 0; i < Entropy.classNum; i++){
			num = (batchOccurredNum.get(i) / totalOccurredNum);
			BigDecimal bd = new BigDecimal(num);
			num = bd.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			wordOccurredCProbability.set(i, num);
		}
	}
	
	
	//返回单词未出现后，每一个类发生的概率的ArrayList<Double>
	public ArrayList<Double> getWordNotOccurredCProbability() {
		return this.wordNotOccurredCProbability;
	}
	//计算单词未出现后，每一个类发生的概率，保留四位小数
	public void countWordNotOccurredCProbability() {
		double num = 0;
		//（每一类单词未出现的文章数/未出现单词的文章总数）
		for(int i = 0; i < Entropy.classNum; i++){
			num = ((Entropy.batchNum.get(i) - batchOccurredNum.get(i)) 
					/ (Entropy.totalNum - totalOccurredNum));
			BigDecimal bd = new BigDecimal(num);
			num = bd.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			wordNotOccurredCProbability.set(i, num);
		}
	}
	
	//返回信息增益值
	public Double getIG() {
		return iG;
	}
	
	//计算信息增益值，保留四位小数，计算方法具体见公式
	public void countIG() {
		double sum1 = 0,sum2 = 0;
		for(int i = 0; i < Entropy.classNum; i++){
			sum1 += wordOccurredCProbability.get(i)
					* (Math.log(wordOccurredCProbability.get(i)) / Math.log(2)); 

			sum2 += wordNotOccurredCProbability.get(i)
					* (Math.log(wordNotOccurredCProbability.get(i)) / Math.log(2)); 
		}
		
		iG = Entropy.classentropy + wordOccurredProbability * sum1 
				+ (1-wordOccurredProbability) * sum2;
		
		System.out.println(iG);
		BigDecimal bd = new BigDecimal(iG);
		iG = bd.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue(); 
	}
	
	
}
