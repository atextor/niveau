package de.kantico.niveau.thumbnails

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Functions to work with the freedesktop.org thumbnail specification.
 * 
 * @version $Id$
 */
object Thumbnails {
  val home = System.getProperty("user.home")
  val sep = File.separator
  val nailers = Map[String, AbstractThumbnailer](
    "png" -> ImageThumbnailer,
    "jpg" -> ImageThumbnailer
  )
  
  /**
   * Calculates the MD5 sum of a normalized URL of the file path (not the file itself), as
   * specified for the file names of thumbnails in the freedesktop.org thumbnail specification
   */
  def md5(file: File): String = {
    val digest = MessageDigest.getInstance("MD5")
    val path = ("file://" + file.getCanonicalPath).getBytes
    digest.reset
    digest.update(path)
    val bytes = digest.digest
    new BigInteger(1, bytes).toString(16)
  }
  
  /**
   * When a thumbnail tn for a file is found, Some(tn) is returned. Otherwise, a thumbnailer
   * for the file format is searched. If none is found, the result is None. Otherwise, this
   * thumbnailer is called. If it succeeds to generate a thumbnail tn', Some(tn') is returned,
   * otherwise None.
   */
  def thumbnail(file: File, fileType: String): Option[File] = {
    val can = file.getCanonicalPath
    if (can.startsWith(home + sep + ".thumbnails") && can.endsWith(".png")) {
      Some(file.getCanonicalFile)
    } else {
      val tn = new File(home + sep + ".thumbnails" + sep + "normal" + sep + md5(file) + ".png")
      if (!tn.exists) {
        val procresult: Option[Int] = nailers.get(fileType).map(_.createThumbnail(file, tn, 64))
        if (procresult.isDefined && procresult.get == 0) Some(tn) else None
      } else Some(tn)
    }
  }
  
}
