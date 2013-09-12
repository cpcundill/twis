package core

import twitter4j.{QueryResult, Query, TwitterFactory, Twitter}

class TwitterClient {

  def search(query: String) = {
    val twitter = TwitterFactory.getSingleton()
    val result = twitter.search(new Query(query))
    result
  }

}
