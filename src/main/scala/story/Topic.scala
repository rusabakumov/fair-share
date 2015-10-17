package story

case class TopicId(id: Int) extends AnyVal

case class Topic(id: TopicId, description: String)
