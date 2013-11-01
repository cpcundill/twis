import org.specs2.mutable.Specification
import scala.concurrent.Future
import spray.client.pipelining._
import spray.http.HttpRequest
import spray.http.HttpResponse

class SprayClientIntegrationTest extends Specification {

  "The Spray Client" should {

    "get the spray home page" in {
      val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
      val response: Future[HttpResponse] = pipeline(Get("http://spray.io/"))
    }

  }

}
