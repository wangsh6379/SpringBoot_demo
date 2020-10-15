package com.solr.wangsh.solr.service.impl;

import com.solr.wangsh.solr.model.Student;
import com.solr.wangsh.solr.service.SolrSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

@Component
@Service
public class SolrSearchServiceImpl implements SolrSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Student queryById(String id) {
        Student student = solrTemplate.getById("wangsh", id, Student.class).get();
        Collection<Student> wangsh = solrTemplate.getByIds("wangsh", Arrays.asList(1, 2), Student.class);
        System.out.println(wangsh);
        return student;
    }
}
