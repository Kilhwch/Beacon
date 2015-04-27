package com.example.kasper.beacon.SupportClasses;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by kasper on 4/13/2015.
 *
 * Parses temperature (currently Curitiba) from Yahoo.
 */
public class Temperature extends AsyncTask<Void, Void, String> {

    public ASyncResponse delegate = null;
    private Exception exception;


    @Override
    protected String doInBackground(Void... params) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.parse("http://weather.yahooapis.com/forecastrss?w=455822&u=c");

            doc.getDocumentElement().normalize();

            NodeList items = doc.getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); i++) {
                Node n = items.item(i);
                if (n.getNodeType() != Node.ELEMENT_NODE) continue;
                Element e = (Element) n;

                NodeList list = e.getElementsByTagName("*");
                Node temperature = list.item(5).getAttributes().item(2); // horrible solution that may cause problems,
                return temperature.getTextContent();                     // but done this way due to easy implementation
            }
        } catch (Exception e) {
            this.exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) delegate.processFinish(result);
        else delegate.processFinish("null");
    }
}
