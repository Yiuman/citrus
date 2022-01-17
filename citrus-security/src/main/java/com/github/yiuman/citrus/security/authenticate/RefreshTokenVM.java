package com.github.yiuman.citrus.security.authenticate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yiuman
 * @date 2022/1/17
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenVM {
   private String refreshToken;
}
