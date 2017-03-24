package cn.bistu.icdd.zh.test;
import java.io.IOException;

import cn.bistu.icdd.zh.service.svm_predict;
import cn.bistu.icdd.zh.service.svm_train;

public interface ComMain {
	public static void main(String[] args){
		String[] arg = {"./source/train.txt", "./source/model.txt"};
		String[] parg = {"./source/test.txt", "./source/model.txt", "./source/output.txt"};
		System.out.println("开始运行!");
		svm_train t = new svm_train();
		svm_predict p = new svm_predict();
		try {
			t.main(arg);
			p.main(parg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("结束运行!");
		
	}
}
