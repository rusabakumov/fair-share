package occasion

import event.{ Event, Version }
import org.scalatest.{ FunSpec, Matchers }
import participant.{ Participant, ParticipantId, ParticipantRepo }
import util._

import scala.collection.mutable
import scalaz.\/
import scalaz.syntax.either._

class OccasionServiceTest extends FunSpec with Matchers {

  object TestOccasionRepo extends OccasionRepo {
    val inMemoryStore = mutable.Map[OccasionId, List[Event[OccasionEvent]]]()

    def get(id: OccasionId): Throwable \/ Option[Occasion] = {
      val maybeEvents = inMemoryStore.get(id)

      val maybeOccasion = for {
        events <- maybeEvents
      } yield Occasion.foldLeft(events)

      maybeOccasion.right
    }

    def store(id: OccasionId, ev: Event[OccasionEvent]): V[Unit] = inMemoryStore.synchronized {
      val newEvents = inMemoryStore.getOrElse(id, Nil) :+ ev
      inMemoryStore += (id -> newEvents)
      ().right
    }
  }

  object TestParticipantRepo extends ParticipantRepo {
    val inMemoryStore = mutable.Map[OccasionId, List[Event[OccasionEvent]]]()

    def get(id: ParticipantId): Throwable \/ Option[Participant] = {
      id match {
        case x @ ParticipantId(1) => Some(Participant(x, "Fedor")).right
        case x @ ParticipantId(2) => Some(Participant(x, "Boris")).right
        case _ => None.right
      }
    }
  }

  object TestOccasionService extends OccasionService

  describe("OccasionServiceTest") {
    it("should change description") {
      val id = TestOccasionService.create()(TestOccasionRepo).toOption.get
      TestOccasionService.changeDescription(id, Version(0), "new description")(TestOccasionRepo)

      TestOccasionRepo.get(id).toOption.get.get.description shouldEqual Some("new description")
    }

    it("should make a transfer") {
      val id = TestOccasionService.create()(TestOccasionRepo).toOption.get

      TestOccasionService.transfer(id, Version(0), ParticipantId(1), ParticipantId(2), 777)(TestOccasionRepo, TestParticipantRepo)

      val accounts = TestOccasionRepo.get(id).toOption.get.get.accounts

      val fromAccount = accounts(ParticipantId(1))
      val toAccount = accounts(ParticipantId(2))

      fromAccount.cash.balance shouldEqual -777
      fromAccount.payable.balance shouldEqual -777
      fromAccount.goods.balance shouldEqual 0

      toAccount.cash.balance shouldEqual 777
      toAccount.payable.balance shouldEqual 777
      toAccount.goods.balance shouldEqual 0
    }

    //TODO: check sad path of make a transfer
  }
}
