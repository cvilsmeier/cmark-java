package org.commonmark;

import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WrapperTest extends Assert {

	Wrapper wrapper;

	@Before
	public void before() {
		wrapper = new Wrapper();
	}

	@After
	public void after() {
	}

	@Test
	public void testEmptyMarkdown() throws Exception {
		String html = wrapper.convert("");
		assertEquals("", html);
	}

	@Test
	public void testHeadings() throws Exception {
		String md = "# first\n## second\n";
		String expectedHtml = "<h1>first</h1>\n<h2>second</h2>\n";
		String html = wrapper.convert(md);
		assertEquals(expectedHtml, html);
	}

	@Test
	public void testIn1() throws Exception {
		String md = readResource("in-1.md");
		String html = wrapper.convert(md);
		String expectedHtml = readResource("out-1.html");
		assertEquals(expectedHtml.trim(), html.trim());
	}

	
	private String readResource(String name) throws IOException {
		try( InputStreamReader r = new InputStreamReader(getClass().getResourceAsStream(name))) {
			StringBuilder sb = new StringBuilder();
			char[] buf = new char[1024];
			int c;
			while( (c=r.read(buf)) > 0 ) sb.append(buf,0,c);
			String s = sb.toString();
			s = s.replace("\r", "");
			return s;
		}
	}
	
}
