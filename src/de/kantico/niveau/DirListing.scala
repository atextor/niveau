package de.kantico.niveau

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.StringWriter
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @version $Id$
 */
class DirListing(file: File, display: Display) extends Runnable {
  val templateFile = "dirlisting.xhtml"
  val reader = new InputStreamReader(new FileInputStream(file))
  val home = System.getProperty("user.home")

  def removeAnsiEscapes(s: String) =
    s.replaceAll("\\[1;3.m", "").
    replace("[0m", "").
    replace("]0;", "").
    replace("[K", "").
    replace("", "")
  
  def content(dir: String): Content = new Content(templateFile, Map(
    "dir"   -> dir,
    "files" -> new File(dir.replace("~", home)).listFiles
  ))

  def run() {
    val pat = Pattern.compile(".*\\[(.*)\\].*")
    var lastLine = new StringBuffer
    var i = 0
    while(i != -1) {
      i = reader.read
      if (i == 10) {
        lastLine = new StringBuffer
      } else {
        lastLine.append(Character.toChars(i))
      }
      if (i == 36) {
        val line = removeAnsiEscapes(lastLine.toString)
        val matcher = pat.matcher(line)
        if (matcher.matches) {
          this.display.setContent(content(matcher.group(1)))
        }
      }
    }
    reader.close
  }

}

