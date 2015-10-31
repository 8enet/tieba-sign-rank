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
            handler.setIgnoreLowDays(500);

            Set<IndexPagerResult> indexPageResult = handler.getIndexPageResult(3);


            Set<String> urls=new HashSet<>();
            for (IndexPagerResult result:indexPageResult){
                urls.addAll(result.getPostUrl());
            }

            System.out.println(" all urls : "+urls.size());
            System.out.println("----------------");


            Set<UserInfo> postPageResult = handler.getPostPageResult(urls);

//            MergeManager<UserInfo> mergeManager=new MergeManager<>(UserInfo.sortBySignDays);
//
//            for (UserInfo result:postPageResult){
//                //System.out.println(result.hasNext() +"     "+result.getNextUrl());
//                //print(result.getParseResult());
//                mergeManager.addPart(result);
//            }
//
//            print(mergeManager.merges());

            //Set<UserInfo> rankTop = handler.getRankTop(5);



            Set<UserInfo> res= new TreeSet(UserInfo.sortBySignDays);
            res.addAll(postPageResult);
            print(res);

            long s = System.currentTimeMillis() - st;

            System.out.println("seconds time: " + ((s / 1000l))+"s");
            handler.shutdown();

        }catch (Exception e){
         e.printStackTrace();
        }finally {

        }
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
