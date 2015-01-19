package com.zzzmode.tieba.signrank.work;

import com.zzzmode.tieba.signrank.UserInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Created by zl on 15/1/5.
 */
public class SpiderWork {

    public static final String UserAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";

    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(50, 100, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());

    private static final String BASE_URL = "http://tieba.baidu.com";

    private static final int MAX_NEXT_PAGE=10;  //最多翻页数

    private CopyOnWriteArraySet<String> simpleUrl = new CopyOnWriteArraySet<>();

    private CopyOnWriteArraySet<UserInfo> pUsers = new CopyOnWriteArraySet<>();



    public AtomicInteger count = new AtomicInteger();

    private String baName;

    public SpiderWork(String bName) {
        try {
            this.baName = URLEncoder.encode(bName, "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            this.baName = bName;
        }
    }


    public CopyOnWriteArraySet<String> getSimpleUrl() {
        return simpleUrl;
    }


    /**
     * 获取初步结果
     *
     * @param map
     * @param futures
     * @return
     * @throws Exception
     */
    public Set<UserInfo> getPrimaryResult(Map<String, UserInfo> map, List<Future<Set<UserInfo>>> futures) throws Exception {
        Set<UserInfo> rs1 = new HashSet<>();
        for (Future<Set<UserInfo>> future : futures) {
            if (future.isDone() && future.get() != null) {
                rs1.addAll(future.get());
            }
        }

        List<Future<Set<UserInfo>>> posts = getPost();
        for (Future<Set<UserInfo>> future : posts) {
            if (future.isDone() && future.get() != null) {
                rs1.addAll(future.get());
            }
        }
        Set<UserInfo> rs = new TreeSet<>(UserInfo.sortBySignDays);

        if(map != null){
        for (UserInfo user : rs1) {
            UserInfo u = map.get(user.getName());
            if (u != null) {
                u.copy(user);
                rs.add(u);
            }
        }
        }else {
            rs.addAll(rs1);
        }

        return rs;
    }


    public Set<UserInfo> compareDiff(Set<UserInfo> allUsers, Set<UserInfo> destUsers) {
        Set<UserInfo> otherUsers = new TreeSet<>(UserInfo.sortByTop);
        for (UserInfo all : allUsers) {
            boolean f = false;
            for (UserInfo dest : destUsers) {
                if (dest != null && dest.getName().equals(all.getName())) {
                    f = true;
                    break;
                }
            }
            if (!f) {
                otherUsers.add(all);
            }
        }


        return otherUsers;
    }


    /**
     * 抓取前几页
     *
     * @param p
     * @return
     * @throws InterruptedException
     */
    public List<Future<Set<UserInfo>>> startSpider(int p) throws InterruptedException {
        String basUrl = BASE_URL + "/f?kw=" + baName + "&ie=utf-8&pn=";
        List<SpiderPage<Set<UserInfo>>> tasks = new ArrayList<>();
        for (int i = 0; i < p; i++) {

            tasks.add(new SpiderPage<>(basUrl + (i * 50), paserPage));
        }

        return threadPoolExecutor.invokeAll(tasks);
    }

    /**
     * 获取前(p*20)的排名
     *
     * @param p 页数
     * @return
     * @throws Exception
     */
    public List<Future<Set<UserInfo>>> getLevel(int p) throws Exception {
        String baseUrl = BASE_URL + "/f/like/furank?kw=" + baName + "&pn=";
        Set<SpiderPage<Set<UserInfo>>> ranks = new HashSet<>();
        for (int i = 0; i < p; i++) {
            ranks.add(new SpiderPage<>(baseUrl + (i + 1), paserRank));

        }
        return threadPoolExecutor.invokeAll(ranks);

    }


    public Map<String, UserInfo> getSearchResult(List<UserInfo> userInfos) throws Exception {
        String baseUrl = BASE_URL + "/f/search/res?isnew=1&kw=" + baName + "&qw=";
        Set<SpiderPage<String>> ranks = new HashSet<>();
        for (UserInfo user : userInfos) {
            ranks.add(new SpiderPage<>(baseUrl+user.getName(), paserSearch));
        }
        Map<String, UserInfo> map = new HashMap<>();
        List<Future<String>> futures = threadPoolExecutor.invokeAll(ranks);

        for (int i = 0; i < futures.size(); i++) {
            Future<String> future = futures.get(i);
            if (future != null && future.get() != null && future.isDone()) {
                map.put(future.get(), userInfos.get(i));
            }
        }

        return map;
    }

    public Set<UserInfo> parseSearchResult(Map<String, UserInfo> map) {
        String baseUrl = "http://tieba.baidu.com";
        Set<SpiderPage<UserInfo>> ranks = new HashSet<>();

        return null;
    }

    /**
     * 抓取贴子详情
     *
     * @return
     * @throws InterruptedException
     */
    public List<Future<Set<UserInfo>>> getPost() throws InterruptedException {
        String baseUrl = "http://tieba.baidu.com/";
        Set<SpiderPage<Set<UserInfo>>> posts = new HashSet<>();
        if (simpleUrl != null) {
            for (String url : simpleUrl) {
                posts.add(new SpiderPage<>(baseUrl + url, paserPage));
            }
        }


        return threadPoolExecutor.invokeAll(posts, 3, TimeUnit.MINUTES);
    }


    public void shutdown() {
        threadPoolExecutor.shutdown();

    }

    private class SpiderPage<T> implements Callable<T> {

        private Object tag;
        private String url;
        private DocumentPaser<T> paser;

        public SpiderPage(String url, DocumentPaser<T> paser) {
            this.url = url;
            this.paser = paser;
            if (url == null || paser == null) {
                throw new NullPointerException("Spider url and result paser don't null !!!");
            }
        }

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

        @Override
        public T call() throws Exception {
            Document document = Jsoup.connect(url).userAgent(UserAgent).timeout(10000).get();

            return paser.paser(document);
        }
    }


    private DocumentPaser<Set<UserInfo>> parseHome = new DocumentPaser<Set<UserInfo>>() {
        @Override
        public Set<UserInfo> paser(Document document) {
            return null;
        }
    };

    private DocumentPaser<UserInfo> parseResult = new DocumentPaser<UserInfo>() {
        @Override
        public UserInfo paser(Document document) {

            return null;
        }
    };

    private DocumentPaser<Set<UserInfo>> paserPage = new DocumentPaser<Set<UserInfo>>() {


       // private AtomicInteger indexPage=new AtomicInteger();
        //private String nextPage = null;
        private Pattern pattern = Pattern.compile("[^\\d]");
        @Override
        public Set<UserInfo> paser(Document document) {
             CopyOnWriteArraySet<UserInfo> list = new CopyOnWriteArraySet<>();
            Elements select = document.body().select("a.sign_highlight");
            Element element = null;
            UserInfo user = null;

            for (int i = 0; i < select.size(); i++) {
                try {
                    element = select.get(i);
                    user = new UserInfo();
                    user.setHref(element.attr("href"));
                    user.setName(element.text());
                    String s = pattern.matcher(element.attr("title")).replaceAll("");
                    user.setSignDays(Integer.parseInt(s.substring(0, s.length() - 2)));
                    pUsers.add(user);
                    list.add(user);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Elements select1 = document.body().select("a.j_th_tit");
            if (select1 != null && select1.size() > 0) {
                for (int i = 0; i < select1.size(); i++) {
                    element = select1.get(i);
                    simpleUrl.add(element.attr("href"));
                }
            }

            /*

            select = document.select("li.l_pager.pager_theme_4.pb_list_pager");
            select = select.select("a");

            if (select != null && select.size() > 0) {
                for (int i = 0; i < select.size(); i++) {
                    if ("下一页".equals(select.get(i).text())) {
                        //System.out.println(select.get(i));
                        nextPage = select.get(i).attr("href");
                        break;
                    }
                }
            }

            try {
                if (nextPage != null && indexPage.incrementAndGet() < MAX_NEXT_PAGE) {
                    do {
                        System.out.println(nextPage);
                        Future<Set<UserInfo>> submit = threadPoolExecutor.submit(new SpiderPage<Set<UserInfo>>(BASE_URL + nextPage, this));
                        nextPage=null;
                        Set<UserInfo> userInfos = submit.get(1, TimeUnit.MINUTES);
                        if (userInfos != null) {
                            list.addAll(userInfos);
                            System.out.println("-----------  下一页  ----  "+list.size());
                        }else {
                            System.out.println(submit);
                        }
                    }while (nextPage != null && indexPage.incrementAndGet() < MAX_NEXT_PAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
*/
            return list;
        }
    };


    private DocumentPaser<Set<UserInfo>> paserRank = new DocumentPaser<Set<UserInfo>>() {
        @Override
        public Set<UserInfo> paser(Document document) {
            Elements select = document.select("table.drl_list").select("tr.drl_list_item");
            Set<UserInfo> data = new HashSet<>();
            Pattern pattern = Pattern.compile("[^\\d]");
            for (int i = 0; i < select.size(); i++) {
                Element element = select.get(i);
                UserInfo info = new UserInfo();
                info.setTop(Integer.parseInt(element.select("td.drl_item_index").text()));
                info.setName(element.select("td.drl_item_name").text());
                info.setExperience(Integer.parseInt(element.select("td.drl_item_exp").text()));
                String lv = element.select("td.drl_item_title").first().children().first().className();
                info.setLevel(Integer.parseInt(pattern.matcher(lv).replaceAll("")));
                data.add(info);
            }
            return data;
        }
    };


    private DocumentPaser<String> paserSearch = new DocumentPaser<String>() {
        @Override
        public String paser(Document document) {
            Elements select = document.select("a.bluelink");
            System.out.println();
            for (int i = 0; i < select.size(); i++) {
                String s = select.get(i).attr("href");
                if (s != null && s.trim().matches("^/p/\\d(.*)\\d$")) {
                    return s;
                }
            }
            return null;
        }
    };


    public static interface DocumentPaser<T> {
        T paser(Document document);
    }
}
