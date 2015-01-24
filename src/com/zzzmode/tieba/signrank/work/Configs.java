package com.zzzmode.tieba.signrank.work;


/**
 * Created by zl on 15/1/19.
 */
public final class Configs {

    public static final class HTTP{
        public static final String USER_AGENT="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";

        public static final int CONNECT_TIMEOUT=60*1000;

        public static final String BASE_URL = "http://tieba.baidu.com";
    }

    public static final class PAGE{
        public static final int MAX_NEXT_PAGE=10;  //最多翻页数
    }

}
