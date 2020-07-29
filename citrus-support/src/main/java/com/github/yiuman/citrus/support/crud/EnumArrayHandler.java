package com.github.yiuman.citrus.support.crud;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 枚举数组的字段处理器
 *
 * @author yiuman
 * @date 2020/5/29
 */
public class EnumArrayHandler<E extends Enum<E>> extends BaseTypeHandler<E[]> {

    private final Class<E> type;

    @SuppressWarnings("unchecked")
    public EnumArrayHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = (Class<E>) type.getComponentType();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E[] parameter, JdbcType jdbcType) throws SQLException {
        E[] enumConstants = type.getEnumConstants();
        int enumBinaryValue = 0;
        //2的N次方
        for (int e = 0; e < enumConstants.length; e++) {
            int hasIndex = ObjectUtils.containsElement(parameter, enumConstants[e]) ? 1 : 0;
            enumBinaryValue += hasIndex * Math.pow(2, e);
        }
        ps.setInt(i, enumBinaryValue);
    }


    @Override
    public E[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getEnumArray(rs.getInt(columnName));
    }

    @Override
    public E[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getEnumArray(rs.getInt(columnIndex));
    }

    @Override
    public E[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getEnumArray(cs.getInt(columnIndex));
    }

    @SuppressWarnings("unchecked")
    private E[] getEnumArray(int value) {
        if (value == 0) {
            return null;
        }
        //将int转成二进制数组
        char[] binaryCharArray = Integer.toBinaryString(value).toCharArray();

        //包含多少个1
        int containsOneChar = 0;

        //这里用于翻转数组，因为枚举的顺序与二进制的顺利是相反的
        char[] flipBinaryCharArray = new char[binaryCharArray.length];

        for (int length = binaryCharArray.length - 1, index = 0; length >= 0; length--, index++) {
            char binaryChar = binaryCharArray[length];
            flipBinaryCharArray[index] = binaryChar;
            if ('1' == binaryChar) {
                containsOneChar++;
            }

        }
        if (containsOneChar==0) {
            return null;
        }

        E[] enumConstants = type.getEnumConstants();
        E[] enums = (E[]) Array.newInstance(type, containsOneChar);
        for (int e = 0, returnIndex = 0; e < flipBinaryCharArray.length; e++) {
            if ('1' == flipBinaryCharArray[e]) {
                enums[returnIndex++] = enumConstants[e];
            }

        }
        return enums;
    }
}
