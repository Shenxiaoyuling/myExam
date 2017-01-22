/*
 * 文件名：StateMachineTest.java
 * 版权：Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * 描述： StateMachineTest.java
 * 修改时间：2014-1-9
 * 修改内容：新增
 */
package cn.migu.statemachineframework;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cn.migu.statemachineframework.helper.TestHelper;
import cn.migu.statemachineframework.tsuit.ContextInfo;
import cn.migu.statemachineframework.tsuit.E1Arg;
import cn.migu.statemachineframework.tsuit.E2Arg;

/**
 * 辅助测试用例.
 * 
 * @author  duohaoxue
 * @version  [版本号, 2016年9月13日]
 */
public class StateMachineFrameWorkTest
{
	
	public static void main(String[] args) {
		StateMachineFrameWorkTest test = new StateMachineFrameWorkTest();
		test.testBasicCreateSM();
	}

    // 基本创建状态机
    @Test
    public void testBasicCreateSM()
    {
        try
        {
            StateMachine sm = StateMachineFrameWork.createStateMachine(XMLStaticInfo.BASE);
            Assert.assertNotNull(sm);
        }
        catch (SMException e)
        {
            e.printStackTrace();
            Assert.fail("error");
        }
    }

    // 创建状态机,XML错误
    @Test
    public void testCreateSMErrXml()
    {
        try
        {
            StateMachineFrameWork.createStateMachine(XMLStaticInfo.XML_ERR);
            Assert.fail("succeed, but expect error.");
        }
        catch (SMException e)
        {
            e.printStackTrace();
            Assert.assertEquals(SMException.XML_ERROR, e.getErrCode());
        }
    }

    // 创建状态机,<function> error,方法非静态
    @Test
    public void testCreateSMErrFuncNoStatic()
    {
        try
        {
            StateMachineFrameWork.createStateMachine(XMLStaticInfo.NO_STATIC_FUNC);
            Assert.fail("succeed, but expect error.");
        }
        catch (SMException e)
        {
            e.printStackTrace();
            Assert.assertEquals(SMException.ERROR_FUNCTION, e.getErrCode());
        }
    }

    // 创建状态机,<rettype> error,指定返回值不符
    @Test
    public void testCreateSMErrRetDiffFunc()
    {
        try
        {
            StateMachineFrameWork.createStateMachine(XMLStaticInfo.ERR_RET_DIFF_VAL);
            Assert.fail("succeed, but expect error.");
        }
        catch (SMException e)
        {
            e.printStackTrace();
            Assert.assertEquals(SMException.ERROR_RETTYPE, e.getErrCode());
        }
    }

    // 状态机静态查询1
    @Test
    public void testBasicStaticQuerySM1()
    {
        try
        {
            StateMachine sm = StateMachineFrameWork.createStateMachine(XMLStaticInfo.BASE);
            Assert.assertNotNull(sm);
            List<String> exp = new ArrayList<String>();
            exp.add("S1");
            exp.add("S2");
            TestHelper.assertStringListEqual(exp, sm.queryNextState("S0"));

        }
        catch (SMException e)
        {
            e.printStackTrace();
            Assert.fail("error");
        }
    }

    // 状态机静态查询，无指定状态
    @Test
    public void testBasicStaticQuerySMErr()
    {
        try
        {
            StateMachine sm = StateMachineFrameWork.createStateMachine(XMLStaticInfo.BASE);
            Assert.assertNotNull(sm);
            sm.queryNextState("S9");
            Assert.fail("succeed, but expect error.");
        }
        catch (SMException e)
        {
            e.printStackTrace();
            Assert.assertEquals(SMException.NO_SUCH_STATE, e.getErrCode());
        }
    }

    // 基本状态机执行
    @Test
    public void testBasicSMRun()
    {
        try
        {
            StateMachine sm = StateMachineFrameWork.createStateMachine(XMLStaticInfo.BASE);
            Assert.assertNotNull(sm);
            Assert.assertEquals("S0", sm.queryState().getStateName());
            E1Arg arg = new E1Arg();
            arg.setArg("OKzzz");
            sm.dealEvent(new Event("e1", arg));
            Assert.assertEquals("S1", sm.queryState().getStateName());
            ContextInfo context = (ContextInfo) sm.queryState().getContext();
            // System.out.println(context.getHistory());

            List<String> exp = new ArrayList<String>();
            exp.add("S0:OKzzz");
            TestHelper.assertStringListEqual(exp, context.getHistory());

        }
        catch (SMException e)
        {
            e.printStackTrace();
            Assert.fail("error");
        }
    }

    // 状态机执行 多步
    @Test
    public void testBasicSMRunMore()
    {
        try
        {
            StateMachine sm = StateMachineFrameWork.createStateMachine(XMLStaticInfo.BASE);
            Assert.assertNotNull(sm);
            Assert.assertEquals("S0", sm.queryState().getStateName());
            E1Arg arg = new E1Arg();
            arg.setArg("OK-e1");
            sm.dealEvent(new Event("e1", arg));
            Assert.assertEquals("S1", sm.queryState().getStateName());

            arg.setArg("OK-e1");
            sm.dealEvent(new Event("e1", arg));
            Assert.assertEquals("S1", sm.queryState().getStateName());

            E2Arg arg2 = new E2Arg();
            arg2.setArg("OK-e2");
            sm.dealEvent(new Event("e2", arg2));
            Assert.assertEquals("S2", sm.queryState().getStateName());

            ContextInfo context = (ContextInfo) sm.queryState().getContext();
            System.out.println(context.getHistory());

            List<String> exp = new ArrayList<String>();
            exp.add("S0:OK-e1");
            exp.add("S1:OK-e1");
            exp.add("S1:OK-e2");
            TestHelper.assertStringListEqual(exp, context.getHistory());

        }
        catch (SMException e)
        {
            e.printStackTrace();
            Assert.fail("error");
        }
    }

}
