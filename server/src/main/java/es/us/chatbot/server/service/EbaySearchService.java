package es.us.chatbot.server.service;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.Request;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EbaySearchService {

    OkHttpClient client;

    public EbaySearchService() {
        this.client = new OkHttpClient();
    }

    public Map<String, String> ebaySearch(String keywords, Double maxPrice) throws IOException {
        Map<String, String> result = new HashMap<>();

        HttpUrl url = new HttpUrl.Builder()
                                .host("svcs.ebay.com")
                                .scheme("http")
                                .addPathSegments("services/search/FindingService/v1")
                                .addQueryParameter("OPERATION-NAME", "findItemsByKeywords")
                                .addQueryParameter("SERVICE-VERSION", "1.0.0")
                                .addQueryParameter("SECURITY-APPNAME", "Guillerm-cbdchatb-PRD-edc0df799-80a90995")
                                .addQueryParameter("RESPONSE-DATA-FORMAT", "XML")
                                .addQueryParameter("GLOBAL-ID", "EBAY-ES")
                                .addQueryParameter("keywords", keywords)
                                .addQueryParameter("itemFilter(0).name", "MaxPrice")
                                .addQueryParameter("itemFilter(0).value", maxPrice.toString())
                                .addQueryParameter("itemFilter(0).paramName", "Currency")
                                .addQueryParameter("itemFilter(0).paramValue", "EUR")
                                .build();


        Request request = new Request.Builder()
            .url(url)
            .get()
            .build();

        Response response = client.newCall(request).execute();

        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(response.body().byteStream());
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xpath.compile("//item").evaluate(doc, XPathConstants.NODESET);
            int i = 0;
            while (result.size() < 3) {
                result.put(
                    getValue(nodeList.item(i), ".//viewItemURL", xpath),
                    getValue(nodeList.item(i), ".//title", xpath)
                );
                i++;
                if (i > 100) {
                    break;
                }
            }

        } catch (Exception ignore){}

        return result;
    }

    private String getValue(Node n, String expr, XPath xpath) {
        try {
            XPathExpression pathExpr = xpath.compile(expr);
            return (String) pathExpr.evaluate(n,
                XPathConstants.STRING);
        } catch (XPathExpressionException ignore) {}
        return null;
    }
}
