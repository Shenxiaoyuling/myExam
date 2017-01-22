/*
 * 文件名：S1.java
 * 版权：Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * 描述： S1.java
 * 修改时间：2014-1-9
 * 修改内容：新增
 */
package cn.migu.statemachineframework.tsuit;

import cn.migu.statemachineframework.IRetValue;

/**
 * S1状态下事件定制处理函数.
 * 
 * @author  duohaoxue
 * @version  [版本号, 2016年9月13日]
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
