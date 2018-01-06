package com.dwidasa.ib.transform;

import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.airline.AirSearchFlight;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by IntelliJ IDEA.
 * User: IBaihaqi
 * Date: 3/21/14
 */
public class XmlParsingUtil {

    public static String prettyFormat(String input, int indent) {
           try
           {
               Source xmlInput = new StreamSource(new StringReader(input));
               StringWriter stringWriter = new StringWriter();
               StreamResult xmlOutput = new StreamResult(stringWriter);
               TransformerFactory transformerFactory = TransformerFactory.newInstance();
               // This statement works with JDK 6
               transformerFactory.setAttribute("indent-number", indent);

               Transformer transformer = transformerFactory.newTransformer();
               transformer.setOutputProperty(OutputKeys.INDENT, "yes");
               transformer.transform(xmlInput, xmlOutput);
               return xmlOutput.getWriter().toString();
           }
           catch (Throwable e)
           {
               return input;
           }
       }

       public static String prettyFormat(String input) {
           return prettyFormat(input, 2);
       }
   	public static void main(String[] args) {
        String json="{\"paxAdult\":2,\"oneWay\":false,\"paxChild\":1,\"toAirport\":\"SUB\",\"paxInfant\":0,\"fromAirport\":\"BDO\",\"airlineCodes\":[\"QZ\"],\"departDate\":\"22 03 2014 00:00:00\",\"returnDate\":\"25 03 2014 00:00:00\",\"sessionId\":null,\"providerCode\":null,\"token\":null,\"appType\":null}";
        AirSearchFlight searchFlight = PojoJsonMapper.fromJson(json, AirSearchFlight.class);
        System.out.println("json=" + json);

   		System.out.println(prettyFormat("<response type=\"OFFICEINFORMATION\"><deposit>0</deposit></response>"));
   	}
}
