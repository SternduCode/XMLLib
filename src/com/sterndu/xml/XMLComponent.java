package com.sterndu.xml;

import java.util.*;

public class XMLComponent {

	private List<XMLComponent> childs = new ArrayList<>();
	private boolean isCompound;
	private String name;
	private Map<String, String> params;

	public XMLComponent(String name) {
		this.name = name;
		params = new LinkedHashMap<>();
		childs = new ArrayList<>();
	}

	public XMLComponent(String name, Map<String, String> params, List<XMLComponent> value) {
		this.name = name;
		if (params != null)
			this.params = params;
		else
			this.params = new LinkedHashMap<>();
		if (value != null) {
			if (value.size() > 0) {
				childs = value;
				isCompound = true;
			} else
				isCompound = false;
		} else
			childs = new ArrayList<>();
	}

	void setName(String name) {
		this.name = name;
	}

	public XMLComponent addChild(XMLComponent xc) {
		if (!isCompound)
			isCompound = true;
		childs.add(xc);
		return this;
	}

	public XMLComponent addChild(XMLComponent xc, int index) {
		if (!isCompound)
			isCompound = true;
		childs.add(index, xc);
		return this;
	}

	public XMLComponent addParam(String key, String param) {
		params.put(key, param);
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XMLComponent other = (XMLComponent) obj;
		if (isCompound != other.isCompound)
			return false;
		if (!Objects.equals(name, other.name)) {
			return false;
		}
		if (!Objects.equals(params, other.params)) {
			return false;
		}
		if (!Objects.equals(childs, other.childs)) {
			return false;
		}
		return true;
	}

	public XMLComponent getChild(int index) {
		return childs.get(index);
	}

	public List<XMLComponent> getChilds() {
		return childs;
	}

	public String getName() {
		return name;
	}

	public String getParam(String key) {
		return params.get(key);
	}

	public Map<String, String> getParams() {
		return params;
	}

	@Override
	public int hashCode() {
		return Objects.hash(isCompound, name, params, childs);
	}

	public boolean hasParam(String string) {
		return params.containsKey(string);
	}

	public boolean isCompound() {
		return isCompound;
	}

	public XMLComponent removeChild(int index) {
		childs.remove(index);
		if (childs.size() == 0)
			isCompound = false;
		return this;
	}

	public XMLComponent removeChild(XMLComponent xc) {
		childs.remove(xc);
		if (childs.size() == 0)
			isCompound = false;
		return this;
	}

	public XMLComponent removeParam(String key) {
		params.remove(key);
		return this;
	}

	public XMLComponent setChild(XMLComponent xc, int index) {
		try {
			childs.set(index, xc);
		} catch (IndexOutOfBoundsException e) {
		}
		return this;
	}

	public XMLComponent setParam(String key, String param) {
		params.replace(key, param);
		return this;
	}

	@Override
	public String toString() {
		return XMLWriter.writetoString(this, false, false);
	}

	public String toString(boolean prettyprint, boolean html) {
		return XMLWriter.writetoString(this, prettyprint, html);
	}

	public String toStringInternal() {
		return XMLWriter.writetoStringInternal(this, false, false);
	}

	public String toStringInternal(boolean prettyprint, boolean html) {
		return XMLWriter.writetoStringInternal(this, prettyprint, html);
	}

	//	public String toStringB() {
	//		return name
	//				+ (!childs.isEmpty()
	//						? System.lineSeparator() + "	"
	//						+ (childs.toString().contains(System.lineSeparator())
	//								? childs.toString().replace(System.lineSeparator(), System.lineSeparator() + "	")
	//										: childs)
	//						: "");
	//	}
	//
	//	public String toStringA() {
	//		if (isCompound && childs != null) {
	//			StringBuilder sb = new StringBuilder();
	//			for (XMLComponent xmlComponent : childs) {
	//				sb.append("," + xmlComponent.toString()).append(System.lineSeparator());
	//			}
	//			return name + System.lineSeparator() + sb.toString();
	//		} else
	//			return name;
	//	}

}
