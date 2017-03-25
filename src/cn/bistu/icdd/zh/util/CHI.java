package cn.bistu.icdd.zh.util;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 用来计算CHI值的类
 */

public class CHI {
	
	//分类类别总数
	private static double classNum = 0;
	//每一类别的名称
	private static ArrayList<String> className = new ArrayList<String>();
	//每个类文本实例总数
	private static ArrayList<Double> batchNum = new ArrayList<Double>();
	//所有类文本实例总数
	private static double totalNum = 0;
	
	
	//单词名称
	private String wordName = "";
	//每一类中出现的文本总数
	private ArrayList<Double> batchOccurredNum = new ArrayList<Double>();
	//单词出现在所有的文本总数
	private double totalOccurredNum = 0;
	
	//记录包含单词ti属于ci类中文本的频数
	private ArrayList<Double> a = new ArrayList<Double>();
	//记录包含单词ti不属于ci类中文本的频数
	private ArrayList<Double> b = new ArrayList<Double>();
	//记录不包含单词ti属于ci类中文本的频数
	private ArrayList<Double> c = new ArrayList<Double>();
	//记录不包含单词ti不属于ci类中文本的频数
	private ArrayList<Double> d = new ArrayList<Double>();
	
	//记录每一个类的chi值
	private ArrayList<Double> chi = new ArrayList<Double>();
	//记录单词ti最大的chi值
	private double maxChi = 0;
	
	//初始化函数
	public CHI(){
		for(int i = 0; i < CHI.classNum; i++){
			//初始化单词每一类中出现的次数
			batchOccurredNum.add((double)0);
			//初始化包含单词ti属于ci类中文本的频数
			a.add((double)0);
			//初始化包含单词ti不属于ci类中文本的频数
			b.add((double)0);
			//初始化不包含单词ti属于ci类中文本的频数
			c.add((double)0);
			//初始化不包含单词ti不属于ci类中文本的频数
			d.add((double)0);
			//初始化每一个类的chi值
			chi.add((double)0);
		}
	}
	
	
	//返回分类类别总数
	public static double getClassNum() {
		return classNum;
	}
	//设置分类类别总数
	public static void setClassNum(double classNum) {
		CHI.classNum = classNum;
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
		CHI.className.add(index, className);
	}
	//设置分类类别名称
	public static void setClassName(int index, String className) {
		CHI.className.set(index, className);
	}
	
	
	
	//返回每个类文章实例总数的ArrayList<Double>
	public static ArrayList<Double> getBatchsNum(){
		return batchNum;
	}
	//添加每个类文章实例总数
	public static void addBatchsNum(int index, double batchNum){
		CHI.batchNum.add(index, batchNum);
	}
	//设置每个类文章实例总数
	public static void setBatchsNum(int index, double batchNum){
		CHI.batchNum.set(index, batchNum);
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
	
	
	//返回包含单词ti属于每一个类中文本的频数的ArrayList
	public ArrayList<Double> getA() {
		return a;
	}
	//返回包含单词ti属于某一ci类中文本的频数
	public double getA(int index) {
		return a.get(index);
	}
	//添加包含单词ti属于ci类中文本的频数，用于初始化
	public void addA(double num) {
		a.add(num);
	}
	//计算包含单词ti属于某一ci类中文本的频数
	public void countA() {
		for(int i = 0; i < CHI.classNum; i++){
			a.set(i, batchOccurredNum.get(i));
		}
	}
	
	
	//返回包含单词ti不属于每一个类中文本的频数的ArrayList
	public ArrayList<Double> getB() {
		return b;
	}
	//返回包含单词ti不属于某一ci类中文本的频数
	public double getB(int index) {
		return b.get(index);
	}
	//添加包含单词ti不属于ci类中文本的频数，用于初始化
	public void addB(double num) {
		b.add(num);
	}
	//计算包含单词ti不属于某一ci类中文本的频数
	public void countB() {
		for(int i = 0; i < CHI.classNum; i++){
			b.set(i, totalOccurredNum - batchOccurredNum.get(i));
		}
	}
	
	
	//返回不包含单词ti属于每一个类中文本的频数的ArrayList
	public ArrayList<Double> getC() {
		return c;
	}
	//返回不包含单词ti属于某一个ci类中文本的频数
	public double getC(int index) {
		return c.get(index);
	}
	//添加包含单词ti不属于ci类中文本的频数，用于初始化
	public void addC(double num) {
		c.add(num);
	}
	//计算不包含单词ti属于某一个ci类中文本的频数
	public void countC() {
		for(int i = 0; i < CHI.classNum; i++){
			c.set(i, batchNum.get(i) - batchOccurredNum.get(i));
		}
	}
	
	
	//返回不包含单词ti不属于每一个ci类中文本的频数的ArrayList
	public ArrayList<Double> getD() {
		return d;
	}
	//返回不包含单词ti不属于某一ci类中文本的频数
	public double getD(int index) {
		return d.get(index);
	}
	//添加不包含单词ti不属于某一ci类中文本的频数，用于初始化
	public void addD(double num) {
		d.add(num);
	}
	//计算不包含单词ti不属于某一ci类中文本的频数
	public void countD() {
		for(int i = 0; i < CHI.classNum; i++){
			d.set(i,CHI.totalNum - batchNum.get(i) - totalOccurredNum - batchOccurredNum.get(i));
		}
	}
	
	
	//返回ti对于每一个类的chi值
	public ArrayList<Double> getChi() {
		return chi;
	}
	//计算ti对于每一个类的chi值
	public void countChi() {
		double num = 0;
		for(int i = 0; i < CHI.classNum; i++){
			num = (CHI.totalNum * Math.pow(a.get(i) * d.get(i) - c.get(i) * b.get(i), 2))/
					((a.get(i) + c.get(i)) * (b.get(i) + d.get(i)) * (a.get(i) + b.get(i)) *(c.get(i) + d.get(i)));
			BigDecimal bd = new BigDecimal(num);
			num = bd.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			chi.set(i, num);
		}
	}
	
	
	//返回ti单词ti最大的chi值
	public double getMaxChi() {
		return maxChi;
	}
	//计算ti单词ti最大的chi值
	public void countMaxChi() {
		for(int i = 0; i < CHI.classNum; i++){
			if(chi.get(i) > maxChi){
				maxChi = chi.get(i);
			}
		}
	}
	
}
