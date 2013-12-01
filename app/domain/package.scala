
package object domain {

  type TweetId = Long
  type TweetLinkId = Long
  type TweetWithLinks = (Tweet, List[TweetLink])
}
