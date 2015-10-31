package com.zzzmode.tieba.signrank.result;

import com.zzzmode.tieba.signrank.UserInfo;
import com.zzzmode.tieba.signrank.paser.DocumentPaser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by zl on 15/1/19.
 */
public class IndexPagerResult extends SampleResult<Set<UserInfo>> implements PageResult<UserInfo>,DocumentPaser<IndexPagerResult> {


    public IndexPagerResult() {
    }

    public IndexPagerResult(int ignoreDays) {
        super(ignoreDays);
    }

    @Override
    public boolean hasNext() {
        return canNext;
    }

    @Override
    public Set<UserInfo> getParseResult() {
        return result;
    }

    @Override
    public String getNextUrl() {
        return nextUrl;
    }

    private Set<String> postsUrl =new HashSet<>();


    public Set<String> getPostUrl(){
        return postsUrl;
    }



    @Override
    public IndexPagerResult paser(Document document) {

        Set<UserInfo> list = new HashSet<>();
        Elements select = document.body().select("a.sign_highlight.j_user_card ");
        Element element = null;
        UserInfo user = null;
        //
        for (int i = 0; i < select.size(); i++) {
            try {
                element = select.get(i);
                user = new UserInfo();
                user.setHref(element.attr("href"));
                user.setName(element.text());
                String s = sRegEx_d.matcher(element.attr("title")).replaceAll("");
                user.setSignDays(Integer.parseInt(s.substring(0, s.length() - 2)));
                if(hasAdd(user)){
                    list.add(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //贴子链接
        Elements select1 = document.body().select("a.j_th_tit");
        if (select1 != null && select1.size() > 0) {
            for (int i = 0; i < select1.size(); i++) {
                element = select1.get(i);
                postsUrl.add(element.attr("href"));
            }
        }

        result=list;
        return this;

    }
}
