package occasion

import java.util.UUID
import java.util.concurrent.atomic.AtomicReference

import event.Event
import org.scalatest.{ FunSpec, Matchers }

class OccasionServiceTest extends FunSpec with Matchers {
  object TestOccasionRepo extends OccasionRepo {
    val store = new AtomicReference(Map[OccasionId, List[Event[OccasionEvent]]]())

    def get(id: OccasionId): Option[Occasion] = {
      val maybeEvents = store.get().get(id)

      for {
        events <- maybeEvents
      } yield Occasion.foldLeft(events.map(_.payload))
    }

    def store(id: OccasionId, ev: Event[OccasionEvent]): Boolean = {
      val current = store.get()
      val newEvents = current.getOrElse(id, Nil) :+ ev
      store.set(current + (id -> newEvents))
      true
    }
  }

  object TestOccasionService extends OccasionService

  describe("OccasionServiceTest") {
    it("should change description") {
      val id = OccasionId(UUID.randomUUID())
      TestOccasionService.createOccasion(id)(TestOccasionRepo)
      TestOccasionService.changeDescription(id, "new description")(TestOccasionRepo)

      TestOccasionRepo.get(id).get.description shouldEqual Some("new description")
    }

  }
}
