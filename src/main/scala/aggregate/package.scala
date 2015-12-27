import scalaz.{ -\/, \/-, \/ }

package object aggregate {
  type EventXor[A, C, M] = Event[A, C] \/ Event[A, M]

  object EventXor {
    def create[A, C, M](e: Event[A, C]): EventXor[A, C, M] = -\/(e)

    def change[A, C, M](e: Event[A, M]): EventXor[A, C, M] = \/-(e)
  }
}
