package util

import event.Event

import scalaz.syntax.either._
import scalaz._
import Kleisli._

trait Repo[T] {
  def get(id: Id[T]): Throwable \/ Option[T]

  def store(id: Id[T], ev: Event[T]): Throwable \/ Unit
}
