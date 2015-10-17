package story

case class CharacterId(id: Int) extends AnyVal

case class Character(id: CharacterId, name: String)
