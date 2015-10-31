package com.zzzmode.tieba.signrank.work;

import com.zzzmode.tieba.signrank.UserInfo;
import com.zzzmode.tieba.signrank.result.IndexPagerResult;
import com.zzzmode.tieba.signrank.result.PostPagerResult;
import com.zzzmode.tieba.signrank.result.RankPagerResult;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zl on 15/1/19.
 */
public class SignRankHandler {
    private static ThreadPoolExecutor sThreadPoolExecutor = new ThreadPoolExecutor(150, 200, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    private String baName;
    private int spiderDepth=0;
    private AtomicInteger count=new AtomicInteger();

    private int mIgnoreLowDays=100; //签到低于此值的id 忽略掉，

    public SignRankHandler(String bName) {
        try {
            this.baName = URLEncoder.encode(bName, "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            this.baName = bName;
        }
    }

    public void setIgnoreLowDays(int ignoreLowDays) {
        this.mIgnoreLowDays = ignoreLowDays;
    }

    /**
     * 获取等级排名
     *
     * @param indexPage
     */
    public Set<UserInfo> getRankTop(int indexPage) throws Exception {
        String baseUrl = Configs.HTTP.BASE_URL + "/f/like/furank?kw=" + baName + "&pn=";
        Set<PageSpider<RankPagerResult>> ranks = new HashSet<>();
        if (indexPage <= 0)
            indexPage = 1;
        for (int i = 0; i < indexPage; i++) {
            ranks.add(new PageSpider(baseUrl + (i + 1), new RankPagerResult(mIgnoreLowDays)));
            count.incrementAndGet();
        }

        final Set<RankPagerResult> result = getResult(sThreadPoolExecutor.invokeAll(ranks));
        Set<UserInfo> retUsers=new TreeSet<>(UserInfo.sortByTop);
        for (RankPagerResult rpr:result){
            if(rpr != null){
                retUsers.addAll(rpr.getParseResult());
            }
        }
        return retUsers;
    }


    private static <T> Set<T> getResult(List<Future<T>> list) {
        if (list != null) {
            Set<T> data = new HashSet<>();
            for (Future<T> future : list) {
                try {
                    if (future != null && future.isDone()) {
                        data.add(future.get(2, TimeUnit.MINUTES));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return data;
        }
        return null;
    }


    /**
     * 获取前indexPage 页
     *
     * @param indexPage
     * @return
     */
    public Set<IndexPagerResult> getIndexPageResult(int indexPage) throws Exception {
        String basUrl = Configs.HTTP.BASE_URL + "/f?kw=" + baName + "&ie=utf-8&pn=";
        List<PageSpider<IndexPagerResult>> tasks = new ArrayList<>();
        for (int i = 0; i < indexPage; i++) {
            tasks.add(new PageSpider(basUrl + (i * 50), new IndexPagerResult(mIgnoreLowDays)));
            count.incrementAndGet();
        }
        return getResult(sThreadPoolExecutor.invokeAll(tasks));
    }




    /**
     * 获取贴子详情
     *
     * @param posts
     * @return
     * @throws Exception
     */
    private void getPostPageResult(Set<String> posts, Set<UserInfo> retUsers) throws Exception {
        if (posts == null || retUsers == null)
            return;


        List<PageSpider<PostPagerResult>> tasks = new ArrayList<>();
        for (String url : posts) {
            tasks.add(new PageSpider(Configs.HTTP.BASE_URL + url, new PostPagerResult(mIgnoreLowDays)));
            count.incrementAndGet();
        }
        Set<PostPagerResult> result = getResult(sThreadPoolExecutor.invokeAll(tasks));
        posts.clear();

        for (PostPagerResult pos : result) {
            if (pos != null) {
                retUsers.addAll(pos.getParseResult());
                if (pos.hasNext() && pos.getNextUrl() != null) {
                    posts.add(pos.getNextUrl());
                }
            }
        }

        if (posts.isEmpty() || spiderDepth > Configs.PAGE.MAX_NEXT_PAGE) {
            return;
        }
        System.out.println(" Recursion Spider ... "+spiderDepth);
        spiderDepth++;
        getPostPageResult(posts, retUsers);
    }


    /**
     * 获取贴子详情
     * @param posts
     * @return
     * @throws Exception
     */
    public Set<UserInfo> getPostPageResult(Set<String> posts) throws Exception {
        if(posts == null)
            return null;
        Set<UserInfo> retUsers=new HashSet<>();
        getPostPageResult(posts,retUsers);
        return retUsers;
    }


    public int getSpiderCount(){
        return count.get();
    }


    public Set<UserInfo> getPagesAllof(int size) throws Exception{
        Set<IndexPagerResult> indexPageResult =getIndexPageResult(size);

        Set<String> urls=new HashSet<>();
        for (IndexPagerResult result:indexPageResult){
            urls.addAll(result.getPostUrl());
        }

        return getPostPageResult(urls);
    }

    public void shutdown(){
        try {
            sThreadPoolExecutor.shutdownNow();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
