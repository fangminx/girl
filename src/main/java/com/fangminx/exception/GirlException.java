package com.fangminx.exception;

import com.fangminx.enums.ResultEnum;

public class GirlException extends RuntimeException {

    private Integer code;
    public GirlException(ResultEnum resultEnum){
        super(resultEnum.getMsg());//RuntimeException含有
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
