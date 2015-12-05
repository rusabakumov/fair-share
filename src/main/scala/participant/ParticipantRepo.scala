package participant

import util._

import scalaz.syntax.either._
import scalaz.{ Kleisli, Show, \/ }
import scalaz.std.java.throwable._

trait ParticipantRepo {
  def get: Kleisli[V, ParticipantId, Participant] = Kleisli[V, ParticipantId, Participant] { id =>
    val result = get(id).fold(
      Show[Throwable].shows(_).left[Participant],
      {
        case None => s"Participant $id does not exist.".left[Participant]
        case Some(occasion) => occasion.right[String]
      }
    )

    result
  }

  protected def get(id: ParticipantId): Throwable \/ Option[Participant]
}
