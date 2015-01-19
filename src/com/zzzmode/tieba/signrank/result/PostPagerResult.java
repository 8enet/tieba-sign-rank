package com.zzzmode.tieba.signrank.result;

import com.zzzmode.tieba.signrank.UserInfo;
import com.zzzmode.tieba.signrank.paser.DocumentPaser;
import org.jsoup.nodes.Document;

import java.util.Set;

/**
 * Created by zl on 15/1/19.
 */
public class PostPagerResult extends SampleResult implements PageResult<UserInfo>,DocumentPaser<PageResult<Set<UserInfo>>> {
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


    @Override
    public PageResult<Set<UserInfo>> paser(Document document) {
        return null;
    }
}
