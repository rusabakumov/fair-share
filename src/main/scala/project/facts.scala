package project

import util._

case class ProjectCreated(id: ProjectId, name: String)

sealed trait ProjectChanged

case class ProjectNameChanged(name: String) extends ProjectChanged

case class ProjectStatusChanged(status: ProjectStatus) extends ProjectChanged

