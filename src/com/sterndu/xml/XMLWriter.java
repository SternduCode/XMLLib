package com.sterndu.xml;

import java.io.*;
import java.util.*;

public class XMLWriter extends BufferedWriter {

	public XMLWriter(Writer out) {
		super(out);
	}

	public XMLWriter(Writer out, int sz) {
		super(out, sz);
	}

	public static String writetoString(List<XMLComponent> componets, boolean prettyprint, boolean html) {
		StringBuilder sb = new StringBuilder();
		for (XMLComponent xmlComponent : componets)
			sb.append(writetoString(xmlComponent, prettyprint, html)).append(System.lineSeparator());
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	public static String writetoString(XMLComponent comp, boolean prettyprint, boolean html) {
		String s = "<" + comp.getName()
		+ (comp.getParams().size() != 0 ? " " + XMLUtil.AttributestoString(comp.getParams(), !html) : "")
		+ (comp.isCompound() ? ">" : "/>");
		if (comp.isCompound()) {
			StringBuilder sb = new StringBuilder();
			sb.append(s);
			for (XMLComponent comp2 : comp.getChilds())
				sb.append(System.lineSeparator() + "	"
						+ comp2.toString(prettyprint, html).replace(System.lineSeparator(),
								System.lineSeparator() + "	"));
			sb.append(System.lineSeparator());
			sb.append("</" + comp.getName() + ">");
			s = sb.toString();
		}
		return s;
	}

	public static String writetoString(XMLComponent[] comp, boolean prettyprint, boolean html) {
		StringBuilder sb = new StringBuilder();
		for (XMLComponent xmlComponent : comp)
			sb.append(writetoString(xmlComponent, prettyprint, html)).append(System.lineSeparator());
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	public static String writetoString(XMLStringComponent scomp, boolean prettyprint, boolean html) {
		return XMLUtil.convertToML(scomp.getName());
	}

	public static String writetoStringInternal(List<XMLComponent> componets, boolean prettyprint, boolean html) {
		StringBuilder sb = new StringBuilder();
		for (XMLComponent xmlComponent: componets)
			sb.append(writetoStringInternal(xmlComponent, prettyprint, html)).append(System.lineSeparator());
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	public static String writetoStringInternal(XMLComponent comp, boolean prettyprint, boolean html) {
		String s = "<" + comp.getName()
		+ (comp.getParams().size() != 0 ? " " + XMLUtil.AttributestoStringInternal(comp.getParams(), !html)
		: "")
		+ (comp.isCompound() ? ">" : "/>");
		if (comp.isCompound()) {
			StringBuilder sb = new StringBuilder();
			sb.append(s);
			for (XMLComponent comp2: comp.getChilds())
				sb.append(prettyprint ? System.lineSeparator() + "	"
						: ""
						+ comp2.toStringInternal(prettyprint, html).replace(System.lineSeparator(),
								System.lineSeparator() + "	"));
			sb.append(System.lineSeparator());
			sb.append("</" + comp.getName() + ">");
			s = sb.toString();
		}
		return s;
	}

	public static String writetoStringInternal(XMLComponent[] comp, boolean prettyprint, boolean html) {
		StringBuilder sb = new StringBuilder();
		for (XMLComponent xmlComponent: comp)
			sb.append(writetoStringInternal(xmlComponent, prettyprint, html)).append(System.lineSeparator());
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	public static String writetoStringInternal(XMLStringComponent scomp, boolean prettyprint, boolean html) {
		return scomp.getName();
	}

	private void writeList(List<String> l) throws IOException {
		for (String string : l) {
			write(string);
			newLine();
		}
	}

	@Override
	public void close() throws IOException {
		super.close();
	}

	@Override
	public void flush() throws IOException {
		super.flush();
	}

	public void write(XMLComponent comp, boolean prettyprint, boolean html) throws IOException {
		String s = writetoString(comp, prettyprint, html);
		List<String> l = new ArrayList<>();
		l.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		l.addAll(XMLUtil.convert(s));
		writeList(l);
	}

	public void write(XMLComponent[] comp, boolean prettyprint, boolean html) throws IOException {
		String s = writetoString(comp, prettyprint, html);
		List<String> l = new ArrayList<>();
		l.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		l.addAll(XMLUtil.convert(s));
		writeList(l);
	}

}
