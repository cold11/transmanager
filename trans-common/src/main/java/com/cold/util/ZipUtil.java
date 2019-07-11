package com.cold.util;

import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by ohj on 2017/1/12.
 */
public class ZipUtil {

    public static void zip(String zipFileName,List<String> files) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        for (String fileName : files){
            File file = new File(fileName);
            out.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream in = new FileInputStream(file);
            int b;
            while ( (b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }
        out.close();
    }

    /**
     *
     * @param zipFileName
     * @param inputFile
     * @param base
     * @param filter 过滤的后缀
     * @throws Exception
     */
    public static void zip(String zipFileName, File inputFile, String base,String[] filter) throws Exception {
        FileFilter fileFilter = new NotFileFilter(new SuffixFileFilter(filter));
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        zip(out, inputFile, base,fileFilter);
        out.close();
    }

    public static void zipAll(String zipFileName, File inputFile, String base) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        zipAll(out, inputFile, base);
        out.close();
    }

    public static void zip(ZipOutputStream out, File f, String base,FileFilter fileFilter) throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles(fileFilter);
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName(),fileFilter);
            }
        }else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            int b;
            while ( (b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }
    }

    public static void zipAll(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zipAll(out, fl[i], base + fl[i].getName());
            }
        }else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            int b;
            while ( (b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }
    }

    public static void unZip(String zipFileName,String outputPath) throws Exception{
        ZipInputStream zin=new ZipInputStream(new FileInputStream(zipFileName), Charset.forName("gb2312"));
        BufferedInputStream bin=new BufferedInputStream(zin);
        File fout=null;
        ZipEntry entry;
        while((entry = zin.getNextEntry())!=null && !entry.isDirectory()){
            fout=new File(outputPath,entry.getName());
            if(!fout.exists()){
                (new File(fout.getParent())).mkdirs();
            }
            FileOutputStream out=new FileOutputStream(fout);
            BufferedOutputStream bout=new BufferedOutputStream(out);
            int b;
            while((b=bin.read())!=-1){
                bout.write(b);
            }
            bout.close();
            out.close();
        }
        bin.close();
        zin.close();
    }

    public static void main(String[] args) {
        try {
            unZip("E:\\新建文件夹\\新建文件夹\\J_104142583_6302.zip","E:\\新建文件夹");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            zip("D:\\tcsp\\serverFile\\zh_jp\\20170322003\\test.zip",new File("D:\\tcsp\\serverFile\\zh_jp\\20170322003\\2"),"",new String[]{"xls","xlsx","XLS","XLSX"});
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
