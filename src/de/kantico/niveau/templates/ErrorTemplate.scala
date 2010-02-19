package de.kantico.niveau.templates

import java.io.InputStream

/**
 * @version $Id$
 */
class ErrorTemplate(input: InputStream, output: Display, message: String) extends Template(input, output) {
  override val stayVisible = true
  
  def run: Option[Template] = {
    output.setContent(new Content("error.xhtml", Map("message" -> message)))
    None
  }
}
