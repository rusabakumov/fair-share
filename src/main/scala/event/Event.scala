package event

case class Event[+T](payload: T, metadata: Metadata)
