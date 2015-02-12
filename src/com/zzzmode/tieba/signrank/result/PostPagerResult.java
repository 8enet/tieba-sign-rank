package com.zzzmode.tieba.signrank.result;

import com.zzzmode.tieba.signrank.UserInfo;
import com.zzzmode.tieba.signrank.paser.DocumentPaser;
import com.zzzmode.tieba.signrank.work.Configs;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by zl on 15/1/19.
 */
public class PostPagerResult extends SampleResult<Set<UserInfo>> implements PageResult<UserInfo>, DocumentPaser<PostPagerResult> {
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


    @Override
    public PostPagerResult paser(Document document) {

        Set<UserInfo> list = new HashSet<>();
        Elements select = document.body().select("a.p_author_name.sign_highlight.j_user_card");
        Element element = null;
        UserInfo user = null;
        Pattern pattern = Pattern.compile("[^\\d]");
        for (int i = 0; i < select.size(); i++) {
            try {
                element = select.get(i);
                user = new UserInfo();
                user.setHref(element.attr("href"));
                user.setName(element.text());
                String s = pattern.matcher(element.attr("title")).replaceAll("");
                user.setSignDays(Integer.parseInt(s.substring(0, s.length() - 2)));
                list.add(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        select = document.select("li.l_pager.pager_theme_4.pb_list_pager");
        select = select.select("a");
        if (select != null && select.size() > 0) {
            for (int i = 0; i < select.size(); i++) {
                if ("下一页".equals(select.get(i).text())) {
                    nextUrl = select.get(i).attr("href");
                    if(nextUrl != null){
                        try {
                            int c=nextUrl.lastIndexOf('=');
                            String s=nextUrl.substring(c+1).trim();
                            int idx=Integer.parseInt(s);

                            if(idx > Configs.PAGE.MAX_NEXT_PAGE){
                                nextUrl=null;
                                canNext=false;
                            }else {
                                canNext = true;
                            }
                        }catch (Exception e){
                            canNext=false;
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        }
        result=list;
        return this;
    }
}
