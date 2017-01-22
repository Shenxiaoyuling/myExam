/*
 * 文件名：SMException.java
 * 版权：Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SMException.java
 * 修改时间：2014-1-9
 * 修改内容：新增
 */
package cn.migu.statemachineframework;

/**
 * 本系统自定义异常.
 * 请勿修改此文件
 * 
 * @author  duohaoxue
 * @version  [版本号, 2016年9月13日]
 */
public class SMException extends Exception
{
    /** XML格式解析错误（如起止标签不匹配） */
    public static final int XML_ERROR = 1;

    /** 未指定初始状态 */
    public static final int NO_START_STATE = 2;

    /** 指定过多初始状态 */
    public static final int TOO_MANY_START_STATE = 3;

    /** 状态不存在 */
    public static final int NO_SUCH_STATE = 4;

    /**
     * <rettype>描述不正确（取值不在规定范围，指定返回值与<function>实际返回值不符，<br>
     * 取值为int、boolean、java.lang.String时未指定succval）
     */
    public static final int ERROR_RETTYPE = 5;

    /**
     * <function>指定的方法不满足要求：不是静态方法、或参数不对、或方法找不到。<br>
     * 注意：方法所在类找不到报错误码7。
     */
    public static final int ERROR_FUNCTION = 6;

    /** 指定类无法加载 */
    public static final int LOAD_CLASS_ERROR = 7;

    /** 状态接收事件不在系统接收事件列表中 */
    public static final int NO_SUCH_EVENT = 8;

    /** 事件重复（系统事件名重复，同一状态下处理事件重复） */
    public static final int DUP_EVENT = 9;

    /** 事件处理异常 */
    public static final int EVENT_HANDLE_ERROR = 10;

    private static final long serialVersionUID = 1L;

    private int errCode;

    /**
     * 
     * 构造函数.
     * 
     * @param errCode
     */
    public SMException(int errCode)
    {
        super();
        this.errCode = errCode;
    }

    public SMException(int errCode, String msg)
    {
        super(msg);
        this.errCode = errCode;
    }

    public int getErrCode()
    {
        return errCode;
    }

    public void setErrCode(int errCode)
    {
        this.errCode = errCode;
    }

    @Override
    public String getMessage()
    {
        return "ErrCode:" + Integer.valueOf(errCode).toString() + " info:" + super.getMessage();
    }
}
