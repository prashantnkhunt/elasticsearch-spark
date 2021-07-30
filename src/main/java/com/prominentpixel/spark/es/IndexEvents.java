package com.prominentpixel.spark.es;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spark_project.guava.collect.ImmutableList;
import scala.Tuple2;

import java.util.List;
import java.util.Map;

public class IndexEvents {

    private static final Logger logger = LoggerFactory.getLogger(IndexEvents.class.getName());

    public static void main(String[] args) {

        if(args.length==3){
            try{
                SparkConf sparkConf = new SparkConf().setAppName("Apache Spark ElasticSearch connection.");

                sparkConf.set("es.port",args[0]);
                sparkConf.set("es.nodes",args[1]);
                sparkConf.set("es.nodes.wan.only",args[2]);

                //TODO: Master URL : either set it here OR specify like, -Dspark.master=spark://myhost:7077
                sparkConf.setMaster("local[*]");
                JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

//                indexEvent(javaSparkContext);
                getESIndexData(javaSparkContext);

                javaSparkContext.stop();
                javaSparkContext.close();
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }else{
            logger.error("Please specify 3 arguments like: <ES_PORT> <ES_NODES_URL> <ES_NODES_WAN_ONLY_FLAG>");
            System.exit(-1);
        }
    }

    private static void indexEvent(JavaSparkContext javaSparkContext) {
        Event eventRajkot = new Event(1,"Ganesh nagar","Gundala road","gondal");
        Event eventAhmedabad = new Event(2,"Buniyadi school","Near main gate","Moviya");

        JavaRDD<Event> eventJavaRDD = javaSparkContext.parallelize(ImmutableList.of(eventRajkot,eventAhmedabad));
        JavaEsSpark.saveToEs(eventJavaRDD,"events_2");
    }

    private static void getESIndexData(JavaSparkContext javaSparkContext){
        JavaPairRDD<String, Map<String, Object>> esRDD = JavaEsSpark.esRDD(javaSparkContext,"events_2");

        List<Tuple2<String,Map<String,Object>>> topTenRecords=esRDD.take(10);
        for(Tuple2<String,Map<String,Object>> record : topTenRecords){
            logger.info("****RECORD****::<{}>",record);
        }
    }
}
