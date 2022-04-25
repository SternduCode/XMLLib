package com.sterndu.xml;

import java.util.*;

public class XMLStringComponent extends XMLComponent {

	public XMLStringComponent(String value) {
		super(value);
	}

	@Override
	public XMLComponent addChild(XMLComponent xc) {
		throw new UnsupportedOperationException();
	}

	@Override
	public XMLComponent addChild(XMLComponent xc, int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public XMLComponent addParam(String key, String param) {
		throw new UnsupportedOperationException();
	}

	public void changeValue(String value) {
		super.setName(value);
	}

	@Override
	public XMLComponent getChild(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<XMLComponent> getChilds() { return new ArrayList<>(); }

	@Override
	public String getParam(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, String> getParams() { return new HashMap<>(); }

	public String getValue() { return getName(); }

	@Override
	public boolean hasParam(String string) {
		throw new UnsupportedOperationException();
	}

	@Override
	public XMLComponent removeChild(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public XMLComponent removeChild(XMLComponent xc) {
		throw new UnsupportedOperationException();
	}

	@Override
	public XMLComponent removeParam(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public XMLComponent setChild(XMLComponent xc, int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public XMLComponent setParam(String key, String param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return XMLWriter.writetoString(this, true, true);
	}

	@Override
	public String toString(boolean prettyprint, boolean html) {
		return XMLWriter.writetoString(this, prettyprint, html);
	}

	@Override
	public String toStringInternal() {
		return XMLWriter.writetoStringInternal(this, true, true);
	}

	@Override
	public String toStringInternal(boolean prettyprint, boolean html) {
		return XMLWriter.writetoStringInternal(this, prettyprint, html);
	}

}
