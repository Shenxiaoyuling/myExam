/*
 * �ļ�����StateInfo.java
 * ��Ȩ��Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * ������ StateInfo.java
 * �޸�ʱ�䣺2014-1-9
 * �޸����ݣ�����
 */
package cn.migu.statemachineframework;

/**
 * ״̬����״̬��Ϣ .
 * �����޸Ĵ��ļ�
 * 
 * @author  duohaoxue
 * @version  [�汾��, 2016��9��13��]
 */
public class StateInfo
{
	
	private String stateName; // XML������״̬��
	
	private Object context;// ���Ƶ����ڼ�¼��ǰ��Ϣ����

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
