package com.zzzmode.tieba.signrank.utils;

import com.zzzmode.tieba.signrank.UserInfo;
import com.zzzmode.tieba.signrank.result.PageResult;

import java.util.*;

/**
 * Created by zl on 15/1/19.
 */
public class Utils {


    public static Set<UserInfo> mergeUserResult(Comparator<UserInfo> sort,PageResult... res ){
        if(res != null){
            Set<UserInfo> data=null;
            if(sort != null){
                data=new TreeSet<>(sort);
            }else {
                data=new HashSet<>();
            }

            for(PageResult page:res){
                if(page != null){
                    data.addAll(page.getParseResult());
                }
            }
            return data;
        }

        return null;
    }
}
