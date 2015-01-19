package com.zzzmode.tieba.signrank.result;

import com.zzzmode.tieba.signrank.UserInfo;
import com.zzzmode.tieba.signrank.paser.DocumentPaser;
import org.jsoup.nodes.Document;

import java.util.Set;

/**
 * Created by zl on 15/1/19.
 */
public class SearchResult extends SampleResult implements PageResult<String>,DocumentPaser<PageResult<String>> {




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
    public PageResult<String> paser(Document document) {
        return null;
    }
}
