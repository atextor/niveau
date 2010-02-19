package de.kantico.niveau

import java.io.File

import java.net.MalformedURLException

import java.awt.Color
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
    val w = 800
    val h = 600
    setTitle(text)
    setBackground(Color.BLACK)
    panel.getSharedContext.getTextRenderer.setSmoothingThreshold(0.0f)
    val pane = new JScrollPane(panel)
    pane.getViewport.setBackground(Color.BLACK)
    pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS)
    pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
    getContentPane.add(pane, "Center")
    setSize(w, h)
    
    def setContent(c: Content) {
      panel.setDocumentFromString(c.toString, c.templateUrl, nshandler)
      pack
      setSize(w, h)
    }
  }
  
  def main(args: Array[String]) {
    val n = new Frame("Niveau")
    n.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    n.setVisible(true)
    val d = new DirListing(new File("/tmp/x"), n)
    d.run
    System.exit(0)
  }
}
