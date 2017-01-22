/*
 * �ļ�����S0.java
 * ��Ȩ��Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * ������ S0.java
 * �޸�ʱ�䣺2014-1-9
 * �޸����ݣ�����
 */
package cn.migu.statemachineframework.tsuit;

/**
 * S0״̬���¼����ƴ�����.
 * 
 * @author  duohaoxue
 * @version  [�汾��, 2016��9��13��]
 */
public class S0
{
    public static String onE1(ContextInfo context, E1Arg arg)
    {
        String item = "S0:" + arg.getArg();
        System.out.println("S0:" + arg.getArg());
        context.addItem(item);
        if (arg.getArg().startsWith("OK"))
        {
            return "OK";
        }
        else
        {
            return "err";
        }
    }

    public static int onE2(ContextInfo context, E2Arg arg)
    {
        String item = "S0:" + arg.getArg();
        context.addItem(item);
        if (arg.getArg().startsWith("OK"))
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    public int onE2NoStatic(ContextInfo context, E2Arg arg)
    {
        return 0;
    }
}
