package cn.edu.sjtu;

/**
 * Created by spring on 2017/4/5.
 */

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;


import java.util.ArrayList;
import java.util.List;

public class SolrUtil {
    static final String zkHost = "192.168.1.201:2181,192.168.1.202:2181,192.168.1.203:2181/solr";
    static final Logger log = Logger.getLogger(SolrUtil.class);
    private static CloudSolrClient cloudSolrClient;

    public static synchronized CloudSolrClient getCloudSolrClient(final String zkHost) {
        if (cloudSolrClient == null) {
            try {
                cloudSolrClient = new CloudSolrClient(zkHost);
            } catch(Exception e) {
                System.out.println("");
                e.printStackTrace();
            }
        }
        return cloudSolrClient;
    }

    public static List<SolrDocument> search(String queryString) {
        CloudSolrClient cloudSolrClient = getCloudSolrClient(zkHost);
        cloudSolrClient.setDefaultCollection("jzfile");
        SolrQuery query = new SolrQuery();
        query.setQuery(queryString);
        List<SolrDocument> ds = new ArrayList<SolrDocument>();
        try {
            QueryResponse response = cloudSolrClient.query(query);
            System.out.println(response);
            SolrDocumentList docs = response.getResults();
            System.out.println("Found");
            System.out.println(docs.getNumFound());

            for(SolrDocument doc : docs) {
                ds.add(doc);
            }


        } catch(Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
    public static void main(String[] args) {
        List<SolrDocument> docs = search("*");
        for(SolrDocument doc : docs) {
            System.out.println(doc);
        }
        System.out.println("finished");
    }

}
