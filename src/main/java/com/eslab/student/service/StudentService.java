package com.eslab.student.service;

import com.eslab.student.exception.ApiException;
import com.eslab.student.model.Student;
import com.eslab.util.ESUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static com.eslab.common.ESIndex.INDEX_STUDENT;

@Service
@AllArgsConstructor
public class StudentService {

    private RestHighLevelClient restHighLevelClient;
    private ESUtil esUtil;
    private ObjectMapper objectMapper;

    public Student create(Student student) {
        IndexRequest indexRequest = createIndexRequestForStudent(student);
        try {
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            String responseId = indexResponse.getId();
            return findById(responseId);
        } catch (IOException e) {
            throw new ApiException("Something went wrong while indexing the document.");
        }
    }

    public Student findByRollNumber(String rollNumber) {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("rollNo", rollNumber));
        searchRequest.indices(INDEX_STUDENT).source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = searchResponse.getHits().getHits();
            String docId = Arrays.stream(hits).map(SearchHit::getId).findFirst().orElse(null);
            Student student = Arrays.stream(hits).map((searchHit) -> objectMapper.convertValue(searchHit.getSourceAsMap(), Student.class)).findFirst().orElseThrow(() -> new ApiException("Student not found."));
            student.setId(docId);
            return student;
        } catch (IOException e) {
            throw new ApiException("Something went wrong while searching.");
        }
    }

    public Student update(Student student) {
        Student studentById = findById(student.getId());
        String studentJson = esUtil.modelToJson(student);
        UpdateRequest updateRequest = new UpdateRequest(INDEX_STUDENT, studentById.getId()).doc(studentJson, XContentType.JSON);
        try {
            UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            String responseId = update.getId();
            return findById(responseId);
        } catch (IOException e) {
            throw new ApiException("Something went wrong while updating the document.");
        }

    }

    private IndexRequest createIndexRequestForStudent(Student student) {
        String model = esUtil.modelToJson(student);
        return new IndexRequest(INDEX_STUDENT).source(model, XContentType.JSON);
    }

    public Student findById(String studentId) {
        GetRequest getRequest = new GetRequest(INDEX_STUDENT).id(studentId);
        try {
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            String id = getResponse.getId();
            boolean exists = getResponse.isExists();
            if (exists) {
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
                Student student = objectMapper.convertValue(sourceAsMap, Student.class);
                student.setId(id);
                return student;
            } else {
                throw new ApiException("Student not found with this id.");
            }
        } catch (IOException e) {
            throw new ApiException("Something went wrong while searching.");
        }
    }

    public String deleteById(String studentId) {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX_STUDENT, studentId);
        try {
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            return deleteResponse.getResult().name();
        } catch (IOException e) {
            throw new ApiException("Could not delete student.");
        }
    }
}
