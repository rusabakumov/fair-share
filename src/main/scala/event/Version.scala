package event

case class Version(v: Int) extends AnyVal with Ordered[Version] {
  def compare(that: Version): Int = this.v - that.v

  def next: Version = copy(v + 1)
}
object Version {
  def zero: Version = Version(0)
}
