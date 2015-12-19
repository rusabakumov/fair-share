package event

case class Event[T](payload: Payload[T], metadata: Metadata)
