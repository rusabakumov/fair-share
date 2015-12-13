package participant

import java.util.UUID

import participant.ParticipantEvent.Created
import repos.ParticipantRepo
import util._

import scalaz._

trait ParticipantService {
  def create(name: String): Reader[ParticipantRepo, V[ParticipantId]] = Reader { repo =>
    val id = ParticipantId(UUID.randomUUID())
    repo.store.run(id, Created(id, name)).map(_ => id)
  }
}
