package com.zzzmode.tieba.signrank.work;

import com.zzzmode.tieba.signrank.UserInfo;
import com.zzzmode.tieba.signrank.result.RankPagerResult;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zl on 15/1/19.
 */
public class RankHandler {
    private static  ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(80, 100, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());


    private String baName;

    private RankPagerResult rankPagerResult=new RankPagerResult();




    public RankHandler(String bName) {
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
    public void getRankTop(int indexPage) throws ExecutionException, InterruptedException {
        String baseUrl = Configs.HTTP.BASE_URL + "/f/like/furank?kw=" + baName + "&pn=";

        Set<PageSpider<Set<UserInfo>>> ranks = new HashSet<>();
        if (indexPage <=0)
            indexPage=1;
        for (int i = 0; i < indexPage; i++) {
            ranks.add(new PageSpider<>(baseUrl + (i + 1), rankPagerResult));

        }

        threadPoolExecutor.invokeAny(ranks);

    }

}
