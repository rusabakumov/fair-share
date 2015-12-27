package repo

import aggregate.EventXor
import util.Id

import scala.collection.mutable
import scalaz.syntax.either._
import scalaz.{ -\/, \/ }

class InMemoryEventXorRepo[A, C, M] extends EventXorRepo[A, C, M] {
  type EXor = EventXor[A, C, M]

  val inMemoryStore = mutable.Map[Id[A], Vector[EXor]]()

  def storeEvent(eventXor: EventXor[A, C, M]): Throwable \/ Unit = inMemoryStore.synchronized {
    val id = eventXor.fold(_.id, _.id)
    val toStore = inMemoryStore.getOrElse(id, Vector.empty) :+ eventXor
    inMemoryStore += (id -> toStore)
    ().right
  }

  def getEvents(id: Id[A]): Throwable \/ List[EventXor[A, C, M]] = {
    inMemoryStore.getOrElse(id, Vector.empty).toList.right
  }

  def storeEvents(eventXors: List[EventXor[A, C, M]]): Throwable \/ Unit = inMemoryStore.synchronized {
    eventXors.map(storeEvent).foldLeft(\/.right[Throwable, Unit](())) {
      (acc, cur) =>
        cur match {
          case -\/(err) => err.left[Unit]
          case _ => acc
        }
    }
  }
}
