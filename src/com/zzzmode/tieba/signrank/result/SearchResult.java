package com.zzzmode.tieba.signrank.result;

import com.zzzmode.tieba.signrank.UserInfo;
import com.zzzmode.tieba.signrank.paser.DocumentPaser;
import org.jsoup.nodes.Document;

import java.util.Set;

/**
 * Created by zl on 15/1/19.
 */
public class SearchResult extends SampleResult implements PageResult,DocumentPaser<String> {


    @Override
    public String paser(Document document) {
        return null;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Set<UserInfo> getParseResult() {
        return null;
    }

    @Override
    public String getNextUrl() {
        return null;
    }
}
