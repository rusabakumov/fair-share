package occasion

import java.util.UUID

import account.Account
import event.Version
import org.scalatest.{ FunSpec, Matchers }
import participant._
import repo.{ InMemoryRepo, PrefilledInMemoryRepo }
import util._

import scalaz.{ \/-, -\/ }

class OccasionServiceTest extends FunSpec with Matchers {

  object TestOccasionService extends OccasionService

  object TestParticipantService extends ParticipantService

  describe("OccasionServiceTest") {
    it("should change description") {
      val repo = InMemoryRepo[Occasion]
      val id = TestOccasionService.create()(repo).toOption.get
      TestOccasionService.changeDescription(id, Version(0), "new description")(repo)

      repo.get(id).toOption.get.get.description shouldEqual Some("new description")
    }

    describe("transfer") {
      val occasion = Occasion.empty.empty
      val boris = Participant.empty.empty.copy(name = "Boris")
      val artem = Participant.empty.empty.copy(name = "Artem")

      val occasionRepo = PrefilledInMemoryRepo(occasion.id -> occasion)
      val participantRepo = PrefilledInMemoryRepo(boris.id -> boris, artem.id -> artem)

      it("should make a transfer") {
        val result = TestOccasionService.transfer(occasion.id, occasion.version, boris.id, artem.id, 777)(
          occasionRepo, participantRepo
        )

        result shouldBe a[\/-[_]]

        val accounts = occasionRepo.get(occasion.id).toOption.get.get.accounts
        val fromAccount = accounts(boris.id)
        val toAccount = accounts(artem.id)

        fromAccount shouldEqual Account.empty.give(777)
        toAccount shouldEqual Account.empty.receive(777)
      }

      it("should decline a transfer to non-existent participant") {
        val result = TestOccasionService.transfer(occasion.id, Version(0), boris.id, ParticipantId(UUID.randomUUID()), 2000)(
          occasionRepo, participantRepo
        )

        result shouldBe a[-\/[_]]
      }
    }
  }
}
