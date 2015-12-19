package event

import util.Id

trait Payload[T] {
  def id: Id[T]
}
