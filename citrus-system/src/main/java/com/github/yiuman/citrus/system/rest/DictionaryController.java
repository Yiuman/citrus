package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.system.entity.Dictionary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典管理
 *
 * @author yiuman
 * @date 2020/7/31
 */
@RestController
@RequestMapping("/rest/dicts")
public class DictionaryController extends BaseCrudController<Dictionary, Long> {

}
