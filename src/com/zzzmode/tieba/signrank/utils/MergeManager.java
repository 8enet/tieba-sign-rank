package com.zzzmode.tieba.signrank.utils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by zl on 15/1/22.
 */
public class MergeManager<T> {

    private Set<T> clos;
    private Comparator comparator;


    public MergeManager(Comparator comparator){
        this.comparator=comparator;
        clos=new HashSet<>();
    }

    public MergeManager(){
        this(null);
    }

    public void addPart(Set<T> t){
        clos.addAll(t);
    }

    public Set<T> merges(Comparator comparator){
        if(comparator == null && this.comparator == null){
            return copy(new HashSet<T>());
        }
        if(comparator != null){
            return copy(new TreeSet(comparator));
        }

        if(this.comparator != null){
            return copy(new TreeSet(this.comparator));
        }

        return null;
    }

    public Set<T> merges(){
        return merges(null);
    }

    private Set<T> copy(Set<T> res){
        if(clos != null){
            for (T t:clos){
                res.add(t);
            }
        }
        return res;
    }


    public void reset(){
        try {
            if(clos != null){
                clos.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
