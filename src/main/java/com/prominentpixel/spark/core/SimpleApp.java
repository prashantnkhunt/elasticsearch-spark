package com.prominentpixel.spark.core;

import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;

public class SimpleApp {

	static class Contains implements Function<String, Boolean>{
		String query;

		public Contains(String query){
			this.query = query;
		}

		@Override
		public Boolean call(String input) throws Exception {
			return input.contains(query);
		}
	}

	public static void main(String[] args){

		String wordFile = args[0]; // Should be some file on your system

		SparkConf sparkConf = new SparkConf().setAppName("Word Contains App");

		sparkConf.setMaster("local[2]");
		
		JavaSparkContext sc = new JavaSparkContext(sparkConf);

		JavaRDD<String> logData = sc.textFile(wordFile);
		
		long numAs = logData.filter(new Contains("a")).count();

		long numBs = logData.filter(new Contains("b")).count();

		logData.saveAsTextFile(args[1]);

		System.out.println("Lines with a: " + numAs + ", lines with b: "
				+ numBs);

		sc.stop();
	}
}
