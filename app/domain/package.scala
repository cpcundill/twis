
package object domain {

  type TweetId = Long
  type TweetLinkId = Long
  type TweetWithLinks = (Tweet, List[TweetLink])

  type UserId = String
  type UserPermissionId = Long
  type UserWithPermissions = (User, List[UserPermission])
}
