package core

import org.specs2.mutable.Specification

class DetectLanguageClientIntegrationTest extends Specification {

  val client = new LanguageDetectionClient

  "The Detect Language Client" should {

    // Requires sys prop com.detectlanguage.ws.key

    "detect English" in {
      val response = client.detect("This is an English tweet.  Coming right at ya!")
      response must be_==(DetectionResponse("en")).await
    }

    "detect German" in {
      val response = client.detect("Dies ist ein englischer tweet. Kommen zu Ihnen!")
      response must be_==(DetectionResponse("de")).await
    }

    "detect Japanese as predominant although mixed with some English" in {
      val response = client.detect("@functional2ch: Scalaスレ 2013/11/19(火) 16:02:28.23 \n FXで死んだらあり金溶かすだけじゃ済まないけどな \n2ch to RSS http://bit.ly/17GQOST #scala #2ch")
      response must be_==(DetectionResponse("ja")).await
    }

  }

}
