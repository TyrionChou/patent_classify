package cn.bistu.icdd.zh.util;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 用来计算信息增益的类
 */

public class Entropy {
	
	//分类总数
	private static double classNum = 0;
	//每一类的名称
	private static ArrayList<String> className = new ArrayList<String>();


	//每个类文章实例总数
	private static ArrayList<Double> batchNum = new ArrayList<Double>();
	//所有类文章实例总数
	private static double totalNum = 0;
	//计算出类别的熵
	private static double classentropy = 0;
	
	//单词名称
	private String wordName = "";

	//每一类中出现的的文章总数
	private ArrayList<Double> batchOccurredNum = new ArrayList<Double>();
	//单词出现文章总数
	private double totalOccurredNum = 0;
	//单词出现的概率
	private double wordOccurredProbability = 0;


	//单词出现后，每一个类发生的概率
	private ArrayList<Double> wordOccurredCProbability = new ArrayList<Double>();
	private ArrayList<Double> wordNotOccurredCProbability = new ArrayList<Double>();
	
	//信息增益
	private double iG = 0;
	
	
	public Entropy(){
		for(int i = 0; i < Entropy.classNum; i++){
			//拉普拉斯平滑
			
			batchOccurredNum.add((double)1);
			
			wordOccurredCProbability.add((double)0);
			
			wordNotOccurredCProbability.add((double)0);
		}
	}
	
	public static double getClassNum() {
		return classNum;
	}

	public static void setClassNum(double classNum) {
		Entropy.classNum = classNum;
	}
	
	
	public static ArrayList<String> getClassName() {
		return className;
	}
	
	public static String getClassName(int index) {
		return className.get(index);
	}
	
	public static void setClassName(int index, String className) {
		Entropy.className.add(index, className);
	}
	
	public static ArrayList<Double> getBatchsNum(){
		return batchNum;
	}
	
	public static void setBatchsNum(int index, double batchNum){
		Entropy.batchNum.add(index, batchNum);
	}
	
	public static double getTotalNum(){
		return totalNum;
	}
	
	public static void setTotalNum(){
		for(double i : batchNum){
			totalNum += i;
		}
	}
	
	public static double getClassEntropy(){
		return classentropy;
	}
	//保留四位小数
	public static void setClassEntropy(){
		double sum = 0;
		for(int i = 0; i < classNum; i++){
			sum += (batchNum.get(i) / totalNum) 
					* (Math.log(batchNum.get(i) / totalNum) / Math.log(2)); 
		}
		BigDecimal bd = new BigDecimal(sum);
		sum = bd.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue(); 
		classentropy = 0 - sum;
	}
	
	public String getWordName() {
		return wordName;
	}

	public void setWordName(String wordName) {
		this.wordName = wordName;
	}
	
	public ArrayList<Double> getBatchOccurredNum(){
		return batchOccurredNum;
	}
	
	public double getBatchOccurredNum(int index){
		return batchOccurredNum.get(index);
	}
	
	
	public void setBatchOccurredNum(int index, double batchOccurredNum){
		this.batchOccurredNum.set(index, batchOccurredNum);
	}
	
	public double getTotalOccurredNum(){
		return totalOccurredNum;
	}
	
	public void setTotalOccurredNum(){
		for(double i : batchOccurredNum){
			totalOccurredNum += i;
		}
	}
	
	public double getWordOccurredProbability() {
		return wordOccurredProbability;
	}
	//保留四位小数
	public void setWordOccurredProbability() {
		wordOccurredProbability = totalOccurredNum / Entropy.totalNum;
		BigDecimal bd = new BigDecimal(wordOccurredProbability);
		wordOccurredProbability = bd.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue(); 
	}
	
	public ArrayList<Double> getWordOccurredCProbability() {
		return wordOccurredCProbability;
	}
	//保留四位小数
	public void setWordOccurredCProbability() {
		double num = 0;
		for(int i = 0; i < Entropy.classNum; i++){
			num = (batchOccurredNum.get(i) / totalOccurredNum);
			BigDecimal bd = new BigDecimal(num);
			num = bd.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			wordOccurredCProbability.set(i, num);
		}
	}
	
	public ArrayList<Double> getWordNotOccurredCProbability() {
		return this.wordNotOccurredCProbability;
	}
	//保留四位小数
	public void setWordNotOccurredCProbability() {
		double num = 0;
		for(int i = 0; i < Entropy.classNum; i++){
			num = ((Entropy.batchNum.get(i) - batchOccurredNum.get(i)) 
					/ (Entropy.totalNum - totalOccurredNum));
			BigDecimal bd = new BigDecimal(num);
			num = bd.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			wordNotOccurredCProbability.set(i, num);
		}
	}
	
	public Double getIG() {
		return iG;
	}
	
	//保留四位小数
	public void setIG() {
		
		double sum1 = 0,sum2 = 0;
		for(int i = 0; i < Entropy.classNum; i++){
			sum1 += wordOccurredCProbability.get(i)
					* (Math.log(wordOccurredCProbability.get(i)) / Math.log(2)); 

			sum2 += wordNotOccurredCProbability.get(i)
					* (Math.log(wordNotOccurredCProbability.get(i)) / Math.log(2)); 
		}
		iG = Entropy.classentropy + wordOccurredProbability * sum1 
				+ (1-wordOccurredProbability) * sum2;
		BigDecimal bd = new BigDecimal(iG);
		iG = bd.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue(); 
	}
	
	
}
