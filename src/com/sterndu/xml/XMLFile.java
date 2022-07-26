package com.sterndu.xml;
import java.util.*;
import org.w3c.dom.Document;

public class XMLFile {

	private List<XMLComponent> components;

	XMLFile() {
	}

	public XMLFile(@SuppressWarnings("exports") Document doc) {
		setComponents(XMLUtil.transformtoComp(doc));
	}

	public XMLFile(List<XMLComponent> comp) {
		components = new ArrayList<>();
		components.addAll(comp);
	}

	public XMLFile(XMLComponent comp) {
		components = new ArrayList<>();
		components.add(comp);
	}

	public XMLFile(XMLComponent... comp) {
		components = new ArrayList<>();
		components.addAll(Arrays.asList(comp));
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

	@Override
	public String toString() {
		return XMLWriter.writetoString(components, true, false);
	}

	public String toString(boolean prettyprint, boolean html) {
		return XMLWriter.writetoString(components, prettyprint, html);
	}

}
