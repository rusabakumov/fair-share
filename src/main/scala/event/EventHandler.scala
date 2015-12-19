package event

import util.Empty

trait EventHandler[T] {
  def handle(agg: T, event: Event[T]): T

  def foldLeft(agg: T, events: List[Event[T]]): T = events.foldLeft(agg)(handle)

  def foldLeft(events: List[Event[T]])(implicit empty: Empty[T]): T = foldLeft(empty.empty, events)
}
