package com.dongzj.es.common.es;

import java.util.List;

/**
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2018/11/8
 * Time: 16:58
 */
public class EsSearchResult<T> {

    Integer total = null;
    List<T> hits = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getHits() {
        return hits;
    }

    public void setHits(List<T> hits) {
        this.hits = hits;
    }
}
