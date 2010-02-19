package de.kantico.niveau

import java.io.File
import java.io.FileInputStream
import java.io.Reader
import java.io.InputStreamReader

import java.util.Vector

/**
 * @version $Id$
 */
class DirListing(file: File, display: Display) extends Runnable with ErrorMessage {
  val templateFile = Config.getString("dirlisting.template")
  val home = System.getProperty("user.home")
  val pat = Config.getString("dirlisting.pattern").r
  
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
  
  // Must be private or final for tail call optimization to work
  // Should get a @scala.annotation.tailrec on switch to 2.8
  final def readByte(r: Reader, line: StringBuffer) {
    val i = r.read
    i match {
      case -1 => r.close
      case 10 => readByte(r, new StringBuffer)
      case 36 =>
        line.append(Character.toChars(i))
        val l = removeAnsiEscapes(line.toString)
        val c = pat.findFirstMatchIn(l).map(x => content(x.group(1))).getOrElse(
          errorMessage("No path found your prompt: " + l + "<br/>" +
                        "Should be extracted using regexp: " + pat)
        )
        this.display.setContent(c)
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

