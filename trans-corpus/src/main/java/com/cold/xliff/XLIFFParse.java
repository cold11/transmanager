package com.cold.xliff;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/8/16 10:17
 * @Description:
 */
@Getter
public class XLIFFParse {

    private Document document;
    public final String XMLNS_PREFIX = "xmlns";
    public final String SDL_PREFIX = "sdl";
    private final Namespace sdlnamespace =  new Namespace("sdl",
            "http://sdl.com/FileTypes/SdlXliff/1.0");
    public void load(String filename){
        SAXReader reader = new SAXReader();
        HashMap xmlMap = new HashMap();
        xmlMap.put(XMLNS_PREFIX, "urn:oasis:names:tc:xliff:document:1.2");
        xmlMap.put(SDL_PREFIX, "http://sdl.com/FileTypes/SdlXliff/1.0");
        reader.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
        try {
            document = reader.read(new File(filename));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public Element getSingleElement(String xpath){
        return (Element) document.selectSingleNode(xpath);
    }
    public Element createSdlElement(String name,Element parent){
        return parent.addElement(new QName(name,sdlnamespace));
    }

    public void createSdlElementAttr(Element element,String attrName,String attrValue) {

        element.addAttribute(attrName, attrValue);

    }

    public void save(String outFile){
        try {
            XMLWriter writer = new XMLWriter( new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
            writer.write(document);
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
    public  void parse() {
        Element root = document.getRootElement();
        //root.addNamespace("xmlns","urn:oasis:names:tc:xliff:document:1.2");
        List<Element> transUnitList = root.selectNodes(".//xmlns:trans-unit");
        for (Element transUnit : transUnitList) {
            String id = transUnit.attributeValue("id", "");
            List<Element> segSourceList = transUnit.selectNodes("./xmlns:seg-source");
            List<Element> targetList = transUnit.selectNodes("./xmlns:target");
            //List<Element> childElements = transUnit.elements();

            segSourceList.forEach(childElement -> {

                List<Text> textElements = childElement.selectNodes(".//text()");
                for (Text textElement : textElements) {
                    System.out.println(textElement.getText() + ">>" + textElement.getUniquePath(childElement));
                }
            });


        }

    }

    public static void readXLIFFElement(Element e){
        Iterator<Element> it = e.elementIterator();
        while (it.hasNext()) {
            Element arrrName = it.next();
            System.out.println("节点名：" + e.getName() + "，节点值：" + e.getTextTrim());
            readXLIFFElement(arrrName);
        }
    }
    public static void readElement(Element e){
        //判断是否有复合内容
        if(e.hasMixedContent()){
//            xliffElement.setName(e.getName());
//            List<Attribute> attributes = e.attributes();
//            List<XLIFFAttribute> xliffAttributes = Lists.newArrayList();
//            for (Attribute attr : attributes) {
//                XLIFFAttribute xliffAttribute = new XLIFFAttribute();
//                xliffAttribute.setName(attr.getName());
//                xliffAttribute.setValue(attr.getValue());
//                xliffAttributes.add(xliffAttribute);
//            }
            Iterator<Element> it = e.elementIterator();
            while (it.hasNext()) {
                Element arrrName = it.next();

                readElement(arrrName);
            }
        }else{
            System.out.println("节点名：" + e.getName() + "，节点值：" + e.getTextTrim());
        }
    }

    public static void read(Element e) {
        if (e.nodeCount() > 0) {
            Iterator<?> it = e.elementIterator();
            while (it.hasNext()) {
                Element ele = (Element) it.next();
                read(ele);
                System.out.println("Element :" + ele.getName() + " Path "
                        + ele.getTextTrim());

                if (ele.attributeCount() > 0) {
                    Iterator<?> ait = ele.attributeIterator();
                    System.out.print("  Data : [ ");
                    while (ait.hasNext()) {
                        Attribute attribute = (Attribute) ait.next();
                        System.out.print(attribute.getName() + " : "
                                + attribute.getData() + "   ");
                    }
                    System.out.print(" ]");
                    System.out.println();
                }
            }
        }

    }

    public static void main(String[] args) {
        String filename = "d:\\xliff\\tt.docx.sdlxliff";
        XLIFFParse xliffParse = new XLIFFParse();
        xliffParse.load(filename);
        xliffParse.parse();
    }
}