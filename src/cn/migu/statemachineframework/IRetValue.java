/*
 * 文件名：IRetValue.java
 * 版权：Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * 描述： IRetValue.java
 * 修改时间：2014-1-28
 * 修改内容：新增
 */
package cn.migu.statemachineframework;

/**
 * 复杂返回值接口.
 * 
 * @author  duohaoxue
 * @version  [版本号, 2016年9月13日]
 */
public interface IRetValue
{
    /**
     * 
     * 是否是成功返回值.
     * 
     * @return true: 成功; false: 失败
     */
    boolean isSucceed();
}
