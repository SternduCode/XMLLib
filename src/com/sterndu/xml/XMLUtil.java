package com.sterndu.xml;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import com.sterndu.util.Util;
import com.sterndu.util.interfaces.ThrowingFunction;

public class XMLUtil {

	private static ThrowingFunction<ListIterator<String>, XMLComponent, XMLParseException> f = null;

	private static void getAndAddParams(XMLComponent c, String params) {
		String[] contents = params.split("(?<=[\"']) ");
		String store = "";
		Character ch = null;
		for (String content: contents) {
			if (!store.equals("")) if (content.contains(ch + "")) {
				content = store + content;
				store = "";
				ch = null;
			} else {
				store = store + content;
				continue;
			}
			String[] pair = content.split("=", 2);
			if (pair.length > 1) try {
				ch = pair[1].charAt(0);
				if (ch == '"' || ch == '\'') {
					if (pair[1].indexOf(ch) == pair[1].lastIndexOf(ch)) {
						store = content;
						continue;
					}
				} else ch = null;
				String val = convertfromML(pair[1].substring(1, pair[1].length() - 1));
				c.addParam(pair[0], val);
			} catch (StringIndexOutOfBoundsException e) {
				e.initCause(new StringIndexOutOfBoundsException(content));
				e.printStackTrace();
			}
			else c.addParam(pair[0], "true");
		}
	}

	private static Object normalize(File f, boolean bigfile) {
		File f2 = new File("./" + Thread.currentThread().getName() + ".xml");
		List<String> lis = new ArrayList<>();
		if (!f2.exists() && bigfile)
			try {
				f2.createNewFile();
				f2.deleteOnExit();
			} catch (IOException e) {
				e.printStackTrace();
			}
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			BufferedWriter bw = null;
			if (bigfile)
				bw = new BufferedWriter(new FileWriter(f2));
			String line = "";
			while ((line = br.readLine()) != null) {
				if (line.contains("=")) {
					String[] str = line.split("=");
					for (String string : str) {
						String[] strb = string.split(" ");
						String str2 = strb[strb.length - 1];
						try {
							Integer.parseInt(str2);
							String rep = " " + str2 + "=";
							char[] c = str2.toCharArray();
							StringBuilder sb = new StringBuilder();
							for (char c2 : c)
								sb.append(getWfZC(c2));
							line = line.replace(rep, " " + sb.toString() + "=");
						} catch (NumberFormatException e) {
						}
					}
				}
				//				if (line.indexOf("<") != line.lastIndexOf("<")) {
				//					StringBuilder sb = new StringBuilder();
				//					String[] sp = line.split("<");
				//					sb.append(sp[0] + "<" + sp[1]);
				//					for (int i = 2; i < sp.length; i++) {
				//						sb.append("LESSTHAN");
				//						sb.append(sp[i]);
				//					}
				//					line = sb.toString();
				//				}
				//				if (line.indexOf(">") != line.lastIndexOf(">")) {
				//					List<String> sb = new LinkedList<>();
				//					String[] sp = line.split(">");
				//					sb.add(sp[sp.length - 2] + ">" + sp[sp.length - 1]);
				//					for (int i = sp.length - 3; i >= 0; i--) {
				//						sb.add("GREATERTHAN");
				//						sb.add(sp[i]);
				//					}
				//					StringBuilder sb2 = new StringBuilder();
				//					for (int i = sb.size() - 1; i >= 0; i--)
				//						sb2.append(sb.get(i));
				//					line = sb2.toString();
				//				}

				if (bigfile) {
					bw.write(line);
					bw.newLine();
				} else
					lis.add(line);
			}
			br.close();
			if (bigfile)
				bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (bigfile)
			return f2;
		else
			return lis;
	}

	private static XMLComponent transform(Node n) {
		XMLComponent comp = new XMLComponent(n.getNodeName());
		if (n.hasAttributes())
			for (int i1 = 0; i1 < n.getAttributes().getLength(); i1++) {
				Node attr = n.getAttributes().item(i1);
				comp.addParam(attr.getNodeName(), attr.getNodeValue());
			}
		if (n.hasChildNodes())
			for (int i1 = 0; i1 < n.getChildNodes().getLength(); i1++) {
				Node child = n.getChildNodes().item(i1);
				if (!child.getNodeName().startsWith("#"))
					comp.addChild(transform(child));
			}
		return comp;
	}

	public static String AttributestoString(Map<String,String> params, boolean writebools) {
		Map<String,String> map = new LinkedHashMap<>();
		Set<Entry<String, String>> set = params.entrySet();
		for (Entry<String, String> entry: set) {
			String key = entry.getKey();
			key = convertToML(key);
			String val = entry.getValue();
			val = convertToML(val);
			map.put(key, val);
		}
		String out = Util.MaptoString(map, ' ');
		return writebools?out:out.replace("=\"true\"","").replaceAll(" (\\w+)=\"false\"","");
	}

	public static String AttributestoStringInternal(Map<String, String> params, boolean writebools) {
		String out = Util.MaptoString(params, ' ');
		return writebools ? out : out.replace("=\"true\"", "").replaceAll(" (\\w+)=\"false\"", "");
	}

	public static String AttributestoStringOld(Map<String, String> params, boolean writebools) {
		Map<String, String> map = new LinkedHashMap<>();
		Set<Entry<String, String>> set = params.entrySet();
		for (Entry<String, String> entry : set) {
			String key = entry.getKey();
			key = getWfZC(key);
			String val = entry.getValue();
			val = getWfZC(val);
			map.put(key, val);
		}
		String out = Util.MaptoString(map, ' ');
		return writebools ? out : out.replace("=\"true\"", "").replaceAll(" (\\w+)=\"false\"", "");
	}

	public static List<String> check(List<String> list) {
		List<String> l2 = new ArrayList<>();
		list.forEach(new Consumer<String>() {

			private List<String> cut(String t) {
				List<String> l = Arrays.asList(t.split("<"));
				for (String string : l)
					string = "<" + string;
				return l;
			}

			@Override
			public void accept(String t) {
				if (!t.equals(""))
					if (t.lastIndexOf("<") != t.indexOf("<"))
						l2.addAll(cut(t));
					else
						l2.add(t);
			}
		});
		return delTab(l2);
	}

	public static List<String> convert(String str) {
		String[] str2 = new String[1];
		if (str.contains(System.lineSeparator()))
			str2 = str.split(System.lineSeparator());
		else
			str2[0] = str;
		return Arrays.asList(str2);
	}

	public static String convertfromML(String c2) {
		if (c2.contains("&lt;"))
			c2 = c2.replace("&lt;", "<");
		if (c2.contains("&gt;"))
			c2 = c2.replace("&gt;", ">");
		if (c2.contains("&apos;"))
			c2 = c2.replace("&apos;", "'");
		if (c2.contains("&amp;"))
			c2 = c2.replace("&amp;", "&");
		return c2;
	}

	public static String convertToML(String c2) {
		if (c2.contains("&"))
			c2 = c2.replace("&", "&amp;");
		if (c2.contains("<"))
			c2 = c2.replace("<", "&lt;");
		if (c2.contains(">"))
			c2 = c2.replace(">", "&gt;");
		if (c2.contains("'"))
			c2 = c2.replace("'", "&apos;");

		return c2;
	}

	public static List<String> delTab(List<String> l2) {
		List<String> l3 = new ArrayList<>();
		for (String string : l2) {
			while (string.startsWith("	"))
				string = string.substring(1);
			while (string.startsWith(" "))
				string = string.substring(1);
			if (!string.equals(""))
				l3.add(string);
		}
		return l3;
	}

	public static List<XMLComponent> getComponents(List<String> list) {
		if (list.size() > 0) {
			int i = 0;
			String s1 = list.get(i);
			List<XMLComponent> li = new ArrayList<>();
			boolean isCompound = !s1.contains("/");
			String name = "";
			Map<String, String> params = new LinkedHashMap<>();
			if (s1.contains(" ")) {
				String[] s2 = s1.substring(1, s1.length() - 1).split(" ");
				name = s2[0];
				for (int i1 = 1; i1 < s2.length; i1++) {
					String s3 = s2[i1];
					char[] c1 = s3.toCharArray();
					String paramname = "";
					String paramvalue = "";
					boolean namecompleted = false;
					int valuecompleted = -2;
					for (char c : c1)
						if (!namecompleted) {
							if (c == '=')
								namecompleted = true;
							else
								paramname = paramname + c;
						} else if (c == '"')
							valuecompleted += 1;
						else if (c != '/')
							paramvalue = paramvalue + c;
					while (valuecompleted < 0) {
						paramvalue = paramvalue + " ";
						i1++;
						s3 = s2[i1];
						if (s3.endsWith("\"")) {
							valuecompleted += 1;
							paramvalue = paramvalue + s3.substring(0, s3.length() - 1);
						} else
							paramvalue = paramvalue + s3;
					}
					params.put(paramname, paramvalue);
				}
			} else {
				System.out.println(s1);
				name = s1.substring(1, s1.length() - 1);
				if (name.endsWith("/"))
					name = name.substring(0, name.length() - 1);
			}
			List<XMLComponent> value = new ArrayList<>();
			if (isCompound) {
				List<String> value3 = new ArrayList<>();
				int b = 0;
				s1 = list.get(i + 1);
				for (i += 2; i < list.size(); i++) {
					value3.add(s1);
					if (s1.equals("<" + name + ">"))
						b++;
					else if (s1.equals("</" + name + ">"))
						if (b == 0)
							break;
						else
							b--;
					s1 = list.get(i);
				}
				if (!s1.equals("</" + name + ">"))
					value3.add(s1);
				if (value.size() > 1) {
					List<String> value2 = new ArrayList<>();
					for (int j = 0; j < value.size() - 2; j++)
						value2.add(value3.get(j));
					value3 = value2;
					i--;
				}
				value = getComponents(value3);
			} else
				i++;
			li.add(new XMLComponent(name, params, value));
			if (i < list.size())
				li.addAll(getComponents(list.subList(i, list.size())));
			return li;
		} else
			return new ArrayList<>();
	}

	public static String getWfZC(char c2) {
		switch (c2) {
			case '0':
				return "ZERO";
			case '1':
				return "ONE";
			case '2':
				return "TWO";
			case '3':
				return "THREE";
			case '4':
				return "FOUR";
			case '5':
				return "FIVE";
			case '6':
				return "SIX";
			case '7':
				return "SEVEN";
			case '8':
				return "EIGHT";
			case '9':
				return "NINE";
		}
		return null;
	}

	public static String getWfZC(String c2) {
		String str = "";
		while (!c2.equals(""))
			if (c2.startsWith("ZERO")) {
				c2 = c2.replaceFirst("ZERO", "");
				str = str + "0";
			} else if (c2.startsWith("ONE")) {
				c2 = c2.replaceFirst("ONE", "");
				str = str + "1";
			} else if (c2.startsWith("TWO")) {
				c2 = c2.replaceFirst("TWO", "");
				str = str + "2";
			} else if (c2.startsWith("THREE")) {
				c2 = c2.replaceFirst("THREE", "");
				str = str + "3";
			} else if (c2.startsWith("FOUR")) {
				c2 = c2.replaceFirst("FOUR", "");
				str = str + "4";
			} else if (c2.startsWith("FIVE")) {
				c2 = c2.replaceFirst("FIVE", "");
				str = str + "5";
			} else if (c2.startsWith("SIX")) {
				c2 = c2.replaceFirst("SIX", "");
				str = str + "6";
			} else if (c2.startsWith("SEVEN")) {
				c2 = c2.replaceFirst("SEVEN", "");
				str = str + "7";
			} else if (c2.startsWith("EIGHT")) {
				c2 = c2.replaceFirst("EIGHT", "");
				str = str + "8";
			} else if (c2.startsWith("NINE")) {
				c2 = c2.replaceFirst("NINE", "");
				str = str + "9";
			} else {
				if (c2.contains("&lt;"))
					c2 = c2.replace("&lt;", "LESSTHAN");
				if (c2.contains("&gt;"))
					c2 = c2.replace("&gt;", "GREATERTHAN");
				if (c2.contains("&lt;"))
					c2 = c2.replace("&lt;", "<");
				if (c2.contains("&gt;"))
					c2 = c2.replace("&gt;", ">");
				if (c2.contains("&apos;"))
					c2 = c2.replace("&apos;", "'");
				if (c2.contains("&amp;"))
					c2 = c2.replace("&amp;", "&");
				return c2;
			}
		return str;
	}

	public static List<XMLComponent> parseFile(File f) throws XMLParseException {
		try {
			return parseInputStream(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<XMLComponent> parseInputStream(InputStream inputStream)
			throws XMLParseException {
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		try {
			String line = "", str = null;
			List<String> finall = new ArrayList<>();
			while ((str = br.readLine()) != null) line = line + "\n" + str.replace("	", "");
			if (line.contains("?>")) line = line.substring(line.indexOf("?>") + 2);
			String[] comps = line.split("<");
			List<String> acomps = new ArrayList<>();
			Arrays.asList(comps).forEach(s -> {
				s = s.replace("	", "").replace("\n", "");
				if (s.toCharArray().length == 0)
					return;
				if (s.lastIndexOf(">") == s.length()) acomps.add("<" + s);
				else {
					String[] back = ("<" + s).split(">");
					acomps.addAll(
							Arrays.asList(back).parallelStream().map(s1 -> s1.charAt(0) == '<' ? s1 + ">" : s1)
							.collect(Collectors.toList()));
				}
			});
			finall.addAll(acomps);
			f = i -> {
				String c = i.next();
				if (c.contains("<") & c.contains(">")) {
					if (c.charAt(0) == '/') throw new XMLParseException("Wrong XMLSyntax");
					if (c.charAt(c.length() - 2) == '/') {
						String w = c.substring(1, c.length() - 2);
						String[] contents = w.split(" ", 2);
						XMLComponent comp = new XMLComponent(contents[0]);
						if (contents.length > 1)
							getAndAddParams(comp, contents[1]);
						return comp;
					} else {
						String w = c.substring(1, c.length() - 1);
						String[] part = w.split(" ", 2);
						XMLComponent comp = new XMLComponent(part[0]);
						if (part.length > 1) getAndAddParams(comp, part[1]);
						while (true) {
							String val = i.next();
							try {
								if (val.charAt(1) == '/') {
									if (!val.startsWith("/" + comp.getName(), 1)) i.previous();
									break;
								} else {
									i.previous();
									comp.addChild(f.apply(i));
									if (!i.hasNext()) break;
								}
							} catch (StringIndexOutOfBoundsException e) {
								System.out.println("val: " + val.replace("\n", ""));
							}
						}
						return comp;
					}
				} else return new XMLStringComponent(convertfromML(c.replace("\n", "")));
			};
			ListIterator<String> it = finall.listIterator();
			List<XMLComponent> li = new ArrayList<>();
			while (it.hasNext())
				li.add(f.apply(it));
			return li;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("exports")
	public static Document parseOld(File f) {
		if (f.length() > 0)
			try {
				boolean bigfile = f.length() > Integer.MAX_VALUE - 1000000;
				Object o = normalize(f, bigfile);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = null;
				if (bigfile)
					doc = dBuilder.parse((File) o);
				else {
					StringBuilder sb = new StringBuilder();
					((List<?>) o).forEach(e -> sb.append(e));
					ByteArrayInputStream bis = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
					doc = dBuilder.parse(bis);
				}
				doc.getDocumentElement().normalize();
				if (bigfile)
					((File) o).delete();
				return doc;
			} catch (IOException | SAXException | ParserConfigurationException e) {
				e.printStackTrace();
				return null;
			}
		else
			return null;

	}

	public static XMLFile transform(@SuppressWarnings("exports") Document doc) {
		if (doc != null) {
			List<XMLComponent> nodes = transformtoComp(doc);
			XMLFile file = new XMLFile();
			file.setComponents(nodes);
			return file;
		} else
			return null;
	}

	public static List<XMLComponent> transformtoComp(@SuppressWarnings("exports") Document doc) {
		if (doc != null) {
			List<XMLComponent> nodes = new ArrayList<>();
			for (int i = 0; i < doc.getChildNodes().getLength(); i++) {
				Node n = doc.getChildNodes().item(i);
				if (!n.getNodeName().startsWith("#")) {
					XMLComponent comp = new XMLComponent(n.getNodeName());
					if (n.hasAttributes())
						for (int i1 = 0; i1 < n.getAttributes().getLength(); i1++) {
							Node attr = n.getAttributes().item(i1);
							comp.addParam(attr.getNodeName(), attr.getNodeValue());
						}
					if (n.hasChildNodes())
						for (int i1 = 0; i1 < n.getChildNodes().getLength(); i1++) {
							Node child = n.getChildNodes().item(i1);
							if (!child.getNodeName().startsWith("#"))
								comp.addChild(transform(child));
						}
					nodes.add(comp);
				}
			}
			return nodes;
		} else
			return null;
	}

}
