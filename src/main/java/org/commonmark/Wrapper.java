package org.commonmark;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper around the <a href="https://github.com/jgm/cmark">cmark</a> tool from John MacFarlane.
 * It can be used to programmatically convert Markdown into Html or Xml or Man or Commonmark.<br>
 * 
<pre><code>Wrapper wrapper = new Wrapper();
String md = "# first\n## second\n";
String html = wrapper.convertToHtml(md);
System.out.println(html);</code></pre>

 * will print:

<pre><code>&lt;h1&gt;first&lt;/h1&gt;
&lt;h2&gt;second&lt;/h2&gt;</code></pre>
 *  
 */
public class Wrapper {

	private final String cmarkPath;

	/**
	 * Initializes a wrapper around cmark, assuming cmark is in the system's search path.
	 */
	public Wrapper() {
		super();
		this.cmarkPath = "cmark";
	}

	/**
	 * Initializes a wrapper around cmark.
	 * @param cmarkPath The path name where to find cmark, e.g. <code>/usr/bin/cmark</code>
	 */
	public Wrapper(String cmarkPath) {
		super();
		this.cmarkPath = cmarkPath;
	}

	
	/**
	 * Returns the cmark version.
	 * 
	 * @return The cmark version string
	 * @throws IOException If cmark could not be invoked or its exit code is != 0
	 */
	public String readCmarkVersion() throws IOException {
		ArrayList<String> commandList = new ArrayList<String>();
		commandList.add(cmarkPath);
		commandList.add("--version");
		String output = invoke(commandList, "");
		if( output.startsWith("cmark ") ) {
			String sub1 = output.substring(6);
			int i = sub1.indexOf(' ');
			if( i>0 ) {
				String sub2 = sub1.substring(0,i);
				return sub2.trim();
			} else {
				throw new IOException("cannot determine cmark version");
			}
		} else {
			throw new IOException("cannot determine cmark version");
		}
	}

	
	/**
	 * Converts a markdown text to Html.
	 * 
	 * @param input The (markdown) input text
	 * @return The Html output
	 * @throws IOException If cmark could not be invoked or its exit code is != 0
	 */
	public String convert(String input) throws IOException {
		return convert(input, OutputFormat.Html, 0, false, false, false, false);
	}

	/**
	 * Converts a markdown text to Html/Xml/Man/Commonmark.
	 * 
	 * @param input The markdown input
	 * @param outputFormat Specify output format (html, xml, man, commonmark), see {@link OutputFormat}
	 * @return The Html/Xml/Man/Commonmark output
	 * @throws IOException If cmark could not be invoked or its exit code is != 0
	 */
	public String convert(String input, OutputFormat outputFormat) throws IOException {
		ArrayList<String> commandList = new ArrayList<String>();
		commandList.add(cmarkPath);
		commandList.add("--to");
		commandList.add(outputFormat.getName());
		String output = invoke(commandList, input);
		return output;
	}
	

	/**
	 * Converts a markdown text to Html/Xml/Man/Commonmark.
	 * 
	 * @param input The markdown input
	 * @param outputFormat Specify output format (html, xml, man, commonmark), see {@link OutputFormat}
	 * @param width Specify wrap width (default 0 = nowrap)
	 * @param sourcepos Include source position attribute
	 * @param hardbreaks Treat newlines as hard line breaks
	 * @param smart Use smart punctuation
	 * @param normalize Consolidate adjacent text nodes
	 * @return The Html/Xml/Man/Commonmark output
	 * @throws IOException If cmark could not be invoked or its exit code is != 0
	 */
	public String convert(String input, OutputFormat outputFormat, int width, boolean sourcepos, boolean hardbreaks, boolean smart, boolean normalize) throws IOException {
		ArrayList<String> commandList = new ArrayList<String>();
		commandList.add(cmarkPath);
		commandList.add("--to");
		commandList.add(outputFormat.getName());
		commandList.add("--width");
		commandList.add(""+width);
		if( sourcepos ) {
			commandList.add("--sourcepos");
		}
		if( hardbreaks ) {
			commandList.add("--hardbreaks");
		}
		if( smart ) {
			commandList.add("--smart");
		}
		if( normalize ) {
			commandList.add("--normalize");
		}
		String output = invoke(commandList, input);
		return output;
	}
	
	
	// private
	
	private String invoke( List<String> commandList, String input ) throws IOException {
		ProcessBuilder pb = new ProcessBuilder(commandList);
		Process p = pb.start();
		String output;
		try (OutputStream pout = p.getOutputStream()) {
			pout.write(input.getBytes(StandardCharsets.UTF_8));
			pout.close();
			try (InputStreamReader r = new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8)) {
				StringBuilder sb = new StringBuilder();
				char[] cbuf = new char[1024];
				int c;
				while ((c = r.read(cbuf)) > 0) {
					sb.append(cbuf, 0, c);
				}
				output = sb.toString();
			}
		}
		int exitCode = p.exitValue();
		if (exitCode != 0) throw new IOException("cmark exit " + exitCode);
		return output;
	}
}
