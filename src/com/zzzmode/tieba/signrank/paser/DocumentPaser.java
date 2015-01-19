package com.zzzmode.tieba.signrank.paser;

import org.jsoup.nodes.Document;

/**
 * Created by zl on 15/1/19.
 */
public interface DocumentPaser<T> {
    T paser(Document document);

}
