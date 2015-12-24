package util

trait Aggregate[T] {
  def name: String

  def blank: T

  def id(agg: T): Id[T]
}

object Aggregate {
  def build[T](aName: String, aBlank: => T, anIdF: T => Id[T]): Aggregate[T] = new Aggregate[T] {
    def name: String = aName

    def blank: T = aBlank

    def id(agg: T) = anIdF(agg)
  }
}
