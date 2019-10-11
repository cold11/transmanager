package com.cold.tmx;

import com.cold.vo.TmxEntity;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/9/20 10:14
 * @Description:
 */
public class WriteTmx {
    private SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
    public void generateTmx(String srcLan, String tgtLan, List<TmxEntity> tmxEntities,String saveFile){
        try {

            Document document = DocumentHelper.createDocument();
            Element rootElement = document.addElement("tmx");
            rootElement.addAttribute("version", "1.0");
            Element headerElement = rootElement.addElement("header");
            headerElement.addAttribute("creationtool", "transda Aligner");
            headerElement.addAttribute("segtype", "sentence");
            headerElement.addAttribute("adminlang", "ZH-CN");
            headerElement.addAttribute("srclang", srcLan);
            headerElement.addAttribute("datatype", "xliff");
            headerElement.addAttribute("creationdate", df.format(new Date()));
            headerElement.addAttribute("creationid", "transda");
            Element bodyElement = rootElement.addElement("body");
            tmxEntities.forEach(tmxEntity -> {
                createTUElement(bodyElement, srcLan, tgtLan, tmxEntity.getSource(),tmxEntity.getTranslation());
            });
            write(saveFile, document);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void createTUElement(Element parentElement,String srcLan,String tgtLan, String src, String tgt){
        Element tuElement = parentElement.addElement("tu");
        tuElement.addAttribute("creationdate", df.format(new Date()));
        tuElement.addAttribute("creationid", "transda");

        Element tuvElement = tuElement.addElement("tuv");
        tuvElement.addAttribute("xml:lang", srcLan);
        tuvElement.addElement("seg").setText(src);

        Element tuvTgtElement = tuElement.addElement("tuv");
        tuvTgtElement.addAttribute("xml:lang", tgtLan);
        tuvTgtElement.addElement("seg").setText(tgt);
    }

    private void write(String filePath, Document document){
        OutputFormat format = OutputFormat.createPrettyPrint();
        try {
            StandaloneWriter writer = new StandaloneWriter(new FileOutputStream(filePath),format);
            writer.write(document);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}