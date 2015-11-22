package participant

case class ParticipantId(id: Int) extends AnyVal

case class Participant(id: ParticipantId, name: String)
