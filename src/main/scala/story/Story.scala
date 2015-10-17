package story

case class StoryId(id: Int) extends AnyVal

case class Story(
  id: StoryId,
  name: String,
  topics: List[Topic],
  characters: List[Character],
  transactionGroups: List[TransactionGroup]
)
