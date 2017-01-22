/*
 * 文件名：S0.java
 * 版权：Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * 描述： S0.java
 * 修改时间：2014-1-9
 * 修改内容：新增
 */
package cn.migu.statemachineframework.tsuit;

/**
 * S0状态下事件定制处理函数.
 * 
 * @author  duohaoxue
 * @version  [版本号, 2016年9月13日]
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
