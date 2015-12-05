package participant

import util._

import scalaz.Kleisli

trait ParticipantRepo {
  def get: Kleisli[V, ParticipantId, Option[Participant]] = Kleisli[V, ParticipantId, Option[Participant]] { id =>
    get(id)
  }

  protected def get(id: ParticipantId): V[Option[Participant]]
}
