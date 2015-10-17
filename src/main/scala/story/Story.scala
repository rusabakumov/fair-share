package story

case class StoryId(id: Int) extends AnyVal

case class Story(
  id: StoryId,
  name: String,
  events: List[Event],
  characters: List[Character],
  transactionGroups: List[TransactionGroup]
) {
  def addTransactionGroup(transactionGroup: TransactionGroup): Story = Story(
    id, name, events, characters, transactionGroup :: transactionGroups
  )
}
