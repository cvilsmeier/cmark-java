package org.commonmark;

public enum OutputFormat {

	Html("html"), Xml("xml"), Man("man"), Commonmark("commonmark");

	private final String name;

	private OutputFormat(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
