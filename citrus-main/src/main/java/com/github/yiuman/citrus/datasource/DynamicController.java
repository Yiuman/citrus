package com.github.yiuman.citrus.datasource;

import com.github.yiuman.citrus.system.entity.Dictionary;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yiuman
 * @date 2020/12/2
 */
@RestController
public class DynamicController {

    private final DictionaryMapperOne dictionaryMapperOne;

    private final DictionaryMapperTwo dictionaryMapperTwo;

    public DynamicController(DictionaryMapperOne dictionaryMapperOne, DictionaryMapperTwo dictionaryMapperTwo) {
        this.dictionaryMapperOne = dictionaryMapperOne;
        this.dictionaryMapperTwo = dictionaryMapperTwo;
    }

    @PostMapping("/test/transactional")
    @Transactional(rollbackFor = Throwable.class)
    public void testTransactional() {
        Dictionary dictionary2 = new Dictionary();
        dictionary2.setDictCode("789789");
        dictionary2.setDictName("测试2");
//
        dictionaryMapperTwo.saveEntity(dictionary2);

        int error = 1 / 0;

        Dictionary dictionary = new Dictionary();
        dictionary.setDictCode("123123");
        dictionary.setDictName("测试1");
        dictionaryMapperOne.saveEntity(dictionary);

    }
}
