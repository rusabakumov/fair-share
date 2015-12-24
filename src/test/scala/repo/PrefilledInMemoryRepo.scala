package repo

import event.{ Event, EventHandler }
import util._

import scalaz.\/
import scalaz.syntax.either._

class PrefilledInMemoryRepo[T](aggs: List[T])(
    implicit
    agg: Aggregate[T], eventHandler: EventHandler[T]
) extends Repo[T] {
  val inMemoryRepo = new InMemoryRepo[T]

  def get(id: Id[T]): Throwable \/ Option[T] = {
    val maybePrefilled = aggs.find(x => agg.id(x) == id).map { agg =>
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
  def apply[T](aggs: List[T])(implicit agg: Aggregate[T], eh: EventHandler[T]): PrefilledInMemoryRepo[T] = new PrefilledInMemoryRepo(aggs)

  def apply[T](aggs: T*)(implicit agg: Aggregate[T], eh: EventHandler[T]): PrefilledInMemoryRepo[T] = apply(aggs.toList)
}
