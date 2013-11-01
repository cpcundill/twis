package core

import net.bican.wordpress.{Page, Wordpress}

class WordpressClient {

  val wordpress = new Wordpress("Chris Cundill", "coffeecake84", "http://www.cakesolutions.net/teamblogs/xmlrpc.php")

  def createPost(title: String, content: String) = {
    val newPost = new Page()
    newPost.setTitle(title); newPost.setDescription(content)
    wordpress.newPost(newPost, false)
  }

}
