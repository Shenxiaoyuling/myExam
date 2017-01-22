/*
 * 文件名：ContextInfo.java
 * 版权：Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ContextInfo.java
 * 修改时间：2014-1-9
 * 修改内容：新增
 */
package cn.migu.statemachineframework.tsuit;

import java.util.ArrayList;
import java.util.List;

/**
 * 上下文.
 * 
 * @author  duohaoxue
 * @version  [版本号, 2016年9月13日]
 */
public class ContextInfo
{
    List<String> history = new ArrayList<String>();

    public List<String> getHistory()
    {
        return history;
    }

    public void addItem(String item)
    {
        history.add(item);
    }

}
