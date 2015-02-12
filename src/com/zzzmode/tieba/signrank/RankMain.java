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
            Set<IndexPagerResult> indexPageResult = handler.getIndexPageResult(3);
            Set<String> urls=new HashSet<>();
            for (IndexPagerResult result:indexPageResult){
                //System.out.println (result.getPostUrl().size());
                urls.addAll(result.getPostUrl());
            }

            System.out.println(" all urls : "+urls.size());
            System.out.println("----------------");


            Set<PostPagerResult> postPageResult = handler.getPostPageResult(urls);

            MergeManager<UserInfo> mergeManager=new MergeManager<>(UserInfo.sortBySignDays);

            for (PostPagerResult result:postPageResult){
                //System.out.println(result.hasNext() +"     "+result.getNextUrl());
                //print(result.getParseResult());
                mergeManager.addPart(result.getParseResult());
            }

            print(mergeManager.merges());


            if(true){
                double s=System.currentTimeMillis()-st;
                System.out.println("seconds time: "+( (s/1000d)));
                return;
            }



            /*
            Document document= Jsoup.connect("http://tieba.baidu.com/p/3533945191").userAgent(SpiderWork.UserAgent).get();

//            Elements select = document.select("div.threadlist_author");
//
//            System.out.println(select.size());
//
//            for(int i=0;i<select.size();i++){
//                String texts = select.get(i).text();
//                String[] s=texts.split(" ");
//                System.out.println(s[0]);
//                System.out.println(select.get(i).attr("href")+"\t"+select.get(i).attr("href").trim().matches("^/p/\\d(.*)\\d$"));
//            }

            Elements select = document.select("li.l_pager.pager_theme_4.pb_list_pager");
            select=select.select("a");

            for(int i=0;i<select.size();i++){
                if("下一页".equals(select.get(i).text())){
                    System.out.println(select.get(i));
                    break;
                }
            }
*/

//            Elements select = document.select("a.bluelink");
//            System.out.println();
//            for(int i=0;i<select.size();i++){
//                String s=select.get(i).attr("href");
//                if(s != null && s.trim().matches("^/p/\\d(.*)\\d$")){
//                    System.out.println(s);
//                    break;
//                }
//            }



            long s=System.currentTimeMillis();
            Set<UserInfo> rs1=new HashSet<>();
            SpiderWork spiderWork=new SpiderWork("java");

            //先启动抓取前n页和等级排行榜的任务

            List<Future<Set<UserInfo>>> futures = spiderWork.startSpider(10);
            //List<Future<Set<UserInfo>>> level1 = spiderWork.getLevel(5);



//            //获取等级排名
//            for(Future<Set<UserInfo>> future:level1){
//                if(future.isDone() && future.get() != null){
//                    rs1.addAll(future.get());
//                }
//            }
//
//            //初步根据
//            Map<String,UserInfo> map=new HashMap<>();
//            for(UserInfo u:rs1){
//                map.put(u.getName(), u);
//            }
//
//            System.out.println("---  等级排行  ---");
            //print(rs1);

            Set<UserInfo> rs=new TreeSet<>(UserInfo.sortBySignDays);
            rs.addAll(spiderWork.getPrimaryResult(null, futures));


            System.out.println("---  初步签到天数排行  ---");
            print(rs);


            Map<String, UserInfo> searchResult = spiderWork.getSearchResult(new ArrayList<UserInfo>(spiderWork.compareDiff(rs1, rs)));

            //print(searchResult);



           // oos.writeObject(rs);
            //oos.flush();



            spiderWork.shutdown();
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
