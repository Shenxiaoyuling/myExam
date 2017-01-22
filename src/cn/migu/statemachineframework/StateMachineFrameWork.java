/*
 * �ļ�����StateMachineFactory.java
 * ��Ȩ��Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * ������ StateMachineFactory.java
 * �޸�ʱ�䣺2014-1-9
 * �޸����ݣ�����
 */
package cn.migu.statemachineframework;

import bean.BaseXmlBean;
import bean.Rettype;
import bean.State;
import bean.StateEvent;
import cn.migu.statemachineframework.SMException;
import com.thoughtworks.xstream.XStream;

/**
 * ״̬�����칤��. �������Ʊ����еķ���ʵ�֣������Ҫ�ķ��������ԣ�����Ҫ�޸Ļ�ɾ�����еĺ�����������
 * 
 * @author duohaoxue
 * @version [�汾��, 2016��9��13��]
 */
public class StateMachineFrameWork {
	/**
	 * 
	 * ��������xml��������һ��״̬��������.
	 * 
	 * @param xml
	 *            UTF8�����XML���ݣ����磺 <root>xxx</root> ��ע�ⲻ��xml�ļ�����
	 * @return ״̬��
	 * @throws SMException
	 */
	public static StateMachine createStateMachine(String xml) throws SMException {
		BaseXmlBean baseBean=null;
		try{
		baseBean = getBeans(xml);
		}catch(Exception e){
			throw new SMException(SMException.XML_ERROR,"XML��������");
		}
		State state = baseBean.getInitState();//��ȡ��ʼ��״̬
		StateInfo stateInfo = new StateInfo();
		stateInfo.setStateName(state.getName());
		
		StateMachine stateMachine = new StateMachine(stateInfo,baseBean);
		stateMachine.initAll(baseBean);//��ʼ��������Ϣ
		return stateMachine;
	}

	public static BaseXmlBean getBeans(String xml) {
		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		xstream.alias("root", BaseXmlBean.class);
		xstream.alias("state", State.class);
		xstream.alias("event", StateEvent.class);
		xstream.alias("rettype", Rettype.class);

		return (BaseXmlBean) xstream.fromXML(xml);
	}
	/*public static Element initDoc(Document document, Element root, String xml)
			throws SMException {
		try {
			document = DocumentHelper.parseText(xml);
			root = document.getRootElement();
		} catch (Exception e) {
			throw new SMException(SMException.XML_ERROR, "XML��ʽ������������ֹ��ǩ��ƥ�䣩");
		}
		return root;
	}

	public static StateInfo findCurState(List<StateInfo> states,
			String initStateName) throws SMException {
		StateInfo st = null;
		for (StateInfo state : states) {
			if (state.getStateName().equals(initStateName)) {
				st = state;
			}
		}
		return st;
	}*/
}
