package com.zzzmode.tieba.signrank.result;

import com.zzzmode.tieba.signrank.UserInfo;
import com.zzzmode.tieba.signrank.paser.DocumentPaser;
import org.jsoup.nodes.Document;

import java.util.Set;

/**
 * Created by zl on 15/1/19.
 */
public class SearchResult extends SampleResult implements PageResult<String>,DocumentPaser {




    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Set<String> getParseResult() {
        return null;
    }

    @Override
    public String getNextUrl() {
        return null;
    }

    @Override
    public SearchResult paser(Document document) {
        return this;
    }
}
