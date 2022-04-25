package com.sterndu.xml;
import java.util.*;
import org.w3c.dom.Document;

public class XMLFile {

	private List<XMLComponent> components;
	private List<String> list;

	XMLFile() {
	}

	public XMLFile(@SuppressWarnings("exports") Document doc) {
		setComponents(XMLUtil.transformtoComp(doc));
	}

	public XMLFile(List<String> list) {
		setList(XMLUtil.check(list));
	}

	public XMLFile(XMLComponent comp) {
		components = new ArrayList<>();
		components.add(comp);
		list = XMLUtil.check(XMLUtil.convert(XMLWriter.writetoString(comp, false, false)));
	}

	public XMLFile(XMLComponent[] comp) {
		components = new ArrayList<>();
		components.addAll(Arrays.asList(comp));
		list = XMLUtil.check(XMLUtil.convert(XMLWriter.writetoString(comp, false, false)));
	}

	void setComponents(List<XMLComponent> componets) {
		components = componets;
	}

	public XMLComponent getComponet(int index) {
		return components.get(index);
	}

	// public String toStringA() {
	// StringBuilder sb = new StringBuilder();
	// sb.append("[");
	// componets.forEach(new Consumer<XMLComponent>() {
	//
	// @Override
	// public void accept(XMLComponent t) {
	// sb.append(t.toStringA()).append(",");
	// }
	// });
	// sb.append("]");
	// return sb.toString();
	// }

	public List<XMLComponent> getComponets() {
		return components;
	}

	public List<String> getList() {
		return list;
	}

	public void init() {
		components = new ArrayList<>();
		components.addAll(XMLUtil.getComponents(list));
	}

	public void setList(List<String> list) {
		this.list = list;
		init();
	}

	@Override
	public String toString() {
		return XMLWriter.writetoString(components, true, false);
	}

	public String toString(boolean prettyprint, boolean html) {
		return XMLWriter.writetoString(components, prettyprint, html);
	}

}
