package com.solr.wangsh.solr.model;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

@Data
public class Student {

    @Field(value = "id")
    private String id;
    @Field(value = "name")
    private String name;
    @Field(value = "age")
    private Integer age;
    @Field(value = "sex")
    private String sex;


}
