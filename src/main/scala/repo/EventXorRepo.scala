package repo

import aggregate.EventXor
import util.Id

import scalaz.\/

trait EventXorRepo[A, C, M] {
  def storeEvent(eventXor: EventXor[A, C, M]): Throwable \/ Unit

  def storeEvents(eventXors: List[EventXor[A, C, M]]): Throwable \/ Unit

  def getEvents(id: Id[A]): Throwable \/ List[EventXor[A, C, M]]
}
