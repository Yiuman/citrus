package com.github.yiuman.citrus.support;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.support.utils.CrudUtils;
import com.github.yiuman.citrus.system.entity.Dictionary;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yiuman
 * @date 2021/4/13
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
public class BatchSaveTest {

    public BatchSaveTest() {
    }

    @Test
    @Rollback
    public void bastSaveDict() throws Exception {
        CrudMapper<Dictionary> crudMapper = CrudUtils.getCrudMapper(Dictionary.class);
        List<Dictionary> dictionaryList = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            Dictionary dictionary = new Dictionary();
            dictionary.setDictCode("di" + i);
            dictionary.setDictName("di" + i);

            dictionaryList.add(dictionary);
        }
        boolean success = crudMapper.saveBatch(dictionaryList);
        Assertions.assertTrue(success);

        List<Dictionary> dictionaries = crudMapper.selectList(Wrappers.emptyWrapper());
        log.info("source size {},dictionaries size {}", dictionaryList.size(), dictionaries.size());
        Assertions.assertEquals(dictionaryList.size(), dictionaries.size());
    }

    @Test
    @Rollback
    public void bathSaveOrUpdate() throws Exception {
        CrudMapper<Dictionary> crudMapper = CrudUtils.getCrudMapper(Dictionary.class);
        List<Dictionary> dictionaries = crudMapper.selectList(Wrappers.emptyWrapper());
        int size = dictionaries.size();
        dictionaries.forEach(item -> item.setDictCode("updateupdate" + item.getDictCode()));
        for (int i = 0; i < 10; i++) {
            Dictionary dictionary = new Dictionary();
            dictionary.setDictCode("di" + i);
            dictionary.setDictName("di" + i);

            dictionaries.add(dictionary);
        }
        boolean success = crudMapper.saveBatch(dictionaries);
        Assertions.assertTrue(success);


        log.info("source size {},dictionaries size {}", size, dictionaries.size());
        List<Dictionary> selectList = crudMapper.selectList(Wrappers.emptyWrapper());
        Assertions.assertEquals(size + 10, selectList.size());
    }

}
