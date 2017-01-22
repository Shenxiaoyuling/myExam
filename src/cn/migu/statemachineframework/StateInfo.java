/*
 * 文件名：StateInfo.java
 * 版权：Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * 描述： StateInfo.java
 * 修改时间：2014-1-9
 * 修改内容：新增
 */
package cn.migu.statemachineframework;

/**
 * 状态机的状态信息 .
 * 请勿修改此文件
 * 
 * @author  duohaoxue
 * @version  [版本号, 2016年9月13日]
 */
public class StateInfo
{
	
	private String stateName; // XML描述的状态名
	
	private Object context;// 定制的用于记录当前信息的类

    public StateInfo()
    {
    }

    public StateInfo(String stateName, Object context)
    {
        this.stateName = stateName;
        this.context = context;
    }

    public String getStateName()
    {
        return stateName;
    }

    public void setStateName(String stateName)
    {
        this.stateName = stateName;
    }
    
    public Object getContext()
    {
        return context;
    }

    public void setContext(Object context)
    {
        this.context = context;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("StateInfo [stateName=");
        builder.append(stateName);
        builder.append(", context=");
        builder.append(context);
        builder.append("]");
        return builder.toString();
    }

}
