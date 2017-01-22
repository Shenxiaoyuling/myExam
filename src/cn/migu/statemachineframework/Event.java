/*
 * �ļ�����Event.java
 * ��Ȩ��Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * ������ Event.java
 * �޸�ʱ�䣺2014-1-9
 * �޸����ݣ�����
 */
package cn.migu.statemachineframework;

/**
 * ״̬�����յ��¼�.
 * �����޸Ĵ��ļ�
 * 
 * @author  duohaoxue
 * @version  [�汾��, 2016��9��13��]
 */
public class Event
{
    private String eventName;// XML�������¼���

    private Object arg;// �¼�Я���Ĳ���

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
