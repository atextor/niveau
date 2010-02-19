package de.kantico.niveau.templates

import de.kantico.niveau.RichFile

import java.io.File
import java.io.FileInputStream
import java.io.InputStream

import java.util.Vector

/**
 * @version $Id$
 */
class DirListingTemplate(input: InputStream, output: Display, arguments: Map[String, String]) extends
  	Template(input, output, arguments) with ErrorMessage {
  	  
  val templateFile = Config.getString("dirlisting.template")
  val home = System.getProperty("user.home")
  val pat = Config.getString("dirlisting.pattern").r
  
  def content(dir: String): Content = new Content(templateFile, arguments ++ Map(
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
  final def readByte(r: InputStream, line: StringBuffer): Option[Template] = {
    val i = r.read
    i match {
      case -1 => None
      case 10 => readByte(r, new StringBuffer)
      case 36 =>
        line.append(Character.toChars(i))
        val l = removeAnsiEscapes(line.toString)
        val c = pat.findFirstMatchIn(l)
        if (c.isEmpty) {
          Some(new ErrorTemplate(r, output, "No path found your prompt: " + l + "<br/>" +
                                            "Should be extracted using regexp: " + pat))
        } else {
          this.output.setContent(content(c.get.group(1)))
	      readByte(r, line)
        }
      case _ =>
        line.append(Character.toChars(i))
        readByte(r, line)
    }
  }
  
  def run: Option[Template] = readByte(input, new StringBuffer)

}

