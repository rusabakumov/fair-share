package util

import project.Project

trait Ids {
  type ProjectId = Id[Project]
  val ProjectId = Id.apply[Project] _
}
