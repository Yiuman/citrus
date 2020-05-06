package com.github.yiuman.citrus.support.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yiuman
 * @date 2020/5/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortBy {

    private String sortBy;

    private Boolean sortDesc =false;

}
