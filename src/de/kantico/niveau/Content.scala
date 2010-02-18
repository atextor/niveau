package de.kantico.niveau

import java.io.File
import java.io.StringWriter

import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine

/**
 * This class represents displayable content (i.e. a page). It takes a template file name
 * and a map of variable names to values for the template. toString merges values into the
 * template and returns the result.
 * 
 * @version $Id$
 */
class Content(template: String, properties: Map[String, Any]) {
  val templateFile = "templates/" + template
  lazy val content = {
    val engine = new VelocityEngine
    val context = new VelocityContext
    val writer = new StringWriter
    properties.foreach { case (k, v) => context.put(k, v) }
    
    val t = engine.getTemplate(templateFile)
    t.merge(context, writer)
    writer.toString
  }
  
  def templateUrl = new File(templateFile).toURI.toURL.toString
  
  override def toString: String = content
}
