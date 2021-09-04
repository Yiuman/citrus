package com.github.yiuman.citrus.support.http;

/**
 * @param <T> 返回数据类型
 * @author yiuman
 * @date 2020/3/30
 */
public class ResponseEntity<T> {

    private Integer code;

    private T data;

    private String message = "";

    public ResponseEntity() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static <T> ResponseEntity<T> ok() {
        return ResponseEntity.ok(null);
    }

    public static <T> ResponseEntity<T> ok(T data) {
        ResponseEntity<T> responseResult = new ResponseEntity<>();
        responseResult.setCode(ResponseStatusCode.OK);
        responseResult.setData(data);
        return responseResult;
    }

    public static <T> ResponseEntity<T> badRequest(String message) {
        return error(ResponseStatusCode.BAD_REQUEST, message);
    }

    public static <T> ResponseEntity<T> error(String message) {
        return error(ResponseStatusCode.SERVER_ERROR, message);
    }

    public static <T> ResponseEntity<T> error(Integer code, String message) {
        ResponseEntity<T> responseResult = new ResponseEntity<>();
        responseResult.setCode(code);
        responseResult.setMessage(message);
        return responseResult;
    }
}
