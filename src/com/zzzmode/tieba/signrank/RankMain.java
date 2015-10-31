package com.zzzmode.tieba.signrank;

import com.zzzmode.tieba.signrank.work.SignRankHandler;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by zl on 15/1/5.
 */
public class RankMain {

    public static void main(String[] args){
        SignRankHandler handler=new SignRankHandler("java");
        try {

            long st=System.currentTimeMillis();
            System.out.println("-------- SignRankHandler --------");

            handler.setIgnoreLowDays(500);

            Set<UserInfo> infos = handler.getPagesAllof(3);

            Set<UserInfo> res= new TreeSet(UserInfo.sortBySignDays);
            res.addAll(infos);
            print(res);

            long s = System.currentTimeMillis() - st;

            System.out.println("seconds time: " + ((s / 1000l))+"s");
        }catch (Exception e){
         e.printStackTrace();
        } finally {
            handler.shutdown();
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
