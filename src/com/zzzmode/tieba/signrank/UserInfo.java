package com.zzzmode.tieba.signrank;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by zl on 15/1/5.
 */
public class UserInfo implements Serializable{


    private String name;
    private String href;
    private int signDays;
    private int top;
    private int level;
    private int experience;
    private boolean disSign=false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getSignDays() {
        return signDays;
    }

    public void setSignDays(int signDays) {
        this.signDays = signDays;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public boolean isDisSign() {
        return disSign;
    }

    public void setDisSign(boolean disSign) {
        this.disSign = disSign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;


        if (name != null ? !name.equals(userInfo.name) : userInfo.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();

        return result;
    }

    public  void copy(UserInfo info){
        if(info != null && info.getName() != null && info.getName().equals(getName())){

            if(info.getTop() != 0){
                setTop(info.getTop());
            }
            if(info.getExperience() != 0){
                setExperience(info.getExperience());
            }
            if(info.getLevel() != 0){
                setLevel(info.getLevel());
            }
            if(info.getSignDays() != 0){
                setSignDays(info.getSignDays());
            }

        }
    }


    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("UserInfo{name='").append(name).append("\', signDays=").append(signDays);

        sb.append(", top=").append(top);
        sb.append(", level=").append(level);
        sb.append(", experience=").append(experience);
        sb.append("}");

        return sb.toString();
    }




    public transient static final Comparator<UserInfo> sortBySignDays =new Comparator<UserInfo>() {

        @Override
        public int compare(UserInfo o1, UserInfo o2) {
            if(o1 == null || o2==null)
                return 0;
            if(o1.getSignDays()>o2.getSignDays())
                return -1;
            if(o1.getSignDays()<o2.getSignDays())
                return 1;


            return o1.getName().compareTo(o2.getName());
        }
    };


    public transient static final Comparator<UserInfo> sortByExperience=new Comparator<UserInfo>() {

        @Override
        public int compare(UserInfo o1, UserInfo o2) {
            if(o1 == null || o2==null)
                return 0;

            if(o1.getExperience() > o2.getExperience())
                return -1;
            if(o1.getExperience() < o2.getExperience())
                return 1;

            return o1.getName().compareTo(o2.getName());
        }
    };


    public transient static final Comparator<UserInfo> sortByTop =new Comparator<UserInfo>() {
        @Override
        public int compare(UserInfo o1, UserInfo o2) {
            if(o1 == null || o2==null)
                return 0;
            if(o1.getTop() < o2.getTop())
                return -1;
            if(o1.getTop() > o2.getTop())
                return 1;
            return o1.getName().compareTo(o2.getName());
        }
    };





}
