package de.kantico.niveau

/**
 * @version $Id$
 */
trait ErrorMessage {
  val errorTemplate = "error.xhtml"
  def errorMessage(message: String): Content = new Content(errorTemplate, Map("message" -> message))
}
