package com.zzzmode.tieba.signrank.work;

import com.zzzmode.tieba.signrank.UserInfo;
import com.zzzmode.tieba.signrank.result.IndexPagerResult;
import com.zzzmode.tieba.signrank.result.PageResult;
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
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(150, 200, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    private String baName;
    private int spiderDepth=0;
    private AtomicInteger count=new AtomicInteger();

    public SignRankHandler(String bName) {
        try {
            this.baName = URLEncoder.encode(bName, "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            this.baName = bName;
        }
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
            ranks.add(new PageSpider(baseUrl + (i + 1), new RankPagerResult()));
            count.incrementAndGet();
        }

        final Set<RankPagerResult> result = getResult(threadPoolExecutor.invokeAll(ranks));
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
            tasks.add(new PageSpider(basUrl + (i * 50), new IndexPagerResult()));
            count.incrementAndGet();
        }
        return getResult(threadPoolExecutor.invokeAll(tasks));
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

        CountDownLatch latch=new CountDownLatch(10);
        latch.countDown();

        List<PageSpider<PostPagerResult>> tasks = new ArrayList<>();
        for (String url : posts) {
            tasks.add(new PageSpider(Configs.HTTP.BASE_URL + url, new PostPagerResult()));
            count.incrementAndGet();
        }
        Set<PostPagerResult> result = getResult(threadPoolExecutor.invokeAll(tasks));
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
        System.out.println(" Recursion Spider ... ");
        spiderDepth++;
        getPostPageResult(posts, retUsers);
    }



    public Set<UserInfo> getPostPageResult(Set<String> posts) throws Exception {
        Set<UserInfo> result=new TreeSet<>(UserInfo.sortBySignDays);
        spiderDepth=0;
        getPostPageResult(posts,result);
        return result;
    }


    public int getSpiderCount(){
        return count.get();
    }


    public void shutdown(){
        try {
            threadPoolExecutor.shutdownNow();
        }catch (Exception e){

        }

    }
}
