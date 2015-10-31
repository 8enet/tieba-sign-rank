package com.zzzmode.tieba.signrank.result;

import com.zzzmode.tieba.signrank.UserInfo;

import java.util.regex.Pattern;

/**
 * Created by zl on 15/1/19.
 */
public abstract class SampleResult<T> {

    public SampleResult(){}

    public SampleResult(int ignoreDays){
        this.ignoreDays=ignoreDays;
    }

    protected static final Pattern sRegEx_d=Pattern.compile("[^\\d]");

    protected boolean canNext=false;
    protected String nextUrl=null;
    protected T result;

    protected int ignoreDays;

    public void setIgnoreDays(int ignoreDays) {
        this.ignoreDays = ignoreDays;
    }


    protected boolean hasAdd(UserInfo info){
        if(info == null)
            return false;
        return info.getSignDays() > ignoreDays;
    }
}
