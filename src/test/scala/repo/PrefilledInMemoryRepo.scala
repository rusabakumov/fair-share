package repo

import event.{ Event, EventHandler }
import util.{ Id, Empty, Repo }

import scalaz.\/
import scalaz.syntax.either._

class PrefilledInMemoryRepo[T](aggs: Map[Id[T], T])(
    implicit
    empty: Empty[T], eventHandler: EventHandler[T]
) extends Repo[T] {
  val inMemoryRepo = new InMemoryRepo[T]

  def get(id: Id[T]): Throwable \/ Option[T] = {
    val maybePrefilled = aggs.get(id).map { agg =>
      val inMemoryEvents = inMemoryRepo.inMemoryStore.getOrElse(id, Nil)
      eventHandler.foldLeft(agg, inMemoryEvents)
    }

    maybePrefilled match {
      case x @ Some(_) => x.right
      case None => inMemoryRepo.get(id)
    }
  }

  def store(id: Id[T], ev: Event[T]): Throwable \/ Unit = inMemoryRepo.store(id, ev)
}

object PrefilledInMemoryRepo {
  def apply[T](aggs: Map[Id[T], T])(implicit empty: Empty[T], eventHandler: EventHandler[T]): PrefilledInMemoryRepo[T] = new PrefilledInMemoryRepo[T](aggs)

  def apply[T](aggs: List[(Id[T], T)])(implicit empty: Empty[T], eventHandler: EventHandler[T]): PrefilledInMemoryRepo[T] = apply(aggs.toMap)

  def apply[T](aggs: (Id[T], T)*)(implicit empty: Empty[T], eventHandler: EventHandler[T]): PrefilledInMemoryRepo[T] = apply(aggs.toMap)
}
