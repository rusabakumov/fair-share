package participant

import java.util.UUID

import event.{ Version, Metadata, Event }
import participant.ParticipantPayload.Created
import util._
import util.syntax.repo._
import scalaz.syntax.either._

import scalaz._

trait ParticipantService {
  def create(name: String): Reader[ParticipantRepo, V[ParticipantId]] = Reader { repo =>
    val id = ParticipantId(UUID.randomUUID())
    val event: Event[Participant] = Event(Created(id, name), Metadata(Version.zero))

    repo.storeV(id, event).map(_ => id)
  }

  def changeName(
    id: ParticipantId,
    version: Version,
    aName: String
  ): Reader[ParticipantRepo, V[Unit]] = Reader { repo =>
    repo.getK
      .andThen(validateVersion(version))
      .andThen(changeName(aName))
      .andThen(repo.storeK)
      .run(id)
  }

  def validateVersion(version: Version) = Kleisli[V, Participant, Participant] { participant =>
    if (version != participant.version) "You're working with an outdated participant.".left[Participant]
    else participant.right
  }

  def changeName(aName: String) = Kleisli[V, Participant, (ParticipantId, Event[Participant])] { project =>
    (project.id, ParticipantCommandHandler.changeName(project, aName)).right
  }
}
