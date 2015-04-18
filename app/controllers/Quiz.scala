package controllers

import models.Vocabulary
import play.api.i18n.Lang
import play.api.mvc._

/**
 * Created by abhising on 17-04-2015.
 */
object Quiz extends Controller {

  def quizz(sourceLanguage: Lang, targetLanguage: Lang) = Action { request =>
    Vocabulary.findRandomVocabulary(sourceLanguage, targetLanguage).map(v => Ok(v.word)) getOrElse NotFound
  }

  def check(sourceLanguage: Lang, word: String, targetLanguage: Lang,
    translation: String) = Action { request =>
    if (Vocabulary.verify(sourceLanguage, word, targetLanguage, translation))
      Ok
    else
      NotAcceptable
  }
}
