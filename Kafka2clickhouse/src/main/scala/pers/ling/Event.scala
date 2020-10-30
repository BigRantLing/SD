package pers.ling

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonProperty}

import scala.beans.BeanProperty

@JsonIgnoreProperties(ignoreUnknown = true)
case class Event(
                  @JsonProperty("timestamp") @BeanProperty timestamp: Long,
                  @JsonProperty("event_id") @BeanProperty eventId: String,
                  @JsonProperty("type") @BeanProperty `type`: Int,
                  @JsonProperty("buvid") @BeanProperty buvid: String) extends Serializable
