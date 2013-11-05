package core

import net.bican.wordpress.{Page, Wordpress}
import com.typesafe.config.ConfigFactory

class WordpressClient {

  private val config = ConfigFactory.load("wordpress")
  private val wordpress = new Wordpress(config.getString("user"), config.getString("password"), config.getString("url"))
  private val publishNewPosts = false

  def createPost(title: String, content: String) = {
    val newPost = new Page()
    newPost.setTitle(title); newPost.setDescription(content)
    wordpress.newPost(newPost, publishNewPosts)
  }

}
