package de.kantico.niveau.thumbnails

import java.io.File
import java.lang.ProcessBuilder

/**
 * @version $Id$
 */
abstract class AbstractThumbnailer {
  /**
   * List of commands that are required to run this thumbnailer
   */
  def required = List[String]()
  
  def createThumbnail(in: File, out: File, size: Int): Int
  
  protected def run(command: String*): Int = {
    val pb = new ProcessBuilder(command: _*)
    val p = pb.start
    p.waitFor 
  }
}
