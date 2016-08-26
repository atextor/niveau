niveau 
======

An experimental graphical add-on view for the command line to display things your xterm can’t.

See also the [accompanying blog post](http://atextor.de/2012/04/20/the-quest-for-the-next-generation-power-user-interface.html) for motivation.
It turned out to not be as useful for me as I had originally anticipated.

niveau reads a byte stream (e.g., the content of your terminal
window), extracts interesting information (e.g., your current working
directory), and generates a graphical view you don’t have in your terminal
window (e.g., thumbnails of images). niveau works totally non-invasive - you
don’t have to change your terminal emulator or your shell. The view is
customizable using XHTML/CSS templates.

niveau was developed using the Scala language, the Flying Saucer XHTML renderer
and the Apache Velocity templating engine. 

To give it a try:
 * Have the bin/ of a Scala 2.7 distribution (e.g. Scala 2.7.7) in your PATH (does not work with 2.8 or newer)
 * `sh compile.sh`
 * `mkfifo /tmp/niveau-pipe`, check cfg/config
 * `sh run.sh` and in another terminal, `script -f /tmp/niveau-pipe`

niveau was written by Andreas Textor <textor.andreas@googlemail.com>.


