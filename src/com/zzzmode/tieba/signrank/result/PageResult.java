package com.zzzmode.tieba.signrank.result;

import com.zzzmode.tieba.signrank.UserInfo;

import java.util.Set;

/**
 * Created by zl on 15/1/19.
 */
public interface PageResult {

    boolean hasNext();

    Set<UserInfo> getParseResult();

    String getNextUrl();



}
