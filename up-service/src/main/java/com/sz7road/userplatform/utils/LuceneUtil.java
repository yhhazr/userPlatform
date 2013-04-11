package com.sz7road.userplatform.utils;

import com.sz7road.userplatform.pojos.FaqObject;
import com.sz7road.userplatform.service.FaqService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 12-8-20
 * Time: 下午4:41
 * 全文检索的搜索类
 */
public class LuceneUtil {


    private static final String INDEXDIR = "D:\\lucene1";

    private static final Logger log = LoggerFactory.getLogger(LuceneUtil.class);

    public static void buildIndex(List<FaqObject> faqObjectList) {
        IndexWriter indexWriter = null;
        try {
            //存放索引的位置
            Directory indexDir = new SimpleFSDirectory(new File(INDEXDIR));

            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);

            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, analyzer);
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

            indexWriter = new IndexWriter(indexDir, indexWriterConfig);

            if (faqObjectList != null && !faqObjectList.isEmpty()) {
                for (FaqObject faqObject : faqObjectList) {
                    Document doc = new Document();

                    doc.add(new Field("id", String.valueOf(faqObject.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));

                    doc.add(new Field("question", faqObject.getQuestion(), Field.Store.YES, Field.Index.ANALYZED));

                    doc.add(new Field("answer", faqObject.getAnswer(), Field.Store.YES, Field.Index.ANALYZED));

                    indexWriter.addDocument(doc);
                }

                log.info("构建Faq索引成功！");
            }

        } catch (IOException e) {
            log.error("构建Faq索引异常！");
            e.printStackTrace();
        } finally {
            try {
                indexWriter.commit();
                indexWriter.optimize();
                indexWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public static List<FaqObject> search(String keyWord) {
        List<FaqObject> faqObjectList = new ArrayList<FaqObject>();
        try {
            Directory indexDir = new SimpleFSDirectory(new File(INDEXDIR));

            IndexReader indexReader = IndexReader.open(indexDir);

            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            String[] fields = {"question", "answer"};

            QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_36, fields, new SimpleAnalyzer(Version.LUCENE_36));

            Query query = queryParser.parse(keyWord);

            TopDocs topDocs = indexSearcher.search(query, 100);

            Formatter formatter = new SimpleHTMLFormatter("<span style=\"color:red\">", "</span>");

            Scorer scorer = new QueryScorer(query);

            Highlighter highlighter = new Highlighter(formatter, scorer);

            Fragmenter fragmenter = new SimpleFragmenter(100);

            highlighter.setTextFragmenter(fragmenter);

            int numTotalHits = topDocs.totalHits;
            System.out.println("总共有【" + numTotalHits + "】条结果");
            System.out.println(topDocs.scoreDocs.length);


            for (ScoreDoc doc : topDocs.scoreDocs) {
                Document document = indexSearcher.doc(doc.doc);
                int id = Integer.parseInt(document.get("id"));
                String question = document.get("question");
                TokenStream tokenStream = new SmartChineseAnalyzer(Version.LUCENE_36).tokenStream("token", new StringReader(question));
                String answer = document.get("answer");
                log.info("问题：" + question + "答案：" + answer);

                FaqObject faqObject = new FaqObject();
                faqObject.setId(id);
                faqObject.setQuestion(highlighter.getBestFragment(tokenStream, question));
                faqObject.setAnswer(answer);
                faqObjectList.add(faqObject);
            }


//
//                if(hc==null){//如果无结果那么返回原文的前50个字符
//                        hc=content.substring(0,Math.min(50,content.length()));
//                    //    Field contentField=doc.getFieldable("content");
//                    }
//                Field contentField=(Field) doc.getFieldable("content");
//                contentField.setValue(hc);
//                  doc.getField("content").setValue(hc);
//                System.out.println(doc.get("content"));
//
//                TokenStream ts=a4.tokenStream("content", new StringReader(content));
//                   System.out.println("token: "+ts.getAttribute(String.class).toString());
//                OffsetAttribute offsetAttribute = ts.getAttribute(OffsetAttribute.class);
//                TermAttribute termAttribute = ts.getAttribute(TermAttribute.class);
//                while (ts.incrementToken()) {
//                        int startOffset = offsetAttribute.startOffset();
//                        int endOffset = offsetAttribute.endOffset();
//                        String term = termAttribute.term();
//                        //System.out.println(term);
//                    }


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return faqObjectList;
    }


    public static void main(String[] args) {
        FaqService faqService = new FaqService();
        List<FaqObject> faqObjectList = faqService.fullTextQuery("");
        if (faqObjectList != null) LuceneUtil.buildIndex(faqObjectList);
    }


}
