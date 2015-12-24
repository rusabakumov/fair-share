package util

import occasion.Occasion
import participant.Participant
import project.Project

trait Ids {
  type OccasionId = Id[Occasion]
  val OccasionId = Id.apply[Occasion] _

  type ParticipantId = Id[Participant]
  val ParticipantId = Id.apply[Participant] _

  type ProjectId = Id[Project]
  val ProjectId = Id.apply[Project] _
}
