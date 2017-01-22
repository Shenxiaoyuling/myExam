/*
 * 文件名：Event.java
 * 版权：Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * 描述： Event.java
 * 修改时间：2014-1-9
 * 修改内容：新增
 */
package cn.migu.statemachineframework;

/**
 * 状态机接收的事件.
 * 请勿修改此文件
 * 
 * @author  duohaoxue
 * @version  [版本号, 2016年9月13日]
 */
public class Event
{
    private String eventName;// XML描述的事件名

    private Object arg;// 事件携带的参数

    public Event()
    {

    }

    public Event(String eventName, Object arg)
    {
        this.eventName = eventName;
        this.arg = arg;
    }

    public String getEventName()
    {
        return eventName;
    }

    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public Object getArg()
    {
        return arg;
    }

    public void setArg(Object arg)
    {
        this.arg = arg;
    }

}
