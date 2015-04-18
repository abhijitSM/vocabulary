package actors

import akka.actor.{ Actor, ActorRef, Props }
import models.Vocabulary
import play.api.i18n.Lang

class QuizzActor(out: ActorRef, sourceLang: Lang, targetLang: Lang)
    extends Actor {
  private var word = ""

  override def preStart(): Unit = sendWord()

  def receive = {
    case translation: String if Vocabulary
      .verify(sourceLang, word, targetLang, translation) =>
      out ! "Correct!"
      sendWord()
    case _ =>
      out ! "Incorrect, try again!"
  }

  def sendWord() = {
    Vocabulary
      .findRandomVocabulary(sourceLang, targetLang).map { v =>
        out ! s"Please translate '${v.word}'"
        word = v.word
      } getOrElse {
        out ! s"I don't know any word for ${sourceLang.code} " +
          " and ${targetLang.code}"
      }
  }
}

object QuizzActor {
  def props(sourceLang: Lang, targetLang: Lang, out: ActorRef): Props =
    Props(classOf[QuizzActor], out, sourceLang, targetLang)
}