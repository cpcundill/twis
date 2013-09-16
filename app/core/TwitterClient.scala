package core

import twitter4j.{QueryResult, Query, TwitterFactory}

class TwitterClient(val pageSize: Int = 100) {

  def search(query: String): QueryResult = {
    search(query, 0, pageSize)
  }

  def search(query: String, minId: Int): QueryResult = {
    search(query, minId, pageSize)
  }

  def search(query: String, minId: Int, pageSize: Int): QueryResult = {
    val twitter = TwitterFactory.getSingleton()
    val result = twitter.search(new Query(query))
    result
  }

}
