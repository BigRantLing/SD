package pers.ling

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object Main {
  def main(args: Array[String]): Unit = {
    //start kafka producer
    val producerThread = new Thread(new EventProducer(100000))
    producerThread.start()

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val consumerProperties = new Properties()
    consumerProperties.setProperty("bootstrap.servers", "localhost:9092");
    consumerProperties.setProperty("group.id", "flink_consumer_group");
    consumerProperties.setProperty("enable.auto.commit", "true");
    consumerProperties.setProperty("auto.commit.interval.ms", "1000");

    val source = new FlinkKafkaConsumer[String]("test", new SimpleStringSchema(), consumerProperties)

    env.addSource(source)
      .print()
    env.execute()

  }
}
