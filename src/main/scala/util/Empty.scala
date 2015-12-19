package util

trait Empty[T] {
  def empty: T
}

object Empty {
  def apply[T](anEmpty: T): Empty[T] = new Empty[T] {
    def empty: T = anEmpty
  }
}
