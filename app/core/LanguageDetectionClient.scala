package core

import play.api.libs.ws.WS
import com.typesafe.config.ConfigFactory
import scala.concurrent.Future
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

case class DetectionResponse(language: String)
case class LanguageDetection(language: String, isReliable: Boolean, confidence: Double)

class LanguageDetectionClient {

  lazy val key: String = ConfigFactory.load().getString("com.detectlanguage.ws.key")
  implicit val languageDetectionReads: Reads[LanguageDetection] = (
    (__ \ "language").read[String] and (__ \ "isReliable").read[Boolean] and (__ \ "confidence").read[Double])(LanguageDetection)

  def detect(text: String): Future[DetectionResponse] = {
    WS.url("http://ws.detectlanguage.com/0.2/detect").post(Map("key" -> Seq(key), "q" -> Seq(text))).map { response =>
      val detections = (response.json \ "data" \ "detections").as[Seq[LanguageDetection]]
      DetectionResponse(detections.maxBy(_.confidence).language)
    }
  }

}
