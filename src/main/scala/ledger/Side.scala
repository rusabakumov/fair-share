package ledger

sealed trait Side

case object Debit extends Side

case object Credit extends Side
