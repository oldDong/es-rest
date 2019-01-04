package com.dongzj.es.dao;

import com.dongzj.es.common.es.EsRestClient;
import com.dongzj.es.common.es.EsSearchResult;
import com.dongzj.es.common.util.SQLUtils;
import com.dongzj.es.po.EsKpUpdatePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2018/11/6
 * Time: 14:55
 */
@Repository
public class OprKpUpdateDao {

    private static final Logger logger = LoggerFactory.getLogger(OprKpUpdateDao.class);

    @Resource
    private EsRestClient esRestClient;

    public EsSearchResult<EsKpUpdatePo> list(Long id, String key, Integer pageSize, Integer pageIndex) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("orgId", id);
        map.put("depttitles", key);
        map.put("offset", (pageIndex - 1) * pageSize);
        map.put("limit", pageSize);
        String sql = SQLUtils.getSql("EsCrmOprKpUpdate.list", map);
        logger.info("EsCrmOprKpUpdate.list.sql: " + sql);
        return esRestClient.search(sql, EsKpUpdatePo.class);
    }

    public EsSearchResult<EsKpUpdatePo> get(Long orgid, Long uid) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("orgId", orgid);
        map.put("uid", uid);
        String sql = SQLUtils.getSql("EsCrmOprKpUpdate.get", map);
        logger.info("EsCrmOprKpUpdate.get.sql: " + sql);
        return esRestClient.search(sql, EsKpUpdatePo.class);
    }

    public void update(Long orgid, Long uid, String tags) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("orgId", orgid);
        map.put("uid", uid);
        map.put("tags", tags);
        String sql = SQLUtils.getSql("EsCrmOprKpUpdate.updateTags", map);
        logger.info("EsCrmOprKpUpdate.updateTags.sql: " + sql);
        esRestClient.perform(sql);
    }

    /**
     * 仅针对存在tags的记录
     *
     * @param orgid
     * @param uid
     * @param tag
     * @throws Exception
     */
    public void addTag(Long orgid, Long uid, String tag) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("orgId", orgid);
        map.put("uid", uid);
        map.put("tag", "、" + tag);
        String sql = SQLUtils.getSql("EsCrmOprKpUpdate.addTags", map);
        logger.info("EsCrmOprKpUpdate.addTags.sql: " + sql);
        esRestClient.perform(sql);
    }

    public EsSearchResult<EsKpUpdatePo> rangeAccount(Integer small, Integer big) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("small", small);
        map.put("big", big);
        String sql = SQLUtils.getSql("EsCrmOprKpUpdate.range", map);
        logger.info("EsCrmOprKpUpdate.range.sql: " + sql);
        return esRestClient.search(sql, EsKpUpdatePo.class);
    }

    public EsSearchResult<EsKpUpdatePo> filter(Integer small, Integer big, String city, String orgName, Integer pageIndex, Integer pageSize) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("small", small);
        map.put("big", big);
        map.put("city", city);
        map.put("orgName", orgName);
        map.put("offset", (pageIndex - 1) * pageSize);
        map.put("limit", pageSize);
        String sql = SQLUtils.getSql("EsCrmOprKpUpdate.filter", map);
        logger.info("EsCrmOprKpUpdate.filter.sql: " + sql);
        return esRestClient.search(sql, EsKpUpdatePo.class);
    }

}
