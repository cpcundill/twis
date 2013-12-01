package core

import net.bican.wordpress.{Page, Wordpress}
import com.typesafe.config.ConfigFactory

class WordpressClient {

  private val config = ConfigFactory.load()
  private val wordpress = new Wordpress(config.getString("wordpress.username"), config.getString("wordpress.password"), config.getString("wordpress.xmlRpcUrl"))
  private val publishNewPosts = false

  def createPost(title: String, content: String): Unit = {
    val newPost = new Page()
    newPost.setTitle(title); newPost.setDescription(content)
    wordpress.newPost(newPost, publishNewPosts)
  }

}
