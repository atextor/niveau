package de.kantico.niveau

import de.kantico.niveau.templates.Template

/**
 * @version $Id$
 */
trait Display {
  def setContent(c: Content)
  def handOverTo(t: Template)
}

