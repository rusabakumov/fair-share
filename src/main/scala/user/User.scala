package user

case class UserId(id: Int) extends AnyVal

case class User(id: UserId, name: String)
