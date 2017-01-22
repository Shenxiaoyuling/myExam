/*
 * �ļ�����S1.java
 * ��Ȩ��Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * ������ S1.java
 * �޸�ʱ�䣺2014-1-9
 * �޸����ݣ�����
 */
package cn.migu.statemachineframework.tsuit;

import cn.migu.statemachineframework.IRetValue;

/**
 * S1״̬���¼����ƴ�����.
 * 
 * @author  duohaoxue
 * @version  [�汾��, 2016��9��13��]
 */
public class S1
{
    public static class S1Ret implements IRetValue
    {
        private boolean succ = false;

        public void setSucc(boolean succ)
        {
            this.succ = succ;
        }

        @Override
        public boolean isSucceed()
        {
            return succ;
        }
    }

    public static class S1RetOther
    {

    }

    public static S1Ret onE1(ContextInfo context, E1Arg arg)
    {
        String item = "S1:" + arg.getArg();
        System.out.println("S1####:" + arg.getArg());
        context.addItem(item);
        S1Ret ret = new S1Ret();
        if (arg.getArg().startsWith("OK"))
        {
            ret.setSucc(true);
        }
        else
        {
            ret.setSucc(false);
        }
        return ret;
    }

    public static S1RetOther onE2(ContextInfo context, E2Arg arg)
    {
        String item = "S1:" + arg.getArg();
        context.addItem(item);
        return new S1RetOther();
    }
}
