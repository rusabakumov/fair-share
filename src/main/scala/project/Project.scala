package project

import participant.Participant
import occasion.Occasion

case class ProjectId(id: Int) extends AnyVal

case class Project(
  id: ProjectId,
  name: String,
  characters: List[Participant],
  occasions: List[Occasion]
) {
}
