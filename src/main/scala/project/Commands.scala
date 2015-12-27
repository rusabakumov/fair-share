package project

import aggregate.Version
import util._
import aggregate.EventSourced.commands._

protected[project] object Commands {
  val projectChangeCommand = changeCommand[Project, ProjectCreated, ProjectChanged] _

  val projectCreateCommand = createCommand[Project, ProjectCreated, ProjectChanged] _

  def create(id: ProjectId, name: String): Project =
    projectCreateCommand(Project(id, name, ProjectStatus.Open, Version.zero), ProjectCreated(id, name))

  def changeName(project: Project, name: String): Project =
    projectChangeCommand(project.copy(name = name), ProjectNameChanged(name))
}
