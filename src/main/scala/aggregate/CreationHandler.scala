package aggregate

trait CreationHandler[A, C] {
  def handle(fact: C): A
}

object CreationHandler {
  def apply[A, C](f: C => A): CreationHandler[A, C] = new CreationHandler[A, C] {
    def handle(fact: C): A = f(fact)
  }
}
