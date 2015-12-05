package util

import scalaz.\/

trait Validation {
  type V[T] = String \/ T

}
