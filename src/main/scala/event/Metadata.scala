package event

import java.time.LocalDateTime

case class Metadata(createdAt: LocalDateTime, version: Version)

object Metadata {
  def apply(version: Version): Metadata = Metadata(LocalDateTime.now(), version)
}
