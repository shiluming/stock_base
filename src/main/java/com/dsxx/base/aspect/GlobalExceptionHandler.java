package com.dsxx.base.aspect;

import com.dsxx.base.model.ResultBean;
import com.dsxx.base.service.exception.BusinessException;
import com.dsxx.base.service.exception.DecodeAccessTokenException;
import com.dsxx.base.service.exception.NoLoginException;
import com.dsxx.base.service.exception.NoPermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * 统一异常处理
 * 在Controller中抛出的异常，GlobalExceptionHandler中定义的处理方法可以起作用
 * 其他的业务层异常也可以单独处理
 * @author slm
 * @date 2017/9/18
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 默认的异常处理
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultBean defaultErrorHandler(HttpServletRequest req, Exception e) {
        //记录日志
        LOGGER.error(e.getMessage()+" url:"+req.getRequestURI(),e);
        return ResultBean.error("服务器异常");
    }

    /**
     * 自定义业务异常处理
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResultBean businessExceptionHandler(HttpServletRequest req, Exception e) {
        //记录日志
        LOGGER.error(e.getMessage()+" url:"+req.getRequestURI(),e);
        return ResultBean.error(e.getMessage());
    }

    /**
     * 处理validation异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public ResultBean validationExceptionHandler(HttpServletRequest req, ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage()).append(" ");
        }
        LOGGER.error(strBuilder.toString()+" url:"+req.getRequestURI(),e);
        return ResultBean.error(strBuilder.toString());
    }

    /**
     * 处理Methodvalidation异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultBean MethodvalidationExceptionHandler(MethodArgumentNotValidException e) {
        StringBuilder stringBuilder = new StringBuilder();
        String str = null;
        if (null != e.getBindingResult()) {
            List<FieldError> errorList = e.getBindingResult().getFieldErrors();
            for (FieldError fieldError : errorList) {
                stringBuilder.append(fieldError.getDefaultMessage()).append("|");
            }
            str = stringBuilder.substring(0, stringBuilder.length() - 1);
        }
        return ResultBean.error(str);
    }

    /**
     * 处理未登录异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = NoLoginException.class)
    @ResponseBody
    public ResultBean<String> noLoginExceptionHandler(HttpServletRequest req, NoLoginException e){
        //记录日志
        LOGGER.error(e.getMessage()+" url:"+req.getRequestURI(),e);
        ResultBean<String> resultBean = new ResultBean<>();
        resultBean.setResult(ResultBean.NO_LOGIN);
        resultBean.setMsg(ResultBean.NO_LOGIN_MSG);
        return resultBean;
    }

    /**
     * 处理没有权限异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = NoPermissionException.class)
    @ResponseBody
    public ResultBean<String> noPermissionExceptionHandler(HttpServletRequest req, NoPermissionException e){
        //记录日志
        LOGGER.error(e.getMessage()+" url:"+req.getRequestURI(),e);
        ResultBean<String> resultBean = new ResultBean<>();
        resultBean.setResult(ResultBean.NO_PERMISSION);
        resultBean.setMsg(ResultBean.NO_PERMISSION_MSG);
        return resultBean;
    }

    /**
     * 功能描述: 处理token解析异常 <br>
     * @Author: wzw
     * @Date: 2018/5/10 11:38
     * @param: HttpServletRequest
     * @param: NoPermissionException
     * @return: ResultBean<String>
     */
    @ExceptionHandler(value = DecodeAccessTokenException.class)
    @ResponseBody
    public ResultBean<String> decodeAccessTokenException(HttpServletRequest req, DecodeAccessTokenException e){
        //记录日志
        LOGGER.error(e.getMessage()+" url:"+req.getRequestURI(),e);
        ResultBean<String> resultBean = new ResultBean<>();
        resultBean.setResult(ResultBean.TOKEN_ERROR);
        resultBean.setMsg(ResultBean.FAIL_MSG);
        return resultBean;
    }
}
