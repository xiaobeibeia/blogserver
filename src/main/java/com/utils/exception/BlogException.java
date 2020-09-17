package com.utils.exception;

import cn.hutool.json.JSONObject;
import com.utils.enums.ErrorInfoEnum;

public class BlogException extends RuntimeException {
    /**
     * 本系统使用的自定义错误类
     * 比如在校验参数时,如果不符合要求,可以抛出此错误类
     * 拦截器可以统一拦截此错误,将其中json返回给前端
     */
        private JSONObject resultJson;

        /**
         * 调用时可以在任何代码处直接throws这个Exception,
         * 都会统一被拦截,并封装好json返回给前台
         *
         * @param errorEnum 以错误的ErrorEnum做参数
         */
        public BlogException(ErrorInfoEnum errorEnum) {
            JSONObject resultJson1 = new JSONObject();
            resultJson1.put("msg",errorEnum.getMsg());
            resultJson1.put("code",errorEnum.getCode());
            this.resultJson = resultJson1;
        }

        public BlogException(JSONObject resultJson) {
            this.resultJson = resultJson;
        }

        public JSONObject getResultJson() {
            return resultJson;
        }
    }

