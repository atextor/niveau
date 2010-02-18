package de.kantico.niveau

import java.io.File
import java.io.FileInputStream
import java.io.Reader
import java.io.InputStreamReader

/**
 * @version $Id$
 */
class DirListing(file: File, display: Display) extends Runnable {
  val templateFile = "dirlisting.xhtml"
  val home = System.getProperty("user.home")
  val pat = """.*\\[(.*)\\].*""".r.pattern
  
  def content(dir: String): Content = new Content(templateFile, Map(
    "dir"   -> dir,
    "files" -> new File(dir.replace("~", home)).listFiles
  ))

  def removeAnsiEscapes(s: String) =
    s.replaceAll("\\[1;3.m", "").
    replace("[0m", "").
    replace("]0;", "").
    replace("[K", "").
    replace("", "")
  
  def readByte(r: Reader, line: StringBuffer) {
    val i = r.read
    i match {
      case -1 => r.close
      case 10 => readByte(r, new StringBuffer)
      case 36 =>
        line.append(Character.toChars(i))
        val l = removeAnsiEscapes(line.toString)
        val matcher = pat.matcher(l)
        if (matcher.matches) {
          this.display.setContent(content(matcher.group(1)))
        }
        readByte(r, line)
      case _ =>
        line.append(Character.toChars(i))
        readByte(r, line)
    }
  }
  
  def run() {
    readByte(new InputStreamReader(new FileInputStream(file)), new StringBuffer)
  }

}

