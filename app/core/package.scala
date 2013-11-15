package object core {

  type TweetId = Long
  type TweetLinkId = Long

  type TweetWithLinks = (Tweet, Seq[TweetLink])

}
