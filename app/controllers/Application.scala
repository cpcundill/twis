package controllers

import play.api.mvc._
import play.libs.Akka

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def tweets = Action {
    val tweetReader = Akka.system().actorFor("akka://application/user/tweetReader")
    tweetReader ! "go-find-em" // Don't care for now
    Ok(views.html.index("Your new application is ready."))
  }
  
}