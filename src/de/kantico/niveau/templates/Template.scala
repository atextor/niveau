package de.kantico.niveau.templates

import java.io.InputStream

/**
 * This class acts as the model of an XHTML template (which acts as the view).
 * It receives an inputstream of bytes, a display to render its content to and optionally
 * a map of arguments that can be used to control the content of the template.
 * 
 * @version $Id$
 */
abstract class Template(input: InputStream, output: Display, arguments: Map[String, String]) {
	/**
     * Alternative constructor without arguments
     */
	def this(input: InputStream, output: Display) = this(input, output, Map())
 
    /**
     * Entry point of this template.
     * @return The Some(template) the control should be handed over to, or None if the input
     * stream was closed.
     */
	def run: Option[Template]
}
