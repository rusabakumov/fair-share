package project

import event.Version
import util._

case class Project(
  id: ProjectId,
  name: String,
  version: Version
)
