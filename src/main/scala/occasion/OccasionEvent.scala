package occasion

sealed trait OccasionEvent

case class OccasionCreated(id: OccasionId) extends OccasionEvent

case class OccasionDescriptionChanged(description: String) extends OccasionEvent
