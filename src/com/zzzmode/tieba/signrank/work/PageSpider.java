package com.zzzmode.tieba.signrank.work;

import com.zzzmode.tieba.signrank.paser.DocumentPaser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zl on 15/1/19.
 */
public class PageSpider<T> implements Callable<T> {

    private static  Logger log ;
    static {
        log= Logger.getLogger("PageSpider");
        log.setLevel(Level.INFO);
    }


    private Object tag;
    private String url;
    private DocumentPaser<T> paser;

    public PageSpider(String url, DocumentPaser<T> paser) {
        this.url = url;
        this.paser = paser;
        if (url == null || paser == null) {
            throw new NullPointerException("Spider url and result paser don't null !!!");
        }
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public T call() throws Exception {
        //log.info(" spider  url :"+url);
        Document document = Jsoup.connect(url).userAgent(Configs.HTTP.USER_AGENT).timeout(Configs.HTTP.CONNECT_TIMEOUT).get();
        return paser.paser(document);
    }
}
