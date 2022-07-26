package com.sterndu.xml;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import com.sterndu.util.*;

class XMLParserTest {
	static String mi_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator()
	+ "<microprocessor name=\"Microcontroller\" description=\"No description set.\" hide_in_inventory=\"false\" width=\"2\" length=\"2\" id_counter=\"0\" id_counter_node=\"0\" transform_index=\"0\" sym0=\"0\" sym1=\"0\" sym2=\"0\" sym3=\"0\" sym4=\"0\" sym5=\"0\" sym6=\"0\" sym7=\"0\" sym8=\"0\" sym9=\"0\" sym10=\"0\" sym11=\"0\" sym12=\"0\" sym13=\"0\" sym14=\"0\" sym15=\"0\">"
	+ System.lineSeparator() + "	<nodes/>" + System.lineSeparator() + "	<group id=\"0\">"
	+ System.lineSeparator() + "		<pos x=\"0\" y=\"0\"/>" + System.lineSeparator()
	+ "		<data type=\"0\" name=\"\" desc=\"\">" + System.lineSeparator() + "			<inputs/>"
	+ System.lineSeparator() + "			<outputs/>" + System.lineSeparator() + "		</data>"
	+ System.lineSeparator() + "		<components/>" + System.lineSeparator() + "		<components_bridge/>"
	+ System.lineSeparator() + "		<groups/>" + System.lineSeparator() + "		<component_states/>"
	+ System.lineSeparator() + "		<component_bridge_states/>" + System.lineSeparator()
	+ "		<group_states/>" + System.lineSeparator() + "	</group>" + System.lineSeparator()
	+ "</microprocessor>";

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		try {
			System.out
			.println(XMLUtil
					.parseFile(new File(
							"C:/Users/Silas/AppData/Roaming/Stormworks/data/microprocessors/storage.xml"))
					.get(0).toStringInternal());
			File o = new File("C:/Users/Silas/AppData/Roaming/Stormworks/data/microprocessors/storage2.xml");
			if (!o.exists())
				o.createNewFile();
			XMLComponent c = XMLUtil.parseInputStream(Util.getStringStream(mi_xml)).get(0);
			XMLComponent nodes = c.getChild(0);
			{
				XMLComponent n = new XMLComponent("n");
				n.addParam("id", "1");
				n.addParam("component_id", "1");
				n.addParam("built_slot_index", "0");
				XMLComponent node = new XMLComponent("node");
				node.addParam("orientation", "0");
				node.addParam("label", "Input");
				node.addParam("mode", "1");
				node.addParam("type", "5");
				node.addParam("description", "The input signal to be processed.");
				node.addParam("flags", "0");
				XMLComponent pos = new XMLComponent("position");
				pos.addParam("x", "0");
				pos.addParam("y", "0");
				pos.addParam("z", "0");
				node.addChild(pos);
				n.addChild(node);
				nodes.addChild(node);
			}
			{
				XMLComponent n = new XMLComponent("n");
				n.addParam("id", "2");
				n.addParam("component_id", "2");
				n.addParam("built_slot_index", "0");
				XMLComponent node = new XMLComponent("node");
				node.addParam("orientation", "0");
				node.addParam("label", "Input");
				node.addParam("mode", "0");
				node.addParam("type", "5");
				node.addParam("description", "The input signal to be processed.");
				node.addParam("flags", "0");
				XMLComponent pos = new XMLComponent("position");
				pos.addParam("x", "0");
				pos.addParam("y", "0");
				pos.addParam("z", "1");
				node.addChild(pos);
				n.addChild(node);
				nodes.addChild(node);
			}
			XMLComponent group = c.getChild(1);
			XMLComponent comps = group.getChild(2);
			XMLComponent compsb = group.getChild(3);
			XMLComponent compst = group.getChild(5);
			XMLComponent compbs = group.getChild(6);
			System.exit(0);
			System.setOut(new PrintStreamwithFile(System.out, new File("C:/Users/Silas/Desktop/log.txt")));
			System.setErr(new PrintStreamwithFile(System.err, new File("C:/Users/Silas/Desktop/err.txt")));
			File f = new File("C:\\Users\\Silas\\AppData\\Roaming\\Stormworks\\data\\vehicles/new flix cybertruck.xml");
			long st = System.currentTimeMillis();
			List<XMLComponent> li = XMLUtil.parseFile(f);
			long et = System.currentTimeMillis();
			long diff = et - st;
			st = System.currentTimeMillis();
			List<XMLComponent> li2 = XMLUtil.transformtoComp(XMLUtil.parseOld(f));
			et = System.currentTimeMillis();
			long diff2 = et - st;
			System.out.println(li.get(0));
			System.out.println(li2);
			System.out.println(diff);
			System.out.println(diff2);
			StringBuilder sb = new StringBuilder();
			Files.readAllLines(f.toPath()).forEach(s -> sb.append(s).append("\n"));
			System.out.println(sb.toString().equals(li.get(0).toString()));
			System.out.println(sb.toString().equals(li2.toString().substring(1,
					li2.toString().length() - 1)));
			System.out.flush();
			while (true) {
				Thread.sleep(5);
				if (System.in.available() > 0)
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
