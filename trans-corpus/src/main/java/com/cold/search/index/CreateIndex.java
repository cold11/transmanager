package com.cold.search.index;

import com.cold.util.FileUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Auther: ohj
 * @Date: 2019/9/5 16:09
 * @Description:
 */
public class CreateIndex {
    private IndexWriter iwriter;
    public  void createIndex(String searchDir,String contentFile, boolean hasDelete){
        try {
            // 获取索引文件位置
            Path dirPath = Paths.get(searchDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            // 设置索引参数
            Directory directory = FSDirectory.open(dirPath);
            Analyzer analyzer = new IKAnalyzer(true);
            IndexWriterConfig iwConfig = new IndexWriterConfig(analyzer);
            iwriter = new IndexWriter(directory, iwConfig);
            if(hasDelete){
                iwriter.deleteAll();// 删除上次的索引文件，重新生成索引
            }
            readFile(contentFile);
            iwriter.close();
            analyzer.close();
            directory.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void readFile(String filename){
        File file = new File(filename);
        try {
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"UTF-8"),10*1024*1024);
            String cnLine = "";
            int line = 0;
            while((cnLine = reader.readLine()) != null){
                appendDoc(cnLine,"zh");
                if(line%10000==0){
                    System.out.println(cnLine);
                }
                line++;
            }
            fis.close();
            reader.close();
         } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void appendDoc(String content,String language){
        Document document = new Document();
        Field idField = new NumericDocValuesField("id",System.currentTimeMillis());
        Field srcField = new TextField("src",content, Field.Store.YES);
        Field lanField = new StringField("language",language, Field.Store.YES);
        document.add(idField);
        document.add(srcField);
        document.add(lanField);
        try {
            iwriter.addDocument(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String indexDir = "D:\\lucene索引\\mtindex";
        String contentFile = "D:\\语料\\new2-cn-11434571.txt";
        CreateIndex createIndex = new CreateIndex();
        createIndex.createIndex(indexDir,contentFile,true);
        //Fileu
    }
}