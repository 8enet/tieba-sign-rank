package com.zzzmode.tieba.signrank;

import com.zzzmode.tieba.signrank.result.IndexPagerResult;
import com.zzzmode.tieba.signrank.result.PostPagerResult;
import com.zzzmode.tieba.signrank.utils.MergeManager;
import com.zzzmode.tieba.signrank.utils.Utils;
import com.zzzmode.tieba.signrank.work.SignRankHandler;
import com.zzzmode.tieba.signrank.work.SpiderWork;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by zl on 15/1/5.
 */
public class RankMain {

    public static void main(String[] args){

        try {


            long st=System.currentTimeMillis();

            System.out.println("-------- SignRankHandler --------");

            SignRankHandler handler=new SignRankHandler("java");

            print(handler.getRankTop(5));

            if(true){
                handler.shutdown();
                System.out.println("times : "+(System.currentTimeMillis()-st)+"    count:"+handler.getSpiderCount());
                return;
            }

            Set<IndexPagerResult> indexPageResult = handler.getIndexPageResult(3);
            Set<String> urls=new HashSet<>();
            for (IndexPagerResult result:indexPageResult){
                //System.out.println (result.getPostUrl().size());
                urls.addAll(result.getPostUrl());
            }









        }catch (Exception e){
         e.printStackTrace();
        }finally {

        }
    }


    public static void aaa(){

        System.out.println(System.getProperty("user.timezone"));

        System.out.println(new Date().toLocaleString());
        Calendar calendar=Calendar.getInstance();

        calendar.setTimeZone(TimeZone.getDefault());
        //calendar.add(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,15);
        calendar.set(Calendar.MINUTE,9);
        calendar.set(Calendar.MILLISECOND,0);

        calendar.set(Calendar.SECOND,0);

        System.out.println(calendar.getTimeInMillis());
        System.out.println(new Date(calendar.getTimeInMillis()).toLocaleString());

        System.out.println(TimeUnit.MINUTES.toMillis(30));
    }

    static void print(Collection list){
        if(list != null){
            int i=0;
            for(Object o:list){
                System.out.println(i+"\t"+o);
                i++;
            }
            System.out.println("--------");
        }
    }


    static void print(Map map){
        if(map != null){
            Set<Map.Entry<Object, Object>> set = map.entrySet();
            System.out.println(" map value --> ");
            for(Map.Entry<Object, Object> obj:set){
                System.out.println(obj.getKey()+"\t"+obj.getValue());
            }
            System.out.println("--------");
        }
    }
}
