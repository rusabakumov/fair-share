import repo.AggregateRepo

package object project {
  type ProjectRepo = AggregateRepo[Project]
}
