package com.zzzmode.tieba.signrank.paser;

import com.zzzmode.tieba.signrank.UserInfo;
import org.jsoup.nodes.Document;

import java.util.Set;

/**
 * Created by zl on 15/1/19.
 */
public class PostPaser implements DocumentPaser<Set<UserInfo>> {
    @Override
    public Set<UserInfo> paser(Document document) {
        return null;
    }
}
