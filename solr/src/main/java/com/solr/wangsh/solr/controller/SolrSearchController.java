package com.solr.wangsh.solr.controller;

import com.solr.wangsh.solr.model.Student;
import com.solr.wangsh.solr.service.SolrSearchService;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("solrSearch")
public class SolrSearchController {

    @Autowired
    private SolrSearchService solrSearchService;

    @Autowired
    private SolrTemplate solrTemplate;

    @RequestMapping("queryById")
    public Student queryById(@RequestParam String id) {
        Student student = solrSearchService.queryById(id);
        System.out.println("ID:" + student.getId() + ",姓名：" + student.getName() + "，年龄：" + student.getAge() + "，性别：" + student.getSex());
        return null;
    }

    /**
     * 查询，通过条件进行查询
     *
     * @param id
     * @return
     */
    @GetMapping("/query")
    public List<Student> text(@RequestParam String id) {
        //指定查询索引
        SimpleQuery sq = new SimpleQuery("name:唱歌");
        //查到查询数据
        Page<Student> wangsh = solrTemplate.query("wangsh", sq, Student.class);
        List<Student> content = wangsh.getContent();
        for (Student student : content) {
            System.out.println("ID:" + student.getId() + ",姓名：" + student.getName() + "，年龄：" + student.getAge() + "，性别：" + student.getSex());
        }
        System.out.println(content.size());
        return null;
    }

    /**
     * 以ID为主键，如果存在则更新，如果不存在则新增
     */
    @GetMapping("/updateById")
    public void updateById() {
        Student student = new Student();
        student.setAge(100);
        student.setName("小明");
        student.setSex("男");
        student.setId("1");
        UpdateResponse response = solrTemplate.saveBean("wangsh", student);
        solrTemplate.commit("wangsh");
        System.out.println(response);
    }

    /**
     * 增加字段，到指定的分区中。
     * <field name="content_id" type="plongs"/>
     * //     <field name="id" type="string" multiValued="false" indexed="true" required="true" stored="true"/>
     * //     <field name="name" type="string" uninvertible="false" indexed="true" stored="true"/>
     * //     <field name="sex" type="string" uninvertible="false" indexed="true" stored="true"/>
     * //     <field name="type" type="plongs"/>
     */
    @GetMapping("/testUpdateDoc2")
    public void testUpdateDoc2() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", 4);
        doc.addField("content_id", 4);
        doc.addField("type", 1);

        UpdateResponse response = solrTemplate.saveDocument("wangsh", doc);
        solrTemplate.commit("wangsh");
        System.out.println(response);
    }

    /**
     * 根据ID删除数据,可删除多个
     */
    @GetMapping("/deleteByIds")
    public void deleteByIds() {
        solrTemplate.deleteByIds("wangsh", Arrays.asList("2", "3"));
        solrTemplate.commit("wangsh");
    }

    /**
     * 根据条件进行删除，可满足多个条件即可删除数据
     */
    @GetMapping("/deleteById")
    public void deleteById() {
        SolrDataQuery query = new SimpleQuery();
        query.addCriteria(Criteria.where("name").startsWith("小明"));
        //如果多加一个条件，类似于 SQL中 AND
        query.addCriteria(Criteria.where("sex").startsWith("男"));
        solrTemplate.delete("wangsh", query);
        solrTemplate.commit("wangsh");
    }

    /**
     * 单字段模糊查询
     */
    @GetMapping("/likeQuery")
    public void likeQuery() {
        SimpleQuery query = new SimpleQuery();
        query.addCriteria(Criteria.where("sex").contains("女"));
        Page<Student> wangsh = solrTemplate.query("wangsh", query, Student.class);
        for (Student student : wangsh) {
            System.out.println("ID:" + student.getId() + ",姓名：" + student.getName() + "，年龄：" + student.getAge() + "，性别：" + student.getSex());
        }
    }

    /**
     * 多字段模糊查询
     */
    @GetMapping("/andQuery")
    public void andQuery() {
        SimpleQuery query = new SimpleQuery();
        query.addCriteria(Criteria.where("sex").and("name").contains("小"));
        Page<Student> wangsh = solrTemplate.query("wangsh", query, Student.class);
        for (Student student : wangsh) {
            System.out.println("ID:" + student.getId() + ",姓名：" + student.getName() + "，年龄：" + student.getAge() + "，性别：" + student.getSex());
        }
    }

    /**
     * 在XXX之间，包含前后。模糊查询
     */
    @GetMapping("/betweenQuery")
    public void betweenQuery() {
        SimpleQuery query = new SimpleQuery();
        query.addCriteria(Criteria.where("id").between("1","3"));
        Page<Student> wangsh = solrTemplate.query("wangsh", query, Student.class);
        for (Student student : wangsh) {
            System.out.println("ID:" + student.getId() + ",姓名：" + student.getName() + "，年龄：" + student.getAge() + "，性别：" + student.getSex());
        }
    }
}
