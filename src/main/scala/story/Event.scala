package story

case class EventId(id: Int) extends AnyVal

case class Event(id: EventId, description: Option[String])
