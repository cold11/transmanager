package com.cold.tmx;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * @Auther: ohj
 * @Date: 2019/9/20 10:43
 * @Description:
 */
public class StandaloneWriter extends XMLWriter {
    public StandaloneWriter(OutputStream out, OutputFormat format)
            throws UnsupportedEncodingException
    {
        super(out,format);
    }

    protected void writeDeclaration()
            throws IOException
    {
        OutputFormat format = getOutputFormat();
        String encoding = format.getEncoding();
        if(!format.isSuppressDeclaration())
        {
            writer.write("<?xml version=\"1.0\"");
            if(encoding.equals("UTF8"))
            {
                if(!format.isOmitEncoding())
                    writer.write(" encoding=\"UTF-8\"");
            } else
            {
                if(!format.isOmitEncoding())
                    writer.write(" encoding=\"" + encoding + "\"");

            }
            writer.write(" standalone=\"yes\"");
            writer.write("?>");
            if(format.isNewLineAfterDeclaration())
                println();
        }
    }
}