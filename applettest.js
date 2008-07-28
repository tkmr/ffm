  var src = '<object id="applet" name="applet" codetype="application/java" type="application/x-java-applet"' +
    ' classid="java:ffm.JSocketApplet" width="300" height="200">' +
    '<param NAME="cache_option" VALUE="false" />' +
    '<param NAME="cache_archive" VALUE="java/lib/plugin.jar, java/lib/jssocket.jar" />' +
    '<param NAME="cache_version" VALUE="0.0.0.2, 0.0.7.1" />' +
    '<param name="codebase" value="." />' +
    '<param name="type" value="application/x-java-applet;jpi-version=1.5.0" />' +
    '<param name="scriptable" value="true" />' +
    '<param name="mayscript" value="true" />' +
    '<param name="policy" value="/test.policy" />' +
    '</object>';
  document.write(src);
