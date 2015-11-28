package catalog

sealed trait CatalogCommand

case class CreateProject(name: String) extends CatalogCommand
