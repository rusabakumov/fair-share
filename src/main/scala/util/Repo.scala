package util

import event.Event

import scalaz.\/

trait Repo[T] {
  def get(id: Id[T]): Throwable \/ Option[T]

  def store(id: Id[T], ev: Event[T]): Throwable \/ Unit
}
