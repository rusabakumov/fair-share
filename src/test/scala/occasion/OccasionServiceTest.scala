package occasion

import java.util.UUID

import event.{ Version, Event }
import org.scalatest.{ FunSpec, Matchers }
import repo.InteractionResult
import scalaz._

import scala.collection.mutable

class OccasionServiceTest extends FunSpec with Matchers {
  object TestOccasionRepo extends OccasionRepo {
    val store = mutable.Map[OccasionId, List[Event[OccasionEvent]]]()

    def get(id: OccasionId): InteractionResult[Option[Occasion]] = {
      val maybeEvents = store.get(id)

      val maybeOccasion = for {
        events <- maybeEvents
      } yield Occasion.foldLeft(events)

      \/.right(maybeOccasion)
    }

    def store(id: OccasionId, ev: Event[OccasionEvent]): InteractionResult[Unit] = store.synchronized {
      val newEvents = store.getOrElse(id, Nil) :+ ev
      store += (id -> newEvents)
      \/.right(())
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
