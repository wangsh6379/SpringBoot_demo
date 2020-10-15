package com.solr.wangsh.solr.service;


import com.solr.wangsh.solr.model.Student;

public interface SolrSearchService {
    public Student queryById(String id);
}
