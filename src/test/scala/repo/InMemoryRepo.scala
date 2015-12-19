package repo

import event.{ Event, EventHandler }
import util.{ Empty, Id, Repo }

import scala.collection.mutable
import scalaz.\/
import scalaz.syntax.either._

class InMemoryRepo[T](implicit empty: Empty[T], eventHandler: EventHandler[T]) extends Repo[T] {
  val inMemoryStore = mutable.Map[Id[T], List[Event[T]]]()

  def get(id: Id[T]): Throwable \/ Option[T] = {
    val maybeEvents = inMemoryStore.get(id)

    val maybeObject = for {
      events <- maybeEvents
    } yield eventHandler.foldLeft(events)

    maybeObject.right
  }

  def store(id: Id[T], ev: Event[T]): Throwable \/ Unit = inMemoryStore.synchronized {
    val newEvents = inMemoryStore.getOrElse(id, Nil) :+ ev
    inMemoryStore += (id -> newEvents)
    ().right
  }
}

object InMemoryRepo {
  def apply[T](implicit empty: Empty[T], eventHandler: EventHandler[T]): InMemoryRepo[T] = new InMemoryRepo[T]
}
