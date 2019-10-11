package com.cold.search;

import cn.hutool.core.io.FileUtil;
import com.google.common.collect.Lists;
import net.sf.okapi.common.resource.TextFragment;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/9/6 09:27
 * @Description:
 */
public class SearchTest {
    public static void main(String[] args) {
        String searchDir ="D:\\lucene索引\\mtindex";
        Path dirPath = Paths.get(searchDir);
        String searchText = "Seat lighting system 600 is generally based on lighting system as described herein comprising light source 602 (generates light beam 603) it is directed on the color reflection unit 601 with color reflection unit 601, light beam 603, and by color reflection unit 601 The reflected beams 603, to form one or more illuminating bundle 603A, 603B, these target areas can be overlapped or cannot Overlapping.";
        searchText = "座位照明系统600通常基于在此描述的照明系统，该照明系统包括光源602（产生光束603），其通过颜色反射单元601，光束603和颜色反射单元601被引导到颜色反射单元601上.为了形成一个或多个照明束603A，603B，这些目标区域可以重叠或不能重叠。";
        try {
            List<String> result = Lists.newArrayList();
            Directory directory = FSDirectory.open(dirPath);
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new IKAnalyzer(true);
            QueryParser parser = new QueryParser("src", analyzer);
            Query query = parser.parse(searchText);
            TopDocs topDocs = searcher.search(query, 5000);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            for (ScoreDoc scoreDoc : scoreDocs){
                int docId = scoreDoc.doc;
                Document doc = searcher.doc(docId);
                String text = doc.get("src");
                result.add(text);


            }
            FileUtil.writeLines(result,"D:\\searchres.txt","UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}