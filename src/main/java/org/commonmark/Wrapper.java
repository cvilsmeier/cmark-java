package org.commonmark;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * A wrapper around the <a href="https://github.com/jgm/cmark">cmark</a> tool from John MacFarlane.
 * It can be used to programmatically convert Markdown into HTML.<br>
 * 
<pre><code>Wrapper wrapper = new Wrapper();
String md = "# first\n## second\n";
String html = wrapper.convert(md);
System.out.println(html);</code></pre>

 * will print:

<pre><code>&lt;h1&gt;first&lt;/h1&gt;
&lt;h2&gt;second&lt;/h2&gt;</code></pre>
 *  
 */
public class Wrapper {

	private final String cmarkCommand;

	public Wrapper() {
		super();
		this.cmarkCommand = "cmark";
	}

	public Wrapper(String cmarkCommand) {
		super();
		this.cmarkCommand = cmarkCommand;
	}

	public String convert(String source) throws IOException {
		ProcessBuilder pb = new ProcessBuilder(cmarkCommand);
		Process p = pb.start();
		String html;
		try (OutputStream pout = p.getOutputStream()) {
			pout.write(source.getBytes(StandardCharsets.UTF_8));
			pout.close();
			try (InputStreamReader r = new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8)) {
				StringBuilder sb = new StringBuilder();
				char[] cbuf = new char[1024];
				int c;
				while ((c = r.read(cbuf)) > 0) {
					sb.append(cbuf, 0, c);
				}
				html = sb.toString();
			}
		}
		int exitCode = p.exitValue();
		if (exitCode != 0) throw new IOException("cmark exit with " + exitCode);
		return html;
	}
}
