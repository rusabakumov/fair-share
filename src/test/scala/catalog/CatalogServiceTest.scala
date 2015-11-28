package catalog

import event.Event
import org.scalatest.{ Matchers, FunSpec }

import scalaz.concurrent.Task

class CatalogServiceTest extends FunSpec with Matchers {

  object TestCatalogRepo extends CatalogRepo {
    def store(ev: Event[CatalogEvent]): Task[Boolean] = Task(true)
  }

  object TestCatalogService extends CatalogService

  describe("CatalogServiceTest") {

    it("should createProject") {
      val meth = TestCatalogService.createProject("Test")
      val res = meth(TestCatalogRepo)

      res.unsafePerformSync shouldBe defined
    }

  }
}
