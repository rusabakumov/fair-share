package participant

import scalaz.concurrent.Task

trait ParticipantRepo {
  def get(id: ParticipantId): Task[Participant] = ???
}
