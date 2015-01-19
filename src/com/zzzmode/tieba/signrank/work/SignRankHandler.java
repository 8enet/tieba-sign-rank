package com.zzzmode.tieba.signrank.work;

import com.zzzmode.tieba.signrank.UserInfo;
import com.zzzmode.tieba.signrank.result.IndexPagerResult;
import com.zzzmode.tieba.signrank.result.PageResult;
import com.zzzmode.tieba.signrank.result.RankPagerResult;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by zl on 15/1/19.
 */
public class SignRankHandler {
    private static  ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(80, 100, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    private String baName;

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
     * @param indexPage
     */
    public Set<RankPagerResult> getRankTop(int indexPage) throws Exception {
        String baseUrl = Configs.HTTP.BASE_URL + "/f/like/furank?kw=" + baName + "&pn=";
        Set<PageSpider<RankPagerResult>> ranks = new HashSet<>();
        if (indexPage <=0)
            indexPage=1;
        for (int i = 0; i < indexPage; i++) {
            ranks.add(new PageSpider(baseUrl + (i + 1), new RankPagerResult()));
        }
        List<Future<RankPagerResult>> futures = threadPoolExecutor.invokeAll(ranks);
        return getResult(futures);

    }


    private static <T> Set<T> getResult(List<Future<T>> list) throws ExecutionException, InterruptedException {
        if(list != null){
            Set<T> data=new HashSet<>();
            for (Future<T> future:list){
                if(future != null && future.isDone()){
                    data.add(future.get());
                }
            }
            return data;
        }
        return null;
    }



    /**
     * 获取前indexPage 页
     * @param indexPage
     * @return
     */
    public Set<IndexPagerResult> getIndexPageResult(int indexPage) throws Exception {
        String basUrl = Configs.HTTP.BASE_URL + "/f?kw=" + baName + "&ie=utf-8&pn=";
        List<PageSpider<IndexPagerResult>> tasks = new ArrayList<>();
        for (int i = 0; i < indexPage; i++) {

            tasks.add(new PageSpider(basUrl + (i * 50), new IndexPagerResult()));
        }

        List<Future<IndexPagerResult>> futures = threadPoolExecutor.invokeAll(tasks);

        return getResult(futures);
    }

}
