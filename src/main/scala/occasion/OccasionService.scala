package occasion

import java.time.LocalDateTime

import event.{ Event, Metadata }

import scalaz.Reader

trait OccasionService {
  def changeDescription(id: OccasionId, newDescription: String): Reader[OccasionRepo, Boolean] = Reader { repo =>
    val ev = Event(OccasionDescriptionChanged(newDescription), Metadata(LocalDateTime.now()))
    repo.store(id, ev)
  }

  def createOccasion(id: OccasionId): Reader[OccasionRepo, Boolean] = Reader { repo =>
    val ev = Event(OccasionCreated(id), Metadata(LocalDateTime.now()))
    repo.store(id, ev)
  }
}
