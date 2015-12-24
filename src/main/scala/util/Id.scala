package util

import java.util.UUID

trait Id[+T] {
  def id: UUID
}

object Id {
  def apply[T](anId: UUID): Id[T] = new Id[T] {
    def id = anId
  }
}

