package de.kantico.niveau

import de.kantico.niveau.thumbnails.Thumbnails

import java.io.File

/**
 * Class that wraps a java.io.File and provides additional information that is useful for
 * niveau views.
 * 
 * @version $Id$
 */
class RichFile(osFile: File) {
  def fullPath = osFile.getCanonicalPath
  def fileName = osFile.getName
  def fileType = fileName.substring(fileName.lastIndexOf(".") + 1)
  def icon: String = if (osFile.isDirectory) RichFile.directoryIcon else RichFile.fileIcon(fileType)
  def thumbnail = Thumbnails.thumbnailPath(osFile)
}

object RichFile {
  def directoryIcon = "filesystems/folder.png"
  def fileIcon(ext: String) = "mimetypes/" + icons.getOrElse(ext.toLowerCase, "unknown.png")
   
  val icons = Map(
    "abw" -> "abiword_abi.png",
    "iso" -> "cdimage.png",
    "cda" -> "cdtrack.png",
    "deb" -> "deb.png",
    "dvi" -> "dvi.png",
    "ebuild" -> "ebuild.png",
    "ttf" -> "font_truetype.png",
    "jar" -> "java_jar.png",
    "makefile" -> "make.png",
    "pdf" -> "pdf.png",
    "gz" -> "tgz.png",
    "tgz" -> "tgz.png",
    "tar" -> "tar.png",
    "rpm" -> "rpm.png",
    "ps" -> "postscript.png",
    "c" -> "source_c.png",
    "cpp" -> "source_cpp.png",
    "css" -> "source_css.png",
    "f" -> "source_f.png",
    "h" -> "source_h.png",
    "java" -> "source_java.png",
    "j" -> "source_j.png",
    "l" -> "source_l.png",
    "moc" -> "source_moc.png",
    "o" -> "source_o.png",
    "php" -> "source_php.png",
    "pl" -> "source_pl.png",
    "p" -> "source_p.png",
    "py" -> "source_py.png",
    "s" -> "source_s.png",
    "y" -> "source_y.png",
    "tex" -> "tex.png",
    "svg" -> "vectorgfx.png",
    "avi" -> "video.png",
    "png" -> "image.png",
    "jpg" -> "image.png"
  )
}
