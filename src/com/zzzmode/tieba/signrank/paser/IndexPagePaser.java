package com.zzzmode.tieba.signrank.paser;

import com.zzzmode.tieba.signrank.UserInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Pattern;

/**
 * Created by zl on 15/1/19.
 */
public class IndexPagePaser implements DocumentPaser<Set<UserInfo>> {


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
                //pUsers.add(user);
                list.add(user);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Elements select1 = document.body().select("a.j_th_tit");
        if (select1 != null && select1.size() > 0) {
            for (int i = 0; i < select1.size(); i++) {
                element = select1.get(i);
                //simpleUrl.add(element.attr("href"));
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
}
