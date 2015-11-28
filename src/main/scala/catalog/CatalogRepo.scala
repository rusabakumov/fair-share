package catalog

import event.Event

import scalaz.concurrent.Task

trait CatalogRepo {
  def store(ev: Event[CatalogEvent]): Task[Boolean]
}
