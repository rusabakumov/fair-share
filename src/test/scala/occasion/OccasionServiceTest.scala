package occasion

import java.util.UUID

import event.{ Event, Version }
import org.scalatest.{ FunSpec, Matchers }
import participant._
import util._

import scala.collection.mutable
import scalaz.\/
import scalaz.syntax.either._

class OccasionServiceTest extends FunSpec with Matchers {

  object TestOccasionRepo extends OccasionRepo {
    val inMemoryStore = mutable.Map[OccasionId, List[Event[Occasion]]]()

    def get(id: OccasionId): Throwable \/ Option[Occasion] = {
      val maybeEvents = inMemoryStore.get(id)

      val maybeOccasion = for {
        events <- maybeEvents
      } yield Occasion.foldLeft(events)

      maybeOccasion.right
    }

    def store(id: OccasionId, ev: Event[Occasion]): Throwable \/ Unit = inMemoryStore.synchronized {
      val newEvents = inMemoryStore.getOrElse(id, Nil) :+ ev
      inMemoryStore += (id -> newEvents)
      ().right
    }
  }

  object TestParticipantRepo extends ParticipantRepo {
    val inMemoryStore = mutable.Map[ParticipantId, List[Event[Participant]]]()

    def get(id: ParticipantId): Throwable \/ Option[Participant] = {
      val maybeEvents = inMemoryStore.get(id)

      val maybeParticipant = for {
        events <- maybeEvents
      } yield Participant.foldLeft(events)

      maybeParticipant.right
    }

    def store(id: ParticipantId, ev: Event[Participant]): Throwable \/ Unit = inMemoryStore.synchronized {
      val newEvents = inMemoryStore.getOrElse(id, Nil) :+ ev
      inMemoryStore += (id -> newEvents)
      ().right
    }
  }

  object TestOccasionService extends OccasionService

  object TestParticipantService extends ParticipantService

  describe("OccasionServiceTest") {
    it("should change description") {
      val id = TestOccasionService.create()(TestOccasionRepo).toOption.get
      TestOccasionService.changeDescription(id, Version(0), "new description")(TestOccasionRepo)

      TestOccasionRepo.get(id).toOption.get.get.description shouldEqual Some("new description")
    }

    it("should make a transfer") {
      val id = TestOccasionService.create()(TestOccasionRepo).toOption.get
      val participantId1 = TestParticipantService.create("Boris")(TestParticipantRepo).toOption.get
      val participantId2 = TestParticipantService.create("Artem")(TestParticipantRepo).toOption.get

      TestOccasionService.transfer(id, Version(0), participantId1, participantId2, 777)(
        TestOccasionRepo, TestParticipantRepo
      )

      val accounts = TestOccasionRepo.get(id).toOption.get.get.accounts

      val fromAccount = accounts(participantId1)
      val toAccount = accounts(participantId2)

      fromAccount.cash.balance shouldEqual -777
      fromAccount.payable.balance shouldEqual -777
      fromAccount.goods.balance shouldEqual 0

      toAccount.cash.balance shouldEqual 777
      toAccount.payable.balance shouldEqual 777
      toAccount.goods.balance shouldEqual 0
    }

    it("should decline a transfer") {
      val id = TestOccasionService.create()(TestOccasionRepo).toOption.get
      val participantId1 = TestParticipantService.create("Boris")(TestParticipantRepo).toOption.get
      val participantId2 = TestParticipantService.create("Artem")(TestParticipantRepo).toOption.get

      // correct transfer
      TestOccasionService.transfer(id, Version(0), participantId2, participantId1, 22)(
        TestOccasionRepo, TestParticipantRepo
      )
      // transfer to not existing Participant
      TestOccasionService.transfer(id, Version(0), participantId2, ParticipantId(UUID.randomUUID()), 2000)(
        TestOccasionRepo, TestParticipantRepo
      )

      val accounts = TestOccasionRepo.get(id).toOption.get.get.accounts

      val fromAccount = accounts(participantId2)
      val toAccount = accounts(participantId1)

      fromAccount.cash.balance shouldEqual -22
      fromAccount.payable.balance shouldEqual -22
      fromAccount.goods.balance shouldEqual 0

      toAccount.cash.balance shouldEqual 22
      toAccount.payable.balance shouldEqual 22
      toAccount.goods.balance shouldEqual 0
    }
  }
}
