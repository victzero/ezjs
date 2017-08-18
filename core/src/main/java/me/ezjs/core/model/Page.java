package me.ezjs.core.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zero-mac on 16/7/10.
 */
public class Page implements Serializable {

    private final List listInfo;
    private final FlipInfo flipInfo;

    public Page(FlipFilter filter, long totalCount, List list) {
        this.flipInfo = new FlipInfo(filter, totalCount);
        this.listInfo = list;
    }

    public List getListInfo() {
        return listInfo;
    }

    public FlipInfo getFlipInfo() {
        return flipInfo;
    }

    @Override
    public String toString() {
        return "Page{" +
                "listInfo=" + listInfo +
                ", flipInfo=" + flipInfo +
                '}';
    }
}
