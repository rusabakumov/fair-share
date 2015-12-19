package participant

import java.util.UUID

import event.{ Version, Metadata, Event }
import participant.ParticipantPayload.Created
import util._
import util.syntax.repo._

import scalaz._

trait ParticipantService {
  def create(name: String): Reader[ParticipantRepo, V[ParticipantId]] = Reader { repo =>
    val id = ParticipantId(UUID.randomUUID())
    val event: Event[Participant] = Event(Created(id, name), Metadata(Version.zero))

    repo.storeV(id, event).map(_ => id)
  }
}
