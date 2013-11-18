package core

import twitter4j.{Status, QueryResult, Query, TwitterFactory}

class TwitterClient {

  def search(query: String, minId: Long = 0, pageSize: Int = 100): QueryResult = {
    val twitter = TwitterFactory.getSingleton()
    val twitterQuery = new Query(query)
    twitterQuery.setCount(pageSize)
    twitterQuery.setSinceId(minId)
    val result = twitter.search(twitterQuery)
    result
  }

  def read(tweetId: Long): Status = {
    val twitter = TwitterFactory.getSingleton()
    twitter.showStatus(tweetId)
  }

}
