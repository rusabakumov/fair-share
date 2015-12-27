package aggregate

trait ChangeHandler[A, M] {
  def handle(a: A, fact: M): A
}

object ChangeHandler {
  def apply[A, M](f: (A, M) => A): ChangeHandler[A, M] = new ChangeHandler[A, M] {
    def handle(a: A, fact: M): A = f(a, fact)
  }
}
