package core

import twitter4j.{QueryResult, Query, TwitterFactory}

class TwitterClient(val pageSize: Int = 100) {

  def search(query: String): QueryResult = {
    search(query, 0, pageSize)
  }

  def search(query: String, minId: Long): QueryResult = {
    search(query, minId, pageSize)
  }

  def search(query: String, minId: Long, pageSize: Int): QueryResult = {
    val twitter = TwitterFactory.getSingleton()
    val twitterQuery = new Query(query)
    twitterQuery.setCount(pageSize)
    twitterQuery.setSinceId(minId)
    val result = twitter.search(twitterQuery)
    result
  }

}
