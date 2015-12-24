package event

import util.Aggregate

trait EventHandler[T] {
  def handle(agg: T, event: Event[T]): T

  def foldLeft(agg: T, events: List[Event[T]]): T = events.foldLeft(agg)(handle)

  def foldLeft(events: List[Event[T]])(implicit agg: Aggregate[T]): T = foldLeft(agg.blank, events)
}
