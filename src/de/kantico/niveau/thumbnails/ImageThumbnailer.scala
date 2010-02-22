package de.kantico.niveau.thumbnails

import java.io.File
import java.lang.ProcessBuilder

/**
 * @version $Id$
 */
object ImageThumbnailer extends AbstractThumbnailer {
  override val required = List("convert")
  
  override def createThumbnail(in: File, out: File, size: Int) =
    run("convert", in.getCanonicalPath, "-scale", size + "x" + size, "png:" + out.getCanonicalPath)
}
