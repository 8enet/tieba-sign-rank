package com.zzzmode.tieba.signrank.result;



import java.util.Set;

/**
 * Created by zl on 15/1/19.
 */
public interface PageResult<T> {

    boolean hasNext();

    Set<T> getParseResult();

    String getNextUrl();



}
