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
  
  def thumbnailPath(file: File): File = {
    val can = file.getCanonicalPath
    if (can.startsWith(home + sep + ".thumbnails") && can.endsWith(".png")) {
      new File(file.getCanonicalPath)
    } else {
      new File(home + sep + ".thumbnails" + sep + "normal" + sep + md5(file) + ".png")
    }
  }
  
}
