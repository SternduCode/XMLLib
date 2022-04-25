package com.sterndu.xml;

import java.io.File;

public class XMLMain {

	public static void main(String[] args) {
		try {
			File f = new File("C:/Users/Silas/AppData/Roaming/Stormworks/data/microprocessors/Radio cont.xml");
			System.out.println(XMLUtil.parseFile(f));

			//FileInputStream fis = new FileInputStream("C:/Users/Silas/AppData/Roaming/Stormworks/data/vehicles/Sender.xml");
			//FileInputStream fis = new FileInputStream("C:/Users/Silas/AppData/Roaming/Stormworks/data/microprocessors/RGB.xml");
			//XMLReader br = new XMLReader(new InputStreamReader(fis));
			//XMLFile xmlfile = new XMLFile(br.readEverything());
			//br.close();
			// XMLFile xmlfile = XMLUtil.transform(XMLUtil.parse(new
			// File("C:/Users/Silas/AppData/Roaming/Stormworks/data/vehicles/a.xml")));
			//XMLFile file = new XMLFile(xmlfile.getTree().getComponets().toArray(new XMLComponent[] {}));
			// String str = XMLWriter.writetoString(xmlfile.getComponets().toArray(new
			// XMLComponent[] {}));
			// XMLWriter bw = new XMLWriter(new FileWriter(new
			// File("C:/Users/Silas/AppData/Roaming/Stormworks/data/vehicles/tmp/tmp.xml")));
			// bw.write(xmlfile.getComponets().toArray(new XMLComponent[] {}));
			// bw.close();
			// System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
