package com.prominentpixel.spark.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.Function;

public class FilterExample {

	static class ContainsFunction implements Function<String,Boolean>{

		private String query;
		
		public ContainsFunction(String query){
			this.query = query;
		}
		
		@Override
		public Boolean call(String input) throws Exception {
			return input.contains(query);
		}
		
	}
	
	public static void main(String[] args) {
		
		SparkConf sparkConf = new SparkConf().setAppName("Filter Sample Example");
		
		//Setting Master for running it from IDE.
		sparkConf.setMaster("local[2]");

		JavaSparkContext sc = new JavaSparkContext(sparkConf);

		JavaRDD<String> inputRdd = sc.textFile(args[0]);
		JavaRDD<String> infoRdd = inputRdd.filter(new ContainsFunction("INFO"));
		JavaRDD<String> errorRdd = inputRdd.filter(new ContainsFunction("ERROR"));
		JavaRDD<String> warningRdd = inputRdd.filter(s -> s.contains("WARN"));

		JavaRDD<String> resultRdd = errorRdd.union(infoRdd);
		resultRdd.saveAsTextFile(args[1]);

		warningRdd.saveAsTextFile(args[2]);
		
		sc.stop();
		sc.close();
	}
}
