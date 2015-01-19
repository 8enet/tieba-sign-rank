package com.zzzmode.tieba.signrank.result;

/**
 * Created by zl on 15/1/19.
 */
public abstract class SampleResult<T> {
    protected boolean canNext=false;
    protected String nextUrl=null;
    protected T result;
}
