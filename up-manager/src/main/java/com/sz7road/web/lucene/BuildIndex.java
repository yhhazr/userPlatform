package com.sz7road.web.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-20
 * Time: 上午11:12
 * 建立索引类
 */
public class BuildIndex {

    private static final String SOURCEDIR = "E:\\indexSource";

    private static final String INDEXDIR = "E:\\index";

    private static Logger log = LoggerFactory.getLogger(BuildIndex.class);

    public boolean build() {
        //1, 构造生成索引的源目录
        File indexSourceDir = new File(SOURCEDIR);
        File[] dataFiles = indexSourceDir.listFiles();
        try {
            //2，构造存放索引的目录。
            Directory indexDir = new SimpleFSDirectory(new File(INDEXDIR));
            //3,构造分析器，写索引
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
            IndexWriter indexWriter = new IndexWriter(indexDir, analyzer, new IndexWriter.MaxFieldLength(1000));

            for (File file : dataFiles) {
                Document document = new Document();

                Reader reader = new FileReader(file);

                document.add(new Field("path", file.getAbsolutePath(), Field.Store.YES, Field.Index.NO));
                document.add(new Field("contents", reader));

                indexWriter.addDocument(document);
            }
            indexWriter.optimize();
            indexWriter.close();

            log.info("索引建立完毕");
        } catch (Exception e) {
            log.error("建立索引异常" + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void search(String keyWord) {
        File indexDir = new File(INDEXDIR);

        try {

            FSDirectory fsDirectory = new SimpleFSDirectory(indexDir);

            IndexSearcher indexSearcher = new IndexSearcher(fsDirectory);

            Term term = new Term("contents", keyWord.toLowerCase());

            TermQuery termQuery = new TermQuery(term);

            TopDocs topDocs = indexSearcher.search(termQuery, 100);

            for (ScoreDoc doc : topDocs.scoreDocs) {
                Document field = indexSearcher.doc(doc.doc);

                log.info("搜索到的结果是：路径：" + field.get("path") + "频度：" + doc.score);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }


    public static void main(String[] args) {
        BuildIndex buildIndex = new BuildIndex();

        buildIndex.build();
        buildIndex.search("lucene");
    }


}
