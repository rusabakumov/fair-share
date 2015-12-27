package project

import aggregate.{ ChangeHandler, CreationHandler, Version }

private[project] trait Handlers {
  implicit val createHandler: CreationHandler[Project, ProjectCreated] = CreationHandler { fact =>
    Project(id = fact.id, name = fact.name, ProjectStatus.Open, version = Version.zero)
  }

  implicit val changeHandler: ChangeHandler[Project, ProjectChanged] = ChangeHandler { (project, fact) =>
    fact match {
      case ProjectNameChanged(name) => project.copy(name = name)
      case ProjectStatusChanged(status) => project.copy(status = status)
    }
  }
}
