package com.dongzj.es.common.es;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.sniff.SniffOnFailureListener;
import org.elasticsearch.client.sniff.Sniffer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2018/11/8
 * Time: 16:54
 */
@Component
public class EsRestClient {

    private static RestClient restClient = null;

    private static Sniffer sniffer = null;

    @Value("${es.ips}")
    private String esIps;

    @PostConstruct
    public void init() {
        if (null == restClient) {
            SniffOnFailureListener sniffOnFailureListener = new SniffOnFailureListener();
            restClient = RestClient.builder(new HttpHost(esIps.split(",")[0], 9200))
                    .setFailureListener(sniffOnFailureListener)
                    .build();
            sniffer = Sniffer.builder(restClient)
                    .setSniffAfterFailureDelayMillis(30000)
                    .build();
            sniffOnFailureListener.setSniffer(sniffer);
        }
    }

    public <T> EsSearchResult<T> search(String query, Class<T> valueType) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        EsSearchResult<T> esSearchResult = new EsSearchResult<>();
        List<T> l = new ArrayList<>();
        Query q = parse(query);
        Response response = restClient.performRequest(q.getMethod(), q.getEndpoint(), q.getParams(), q.getEntity());
        String responseBody = EntityUtils.toString(response.getEntity());
        Map responseBodyMap = objectMapper.readValue(responseBody, Map.class);
        Map hitsMap = (Map) responseBodyMap.get("hits");
        Integer total = (Integer) hitsMap.get("total");
        JavaType beanType = objectMapper.getTypeFactory().constructParametricType(List.class, valueType);

        List<Map> var1 = (List) hitsMap.get("hits");
        List var2 = new ArrayList();
        for (Map map : var1) {
            var2.add(map.get("_source"));
        }
        l = objectMapper.readValue(objectMapper.writeValueAsString(var2), beanType);
        esSearchResult.setTotal(total);
        esSearchResult.setHits(l);
        return esSearchResult;
    }

    public <T> EsSearchResult<T> searchAll(String query, Class<T> valueType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<T> l = new ArrayList<>();
        List var2 = new ArrayList();
        EsSearchResult<T> esSearchResult = new EsSearchResult<>();
        Query q = parse(query);
        Response response = null;
        String responseBody = null;
        Map responseBodyMap = null;
        Map hitsMap = null;
        response = restClient.performRequest(q.getMethod(), q.getEndpoint(), q.getParams(), q.getEntity());
        responseBody = EntityUtils.toString(response.getEntity());
        responseBodyMap = objectMapper.readValue(responseBody, Map.class);
        hitsMap = (Map) responseBodyMap.get("hits");
        Integer total = (Integer) hitsMap.get("total");
        List<Integer> pages = new ArrayList<>();
        if (total > 0) {
            total = total >= 200 ? 200 : total;
            for (int i = 0; i < total; i += 10000)
                pages.add(i);
        }
        HttpEntity entity = null;
        List<Map> var1 = null;
        JavaType beanType = objectMapper.getTypeFactory().constructParametricType(List.class, valueType);
        String searchBodyJson = q.getJson();
        Map searchBodyMap = objectMapper.readValue(searchBodyJson, Map.class);
        searchBodyMap.put("size", 200);
        for (Integer i : pages) {
            searchBodyMap.put("from", i);
            entity = new NStringEntity(objectMapper.writeValueAsString(searchBodyMap), ContentType.APPLICATION_JSON);
            response = restClient.performRequest(q.getMethod(), q.getEndpoint(), q.getParams(), entity);
            responseBody = EntityUtils.toString(response.getEntity());
            responseBodyMap = objectMapper.readValue(responseBody, Map.class);
            hitsMap = (Map) responseBodyMap.get("hits");
            var1 = (List) hitsMap.get("hits");
            for (Map map : var1) {
                var2.add(map.get("_source"));
            }
        }
        l = objectMapper.readValue(objectMapper.writeValueAsString(var2), beanType);
        esSearchResult.setTotal(total);
        esSearchResult.setHits(l);
        return esSearchResult;
    }

    public void perform(String query) throws Exception {
        Query q = parse(query);
        restClient.performRequest(q.getMethod(), q.getEndpoint(), q.getParams(), q.getEntity());
    }

    public void close() throws IOException {
        sniffer.close();
        restClient.close();
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    public String getCurlCommand(String query) {
        Query q = parse(query);
        String command = "curl -X%s 'http://%s:9200/%s?pretty' -d '%s'";
        command = String.format(command, q.getMethod(), esIps.split(",")[0], q.getEndpoint(), q.getJson());
        return command;
    }

    private Query parse(String str) {
        Query query = new Query();
        int s = str.indexOf('{');
        String remains = null;
        if (s > 0) {
            String jsonString = str.substring(s).trim();
            HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
            query.setJson(jsonString);
            query.setEntity(entity);
            remains = str.substring(0, s).trim();
        } else {
            remains = str.trim();
        }
        String[] split = remains.split("\\s+");
        query.setMethod(split[0].toUpperCase());
        StringBuilder endPoint = new StringBuilder();
        for (int i = 1; i < split.length; i++) {
            endPoint.append(split[i]);
        }
        query.setEndpoint(endPoint.toString());
        return query;
    }

    class Query {
        private String method;
        private String endpoint;
        private Map<String, String> params;
        private HttpEntity entity;
        private String json;

        {
            params = Collections.emptyMap();
            json = "{}";
            entity = new NStringEntity("{}", ContentType.APPLICATION_JSON);
        }

        Query() {

        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public HttpEntity getEntity() {
            return entity;
        }

        public void setEntity(HttpEntity entity) {
            this.entity = entity;
        }

        public Map<String, String> getParams() {
            return params;
        }

        public String getJson() {
            return json;
        }

        public void setJson(String json) {
            this.json = json;
        }
    }
}
