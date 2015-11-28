package project

import java.util.UUID

import occasion.OccasionId
import participant.ParticipantId

case class ProjectId(id: UUID) extends AnyVal

case class Project(
    id: ProjectId,
    name: String,
    characters: List[ParticipantId],
    occasions: List[OccasionId]
) {
}
