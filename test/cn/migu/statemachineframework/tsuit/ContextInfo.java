/*
 * �ļ�����ContextInfo.java
 * ��Ȩ��Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * ������ ContextInfo.java
 * �޸�ʱ�䣺2014-1-9
 * �޸����ݣ�����
 */
package cn.migu.statemachineframework.tsuit;

import java.util.ArrayList;
import java.util.List;

/**
 * ������.
 * 
 * @author  duohaoxue
 * @version  [�汾��, 2016��9��13��]
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
