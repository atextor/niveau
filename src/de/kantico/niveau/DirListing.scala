package de.kantico.niveau

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.StringWriter
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine

class DirListing(file: File, display: Display) extends Runnable {
  val templateFile = "templates/dirlisting.xhtml"
  val reader = new InputStreamReader(new FileInputStream(file))

  def removeAnsiEscapes(s: String) =
    s.replaceAll("\\[1;3.m", "").
    replace("[0m", "").
    replace("]0;", "").
    replace("[K", "").
    replace("", "")

  def buildContent(dir: String): String = {
    val engine = new VelocityEngine
    val context = new VelocityContext
    val writer = new StringWriter
    context.put("dir", dir)
    
    val path = dir.replace("~", System.getProperty("user.home"))
    val files = new File(path).listFiles;
    context.put("files", files);
    
    val t = engine.getTemplate(templateFile)
    t.merge(context, writer)
    writer.toString
  }

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
          this.display.setDocument(buildContent(matcher.group(1)), templateFile)
        }
      }
    }
    reader.close
  }

}

