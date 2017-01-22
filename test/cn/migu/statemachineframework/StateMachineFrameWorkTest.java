/*
 * �ļ�����StateMachineTest.java
 * ��Ȩ��Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * ������ StateMachineTest.java
 * �޸�ʱ�䣺2014-1-9
 * �޸����ݣ�����
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
 * ������������.
 * 
 * @author  duohaoxue
 * @version  [�汾��, 2016��9��13��]
 */
public class StateMachineFrameWorkTest
{
	
	public static void main(String[] args) {
		StateMachineFrameWorkTest test = new StateMachineFrameWorkTest();
		test.testBasicCreateSM();
	}

    // ��������״̬��
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

    // ����״̬��,XML����
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

    // ����״̬��,<function> error,�����Ǿ�̬
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

    // ����״̬��,<rettype> error,ָ������ֵ����
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

    // ״̬����̬��ѯ1
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

    // ״̬����̬��ѯ����ָ��״̬
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

    // ����״̬��ִ��
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

    // ״̬��ִ�� �ಽ
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
