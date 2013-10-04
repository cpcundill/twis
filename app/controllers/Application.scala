package controllers

import play.api.mvc._
import play.libs.Akka

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
}