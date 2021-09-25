package com.lxc.file.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author liuxianchun
 * @date 2021/7/17
 */
@Data
@AllArgsConstructor
@Document(indexName = "hot_word_index")
public class HotWord {

    /*id,值*/
    @Id
    private String value;

    /*搜索次数*/
    @Field(type = FieldType.Long)
    private Long searchNum;

    public HotWord(){
        this.searchNum = 0L;
    }

}
