package occasion

import event.{ Event, Version }
import org.scalatest.{ FunSpec, Matchers }

import scala.collection.mutable
import scalaz._
import scalaz.syntax.either._

class OccasionServiceTest extends FunSpec with Matchers {
  object TestOccasionRepo extends OccasionRepo {
    val inMemoryStore = mutable.Map[OccasionId, List[Event[OccasionEvent]]]()

    def get(id: OccasionId): String \/ Option[Occasion] = {
      val maybeEvents = inMemoryStore.get(id)

      val maybeOccasion = for {
        events <- maybeEvents
      } yield Occasion.foldLeft(events)

      maybeOccasion.right
    }

    def store(id: OccasionId, ev: Event[OccasionEvent]): String \/ Unit = inMemoryStore.synchronized {
      val newEvents = inMemoryStore.getOrElse(id, Nil) :+ ev
      inMemoryStore += (id -> newEvents)
      ().right
    }
  }

  object TestOccasionService extends OccasionService

  describe("OccasionServiceTest") {
    it("should change description") {
      val id = TestOccasionService.create()(TestOccasionRepo).toOption.get
      TestOccasionService.changeDescription(id, Version(0), "new description")(TestOccasionRepo)

      TestOccasionRepo.get(id).toOption.get.get.description shouldEqual Some("new description")
    }

  }
}
