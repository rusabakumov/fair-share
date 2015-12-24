package occasion

import java.util.UUID

import account.Account
import event.Version
import org.scalatest.{ FunSpec, Matchers }
import participant._
import repo.PrefilledInMemoryRepo
import util._
import util.syntax.repo._

import scalaz.{ -\/, \/- }

class OccasionServiceTest extends FunSpec with Matchers {

  object TestOccasionService extends OccasionService

  object TestParticipantService extends ParticipantService

  describe("OccasionServiceTest") {
    it("should change description") {
      val occasion = Occasion.blank
      val repo = PrefilledInMemoryRepo(occasion)
      TestOccasionService.changeDescription(occasion.id, Version(0), "new description")(repo)

      repo.getV(occasion.id).map(_.description) shouldEqual \/-(Some("new description"))
    }

    describe("transfer") {
      val occasion = Occasion.blank
      val boris = Participant.blank.copy(name = "Boris")
      val artem = Participant.blank.copy(name = "Artem")

      val occasionRepo = PrefilledInMemoryRepo(occasion)
      val participantRepo = PrefilledInMemoryRepo(boris, artem)

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
