package com.yuntwo.raindrop.util;

import com.yuntwo.raindrop.VO.ResultVO;
import com.yuntwo.raindrop.enums.ResultEnum;

/**
 * 向前端返回不同的ResultVO
 */
public class ResultVOUtil {
    public static ResultVO<Object> success(Object obj){
        ResultVO<Object> res = new ResultVO<>();
        res.setCode(ResultEnum.SUCCESS.getCode());
        res.setMsg(ResultEnum.SUCCESS.getMessage());
        res.setData(obj);
        return res;
    }

    public static ResultVO<Object> success(){
        return success(null);
    }

    public static ResultVO successMsg(String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setMsg(msg);
        resultVO.setCode(ResultEnum.SUCCESS.getCode());
        return resultVO;
    }


    public static ResultVO error(Integer code, String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }

    // todo: 代码优化，直接的可以传入ResultEnum对象就可以，后续慢慢改
    public static ResultVO error(ResultEnum resultEnum) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(resultEnum.getCode());
        resultVO.setMsg(resultEnum.getMessage());
        return resultVO;
    }

    public static ResultVO<Object> error(Integer code, String msg,Object data){
        ResultVO<Object> resultVO = new ResultVO<>();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        resultVO.setData(data);
        return resultVO;
    }

    public static ResultVO errorMsg(String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setMsg(msg);
        resultVO.setCode(ResultEnum.UNAUTHORIZED.getCode());
        return resultVO;
    }
}
