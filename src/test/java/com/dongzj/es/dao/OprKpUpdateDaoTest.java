package com.dongzj.es.dao;

import com.dongzj.es.EsApplicationTests;
import com.dongzj.es.common.es.EsSearchResult;
import com.dongzj.es.po.EsKpUpdatePo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2018/11/8
 * Time: 17:18
 */
public class OprKpUpdateDaoTest extends EsApplicationTests {

    @Autowired
    private OprKpUpdateDao kpUpdateDao;

    @Test
    public void testGet() {
        Long orgid = 5717608611L;
        Long uid = 12133200L;
        try {
            EsSearchResult<EsKpUpdatePo> result = kpUpdateDao.get(orgid, uid);
            System.out.println("total: " + result.getTotal());
            List<EsKpUpdatePo> list = result.getHits();
            list.stream().forEach(esKpUpdatePo -> System.out.println(esKpUpdatePo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAdd() {
        Long orgid = 5717608611L;
        Long uid = 12133200L;
        String tag = "高管";
        try {
            kpUpdateDao.addTag(orgid, uid, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRangeAccount() {
        Integer small = new Integer(2300);
        Integer big = new Integer(2400);
        try {
            EsSearchResult<EsKpUpdatePo> result = kpUpdateDao.rangeAccount(small, big);
            System.out.println("total: " + result.getTotal());
            List<EsKpUpdatePo> list = result.getHits();
            list.stream().forEach(esKpUpdatePo -> System.out.println(esKpUpdatePo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFilter() {
        Integer small = new Integer(1);
        Integer big = new Integer(1000);
        Integer pageIndex = 1;
        Integer pageSize = 200;
        String city = "杭州";
        String orgName = "青山湖";
        try {
            EsSearchResult<EsKpUpdatePo> result = kpUpdateDao.filter(small, big, city, orgName, pageIndex, pageSize);
            System.out.println("total: " + result.getTotal());
            List<EsKpUpdatePo> list = result.getHits();
            list.stream().forEach(esKpUpdatePo -> System.out.println(esKpUpdatePo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
