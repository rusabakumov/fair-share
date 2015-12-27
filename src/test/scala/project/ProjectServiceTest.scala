package project

import java.util.UUID

import aggregate._
import org.scalatest.{ Matchers, FunSpec }
import repo._
import util._

class ProjectServiceTest extends FunSpec with Matchers {
  describe("ProjectServiceTest") {
    def getRepo: ProjectRepo = new EventSourcedAggregateRepo[Project, ProjectCreated, ProjectChanged](new InMemoryEventXorRepo)

    it("should create") {
      val repo = getRepo

      val testId = ProjectId(UUID.randomUUID())

      ProjectService.create(testId, "POC").run(repo)

      val fromRepo = repo.getAggregate(testId)

      fromRepo.fold(
        e => fail(e),
        pOpt => pOpt shouldEqual Some(Project(testId, "POC", ProjectStatus.Open, Version.zero))
      )
    }
  }
}
