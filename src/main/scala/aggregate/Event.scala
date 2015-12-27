package aggregate

import java.time.LocalDateTime

import util.Id

case class Event[A, +F](
  id: Id[A],
  version: Version,
  fact: F,
  createdAt: LocalDateTime
)

object Event {
  def now[A, F](id: Id[A], version: Version, fact: F): Event[A, F] = apply(id, version, fact, LocalDateTime.now())
}
