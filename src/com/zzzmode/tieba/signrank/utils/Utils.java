package com.zzzmode.tieba.signrank.utils;

import com.zzzmode.tieba.signrank.UserInfo;
import com.zzzmode.tieba.signrank.result.PageResult;

import java.util.*;

/**
 * Created by zl on 15/1/19.
 */
public class Utils {


    public static Set<UserInfo> mergeResult(Set<? extends PageResult> list,Comparator<UserInfo> sort){
        if(list != null){
            Set<UserInfo> data=null;
            if(sort != null){
                data=new TreeSet<>(sort);
            }else {
                data=new HashSet<>();
            }
            System.out.println(list.getClass());
            for(Object page:list){
//                if(page != null){
//                    data.addAll(page.getParseResult());
//                }

                System.out.println(page.getClass());
            }
            return data;


        }

        return null;
    }
}
