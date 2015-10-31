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
public class RankPagerResult extends SampleResult<Set<UserInfo>> implements PageResult<UserInfo>,DocumentPaser<RankPagerResult> {


    public RankPagerResult() {
    }

    public RankPagerResult(int ignoreDays) {
        super(ignoreDays);
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Set<UserInfo> getParseResult() {
        return result;
    }

    @Override
    public String getNextUrl() {
        return null;
    }

    @Override
    public RankPagerResult paser(Document document) {
        Elements select = document.select("table.drl_list").select("tr.drl_list_item");
        Set<UserInfo> data = new HashSet<>();
        for (int i = 0; i < select.size(); i++) {
            try {
                Element element = select.get(i);
                UserInfo info = new UserInfo();
                info.setTop(Integer.parseInt(element.select("td.drl_item_index").text()));
                info.setName(element.select("td.drl_item_name").text());
                info.setExperience(Integer.parseInt(element.select("td.drl_item_exp").text()));
                String lv = element.select("td.drl_item_title").first().children().first().className();
                info.setLevel(Integer.parseInt(sRegEx_d.matcher(lv).replaceAll("")));
                if(hasAdd(info)){
                    data.add(info);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        result=data;
        return this;
    }
}
