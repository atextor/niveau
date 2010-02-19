package de.kantico.niveau

import java.io.File
import java.io.FileInputStream
import java.io.Reader
import java.io.InputStreamReader

import java.util.Vector

/**
 * @version $Id$
 */
class DirListing(file: File, display: Display) extends Runnable {
  val templateFile = "dirlisting.xhtml"
  val home = System.getProperty("user.home")
  val pat = """.*\[(.*)\].*""".r
  
  def content(dir: String): Content = new Content(templateFile, Map(
    "dir"   -> dir,
    "files" -> {
      val v = new Vector[RichFile]
      new File(dir.replace("~", home)).listFiles.map(new RichFile(_)).foreach(v.add(_))
      v 
    }
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
        pat.findFirstMatchIn(l).map(x => this.display.setContent(content(x.group(1))))
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

