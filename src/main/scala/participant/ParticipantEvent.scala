package participant

sealed trait ParticipantEvent

object ParticipantEvent {
  case class Created(id: ParticipantId, name: String) extends ParticipantEvent

  case class NameChanged(name: String) extends ParticipantEvent
}
