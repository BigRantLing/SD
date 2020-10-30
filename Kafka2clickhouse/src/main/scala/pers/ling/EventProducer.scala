package pers.ling

import java.util.{Properties, UUID}

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.util.concurrent.RateLimiter
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}

import scala.util.Random

class EventProducer(val tpsLimit: Long) extends Runnable{

  val topic = "test"
  val brokerList = "localhost:9092"
  val properties = new Properties()

  properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  properties.put("bootstrap.servers", brokerList)

  val producer: Producer[String, String] = new KafkaProducer[String, String](properties)
  val random = new Random()
  val mapper = new ObjectMapper()
  var rateLimiter: Option[RateLimiter] = {
    tpsLimit match {
      case 0 => None
      case _ => Some(RateLimiter.create(tpsLimit))
    }
  }


  override def run(): Unit = {
    var counter: Int = 0
    while(true) {

      rateLimiter match {
        case Some(limiter) => limiter.acquire(1)
        case None => {}
      }

      counter += 1
      val event = Event(System.currentTimeMillis(), "event_" + random.nextInt(100), random.nextInt(10).toInt, UUID.randomUUID().toString)
      val record: ProducerRecord[String, String] = new ProducerRecord[String, String](topic, mapper.writeValueAsString(event))
      producer.send(record)

      if(counter == 100000) {
        println(s"$counter records have been written")
        counter = 0
      }
    }
  }
}
