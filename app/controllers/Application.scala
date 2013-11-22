package controllers

import play.api.mvc._
import securesocial.core.SecureSocial
import core.ScalaBlogMan

object Application extends Controller with SecureSocial {

  def index = SecuredAction(ScalaBlogMan) { implicit request =>
    Ok(views.html.index())
  }
  
}