package de.kantico.niveau

import java.io.File

import net.lag.configgy.Configgy

/**
 * @version $Id$
 */
object Config {
  val defaultConfig = Map(
    "base.background"  -> "000000",
    "base.iconpath"    -> "icons",
    "base.inputpipe"   -> "/tmp/niveau-pipe",
    "base.windowtitle" -> "Niveau",
    "base.autoresize"  -> "false",
    "base.width"       -> "800",
    "base.height"      -> "600",
    "base.autoclose"   -> "true",
    "dirlisting.pattern" -> """.*\[(.*)\].*"""
  )
  
  private def file(path: String): Option[File] = {
    val f = new File(path)
    if (f.exists) Some(f) else None
  }
  
  def configure(from: Option[File]) {
    val f = from.getOrElse(
      file(System.getProperty("user.home") + "/.niveau/config").getOrElse(
      file("/etc/niveau/config").getOrElse(
      new File("cfg/config"))))
    Configgy.configure(f.getCanonicalPath)
  }

  def getString(key: String): String = Configgy.config.getString(key, defaultConfig.getOrElse(key, ""))
  
  def getInt(key: String): Int = Configgy.config.getInt(key, defaultConfig.getOrElse(key, "").toInt)
  
  def getBool(key: String): Boolean = Configgy.config.getBool(key, defaultConfig.getOrElse(key, "").toBoolean)
}
