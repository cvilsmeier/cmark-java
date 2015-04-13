package org.commonmark;

import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WrapperTest extends Assert {

	@Before
	public void before() {
	}

	@After
	public void after() {
	}

	@Test
	public void testReadCmarkVersion() throws Exception {
		Wrapper wrapper = new Wrapper();
		String output = wrapper.readCmarkVersion();
		String expected = "0.18.3";
		assertEquals(expected, output);
	}

	@Test
	public void testEmptyHtml() throws Exception {
		Wrapper wrapper = new Wrapper();
		String output = wrapper.convert("", OutputFormat.Html);
		String expected = "";
		assertEquivalent(expected, output);
	}

	@Test
	public void testEmptyXml() throws Exception {
		Wrapper wrapper = new Wrapper();
		String output = wrapper.convert("", OutputFormat.Xml);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE CommonMark SYSTEM \"CommonMark.dtd\">\n<document />";
		assertEquivalent(expected, output);
	}

	@Test
	public void testEmptyMan() throws Exception {
		Wrapper wrapper = new Wrapper();
		String output = wrapper.convert("", OutputFormat.Man);
		String expected = "";
		assertEquivalent(expected, output);
	}

	@Test
	public void testEmptyCommonmark() throws Exception {
		Wrapper wrapper = new Wrapper();
		String output = wrapper.convert("", OutputFormat.Commonmark);
		String expected = "";
		assertEquivalent(expected, output);
	}

	@Test
	public void testHeadingsHtml() throws Exception {
		Wrapper wrapper = new Wrapper();
		String output = wrapper.convert("# first\n## second", OutputFormat.Html);
		String expected = "<h1>first</h1>\n<h2>second</h2>";
		assertEquivalent(expected, output);
	}

	@Test
	public void testHeadingsXml() throws Exception {
		Wrapper wrapper = new Wrapper();
		String output = wrapper.convert("# first\n## second", OutputFormat.Xml);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		expected += "<!DOCTYPE CommonMark SYSTEM \"CommonMark.dtd\">\n";
		expected += "<document>\n";
		expected += "  <header level=\"1\">\n";
		expected += "    <text>first</text>\n";
		expected += "  </header>\n";
		expected += "  <header level=\"2\">\n";
		expected += "    <text>second</text>\n";
		expected += "  </header>\n";
		expected += "</document>\n";
		assertEquivalent(expected, output);
	}

	@Test
	public void testHeadingsMan() throws Exception {
		Wrapper wrapper = new Wrapper();
		String output = wrapper.convert("# first\n## second", OutputFormat.Man);
		String expected = ".SH\nfirst\n.SS\nsecond";
		assertEquivalent(expected, output);
	}

	@Test
	public void testHeadingsCommonmark() throws Exception {
		Wrapper wrapper = new Wrapper();
		String output = wrapper.convert("# first\n## second", OutputFormat.Commonmark);
		String expected = "# first\n\n## second";
		assertEquivalent(expected, output);
	}

	@Test
	public void testIn1Html() throws Exception {
		Wrapper wrapper = new Wrapper();
		String input = readResource("in-1.md");
		String output = wrapper.convert(input, OutputFormat.Html);
		String expected = readResource("out-1.html");
		assertEquivalent(expected, output);
	}

	@Test
	public void testIn1Xml() throws Exception {
		Wrapper wrapper = new Wrapper();
		String input = readResource("in-1.md");
		String output = wrapper.convert(input, OutputFormat.Xml);
		String expected = readResource("out-1.xml");
		assertEquivalent(expected, output);
	}

	@Test
	public void testIn1Man() throws Exception {
		Wrapper wrapper = new Wrapper();
		String input = readResource("in-1.md");
		String output = wrapper.convert(input, OutputFormat.Man);
		String expected = readResource("out-1.man");
		assertEquivalent(expected, output);
	}

	@Test
	public void testIn1Commonmark() throws Exception {
		Wrapper wrapper = new Wrapper();
		String input = readResource("in-1.md");
		String output = wrapper.convert(input, OutputFormat.Commonmark);
		String expected = readResource("out-1.commonmark");
		assertEquivalent(expected, output);
	}

	
	
	// private 

	private String readResource(String name) throws IOException {
		try( InputStreamReader r = new InputStreamReader(getClass().getResourceAsStream(name))) {
			StringBuilder sb = new StringBuilder();
			char[] buf = new char[1024];
			int c;
			while( (c=r.read(buf)) > 0 ) sb.append(buf,0,c);
			String s = sb.toString();
			return s;
		}
	}

	private void assertEquivalent(String expected, String actual) {
		assertEquals(expected.replace("\r", "").trim(), actual.replace("\r", "").trim());
	}
	
}
