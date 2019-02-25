package com.yesmywine.goods.service.Impl;

import com.yesmywine.util.basic.Dictionary;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.springframework.stereotype.Service;

/**
 * Created by hz on 2017/6/14.
 */
@Service
public class SetSearchService {

    public HttpSolrServer getHttpSolrServer(){
        String url = Dictionary.SOLR_SERVER;
//        String url = "http://api.hzbuvi.com:8791/solr/yesmywine";
        HttpSolrServer httpSolrServer = new HttpSolrServer(url);
        httpSolrServer.setParser(new XMLResponseParser()); // 设置响应解析器
        httpSolrServer.setMaxRetries(1); // 设置重试次数，推荐设置为1
        httpSolrServer.setConnectionTimeout(500); // 建立连接的最长时间
        return httpSolrServer;
    }
}
