package de.kantico.niveau

import de.kantico.niveau.templates.DirListingTemplate
import de.kantico.niveau.templates.Template

import java.io.File
import java.io.FileInputStream

import java.awt.Color
import java.awt.Dimension
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.ScrollPaneConstants

import org.xhtmlrenderer.simple.XHTMLPanel
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler

/**
 * @version $Id$
 */
object Niveau {
  class Frame(text: String) extends JFrame with Display {
    val panel = new XHTMLPanel
    val nshandler = new XhtmlNamespaceHandler
    val w = Config.getInt("base.width")
    val h = Config.getInt("base.height")
    val bgColor = Color.decode(Config.getString("base.background"))
    setTitle(text)
    setBackground(bgColor)
    panel.getSharedContext.getTextRenderer.setSmoothingThreshold(0.0f)
    panel.setPreferredSize(new Dimension(w, h));
    val pane = new JScrollPane(panel)
    pane.getViewport.setBackground(bgColor)
    pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS)
    pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
    getContentPane.add(pane, "Center")
    pack
    setSize(w, h)
    
    def setContent(c: Content) {
      panel.setDocumentFromString(c.toString, c.templateUrl, nshandler)
      if (Config.getBool("base.autoresize")) pack
    }
    
    // Must be private or final for tail call optimization to work
    // Should get a @scala.annotation.tailrec on switch to 2.8
    final def handOverTo(t: Template) {
      val next = t.run
      if (next.isDefined) {
        handOverTo(next.get)
      } else {
        t.input.close
        if (!t.stayVisible && Config.getBool("base.autoclose")) {
          System.exit(0)
        }
        // Just do nothing. The swing thread will remain to display the last template
      }
    }
  }
  
  def main(args: Array[String]) {
    Config.configure(None)
    
    val n = new Frame(Config.getString("base.windowtitle"))
    n.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    n.pack
    n.setVisible(true)
    val input = new FileInputStream(new File(Config.getString("base.inputpipe")))
    n.handOverTo(new DirListingTemplate(input, n, Map()))
  }
}
