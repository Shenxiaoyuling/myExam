/*
 * 文件名：StateMachineFactory.java
 * 版权：Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * 描述： StateMachineFactory.java
 * 修改时间：2014-1-9
 * 修改内容：新增
 */
package cn.migu.statemachineframework;

import bean.BaseXmlBean;
import bean.Rettype;
import bean.State;
import bean.StateEvent;
import cn.migu.statemachineframework.SMException;
import com.thoughtworks.xstream.XStream;

/**
 * 状态机制造工厂. 可以完善本类中的方法实现，补充必要的方法和属性，但不要修改或删除已有的函数及方法。
 * 
 * @author duohaoxue
 * @version [版本号, 2016年9月13日]
 */
public class StateMachineFrameWork {
	/**
	 * 
	 * 根据输入xml描述创建一个状态机并返回.
	 * 
	 * @param xml
	 *            UTF8编码的XML内容，形如： <root>xxx</root> （注意不是xml文件名）
	 * @return 状态机
	 * @throws SMException
	 */
	public static StateMachine createStateMachine(String xml) throws SMException {
		BaseXmlBean baseBean=null;
		try{
		baseBean = getBeans(xml);
		}catch(Exception e){
			throw new SMException(SMException.XML_ERROR,"XML解析错误");
		}
		State state = baseBean.getInitState();//获取初始化状态
		StateInfo stateInfo = new StateInfo();
		stateInfo.setStateName(state.getName());
		
		StateMachine stateMachine = new StateMachine(stateInfo,baseBean);
		stateMachine.initAll(baseBean);//初始化所有信息
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
			throw new SMException(SMException.XML_ERROR, "XML格式解析错误（如起止标签不匹配）");
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
