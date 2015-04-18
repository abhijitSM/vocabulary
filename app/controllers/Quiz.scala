package controllers

import actors.QuizzActor
import models.Vocabulary
import play.api.Play.current
import play.api.i18n.Lang
import play.api.mvc._

object Quiz extends Controller {

  def quizz(sourceLanguage: Lang, targetLanguage: Lang) = Action { request =>
    Vocabulary.findRandomVocabulary(sourceLanguage, targetLanguage).map(v => Ok(v.word)) getOrElse NotFound
  }

  def check(sourceLanguage: Lang, word: String, targetLanguage: Lang,
    translation: String) = Action { request =>
    val isCorrect =
      Vocabulary
        .verify(sourceLanguage, word, targetLanguage, translation)
    val correctScore =
      request.session.get("correct").map(_.toInt).getOrElse(0)
    val wrongScore =
      request.session.get("wrong").map(_.toInt).getOrElse(0)
    if (isCorrect) {
      Ok.withSession(
        "correct" -> (correctScore + 1).toString,
        "wrong" -> wrongScore.toString
      )
    } else {
      NotAcceptable.withSession(
        "correct" -> correctScore.toString,
        "wrong" -> (wrongScore + 1).toString
      )
    }
  }

  def quizzEndpoint(sourceLang: Lang, targetLang: Lang) =
    WebSocket.acceptWithActor[String, String] { request =>
      out =>
        QuizzActor.props(sourceLang, targetLang, out)
    }
}
