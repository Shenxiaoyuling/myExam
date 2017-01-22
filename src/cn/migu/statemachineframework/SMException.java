/*
 * �ļ�����SMException.java
 * ��Ȩ��Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * ������ SMException.java
 * �޸�ʱ�䣺2014-1-9
 * �޸����ݣ�����
 */
package cn.migu.statemachineframework;

/**
 * ��ϵͳ�Զ����쳣.
 * �����޸Ĵ��ļ�
 * 
 * @author  duohaoxue
 * @version  [�汾��, 2016��9��13��]
 */
public class SMException extends Exception
{
    /** XML��ʽ������������ֹ��ǩ��ƥ�䣩 */
    public static final int XML_ERROR = 1;

    /** δָ����ʼ״̬ */
    public static final int NO_START_STATE = 2;

    /** ָ�������ʼ״̬ */
    public static final int TOO_MANY_START_STATE = 3;

    /** ״̬������ */
    public static final int NO_SUCH_STATE = 4;

    /**
     * <rettype>��������ȷ��ȡֵ���ڹ涨��Χ��ָ������ֵ��<function>ʵ�ʷ���ֵ������<br>
     * ȡֵΪint��boolean��java.lang.Stringʱδָ��succval��
     */
    public static final int ERROR_RETTYPE = 5;

    /**
     * <function>ָ���ķ���������Ҫ�󣺲��Ǿ�̬��������������ԡ��򷽷��Ҳ�����<br>
     * ע�⣺�����������Ҳ�����������7��
     */
    public static final int ERROR_FUNCTION = 6;

    /** ָ�����޷����� */
    public static final int LOAD_CLASS_ERROR = 7;

    /** ״̬�����¼�����ϵͳ�����¼��б��� */
    public static final int NO_SUCH_EVENT = 8;

    /** �¼��ظ���ϵͳ�¼����ظ���ͬһ״̬�´����¼��ظ��� */
    public static final int DUP_EVENT = 9;

    /** �¼������쳣 */
    public static final int EVENT_HANDLE_ERROR = 10;

    private static final long serialVersionUID = 1L;

    private int errCode;

    /**
     * 
     * ���캯��.
     * 
     * @param errCode
     */
    public SMException(int errCode)
    {
        super();
        this.errCode = errCode;
    }

    public SMException(int errCode, String msg)
    {
        super(msg);
        this.errCode = errCode;
    }

    public int getErrCode()
    {
        return errCode;
    }

    public void setErrCode(int errCode)
    {
        this.errCode = errCode;
    }

    @Override
    public String getMessage()
    {
        return "ErrCode:" + Integer.valueOf(errCode).toString() + " info:" + super.getMessage();
    }
}
