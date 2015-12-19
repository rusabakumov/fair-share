package util

import java.util.UUID

import occasion.Occasion

trait Id[+T] {
  def id: UUID
}

object Id {
  def apply[T](anId: UUID): Id[T] = new Id[T] {
    def id = anId
  }
}

