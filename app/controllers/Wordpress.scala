package controllers

import play.api.mvc.{Action, Controller}
import core.WordpressClient

import play.api.data.Forms._
import play.api.data.Form


object Wordpress extends Controller {

  private val wordpressClient = new WordpressClient

  val createForm = Form(
    tuple(
      "title" -> text, "content" -> text
    )
  )

  def create = Action { implicit request =>
    createForm.bindFromRequest.fold(
      formWithErrors => UnprocessableEntity,
      values => {
        wordpressClient.createPost(values._1, values._2)
        Redirect(routes.Wordpress.created)
    })
  }

  def created = Action {
    Ok(views.html.wordpress.index())
  }

}
