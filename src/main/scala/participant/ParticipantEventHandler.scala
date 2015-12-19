package participant

import event.{ Event, EventHandler }

class ParticipantEventHandler extends EventHandler[Participant] {
  def handle(agg: Participant, event: Event[Participant]): Participant = {
    val Event(payload, metadata) = event

    payload match {
      case ParticipantPayload.Created(id, name) => agg.copy(id = id, name = name)
      case ParticipantPayload.NameChanged(id, name) => agg.copy(name = name)
    }
  }
}
