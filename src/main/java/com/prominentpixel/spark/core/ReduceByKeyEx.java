package com.prominentpixel.spark.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class ReduceByKeyEx {

	public static void main(String[] args) {

		// Input Path
		String inputPath = "/media/ubuntu/data/ml-sample-data/employee-incentive.tsv";

		SparkConf configuration = new SparkConf();

		configuration.setAppName("ReduceByKey Example");

		configuration.setMaster("local[2]");

		JavaSparkContext context = new JavaSparkContext(configuration);

		JavaRDD<String> lines = context.textFile(inputPath);
		
		JavaPairRDD<String,Integer> pairs = lines.mapToPair(new PairFunction<String, String, Integer>() {

			private static final long serialVersionUID = -2583909414682054117L;

			@Override
			public Tuple2<String, Integer> call(String input) throws Exception {

				String[] values = input.split("\t");
				return new Tuple2<String,Integer>(values[0],Integer.parseInt(values[1]));
			}
		
		});
		
		JavaPairRDD<String,Integer> result = pairs.reduceByKey(new Function2<Integer,Integer,Integer>(){

			private static final long serialVersionUID = 8298427813777158309L;

			@Override
			public Integer call(Integer val1, Integer val2) throws Exception {
				return val1+val2;
			}

		});
		
/*		We can actually implement word count even faster by using the
		countByValue() function on the first RDD: input.flatMap(x =>
		x.split(" ")).countByValue().
*/
		
		System.out.println(result.collect());
		
		result.saveAsTextFile("/home/ubuntu/Desktop/SparkOutput/Aggregated-Incentive");

		context.stop();
		context.close();

	}
}
