import scalaz.\/

package object repo {
  type InteractionResult[T] = Throwable \/ T
}
