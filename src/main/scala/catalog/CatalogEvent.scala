package catalog

import project.ProjectId

sealed trait CatalogEvent

case class ProjectCreated(id: ProjectId, name: String) extends CatalogEvent
