/*
 * �ļ�����StateMachine.java
 * ��Ȩ��Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * ������ StateMachine.java
 * �޸�ʱ�䣺2014-1-9
 * �޸����ݣ�����
 */
package cn.migu.statemachineframework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bean.BaseXmlBean;
import bean.State;
import bean.StateEvent;
import cn.migu.statemachineframework.StateInfo;
import cn.migu.statemachineframework.Event;
import cn.migu.statemachineframework.tsuit.ContextInfo;
import cn.migu.statemachineframework.tsuit.E1Arg;
import cn.migu.statemachineframework.tsuit.E2Arg;

/**
 * ״̬������. �������Ʊ����еķ���ʵ�֣������Ҫ�ķ��������ԣ�����Ҫ�޸Ļ�ɾ�����еĺ�����������
 * 
 * @author duohaoxue
 * @version [�汾��, 2016��9��13��]
 */
public class StateMachine {
	private BaseXmlBean baseBean;// XML����bean
	private ContextInfo contextInfo;
	private StateInfo currentState;// ��ǰ״̬

	private Map<String, State> allStatesMap = new HashMap<String, State>();// ״̬����
	private Map<String, StateEvent> stateEventsMap = new HashMap<String, StateEvent>();// ����״̬�µ��¼�����
	// ��XML�ڵ��д���Events��event�ڵ�������ԭ�����н���������event����ֱ��װ����ȷ��bean��
	private Map<String, StateEvent> eventsMap = new HashMap<String, StateEvent>();// ״̬���ɽ��յ��¼�����
	// function��Ϣ�У�������������һ�������Ҫ�����Ƿֿ���װ
	private Map<String, Method> eventMethodMap = new HashMap<String, Method>();// //�����¼���Ӧ�ķ�����
	private Map<String, Class> eventClassMap = new HashMap<String, Class>();// �����¼����ڵ�����

	public StateMachine() {
	}

	// �ڳ��ι���ʵ��ʱ˳���趨��ǰ״̬
	public StateMachine(StateInfo currentState, BaseXmlBean baseBean)
			throws SMException {
		this.currentState = currentState;
		this.baseBean = baseBean;
		contextInfo = (ContextInfo) createInstance(baseBean.getContext());
	}

	// ���ڴ���ʵ��
	public Object createInstance(String className) throws SMException {
		Object obj = null;
		try {
			obj = Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new SMException(SMException.LOAD_CLASS_ERROR, "�ƶ����޷�����");
		}
		return obj;

	}

	// ��װBaseXmlBean����
	public void initAll(BaseXmlBean baseBean) throws SMException {
		constructStates(baseBean);
		constructMethods(baseBean);
	}

	/**
	 * ��̬��ѯ״̬����Ϣ������״̬����ǰ����stateNameָ����״̬���뵱ǰ״̬����ʵ��״̬�޹أ���<br>
	 * �����յ�һ���¼���״̬�����ܴ��ڵ�״̬�б�
	 * 
	 * @param stateName
	 *            ����״̬����ǰ����״̬������
	 * @return ״̬�б�
	 * @throws SMException
	 */

	public List<String> queryNextState(String stateName) throws SMException {
		List<String> allStates = new ArrayList<String>();
		State state = allStatesMap.get(stateName);
		if (state == null) {
			throw new SMException(SMException.NO_SUCH_STATE, "��̬��ѯָ��״̬������");
		}
		/*
		 * for (StateEvent event : state.getEvents()) { if
		 * (eventIsAvailable(event.getName())) {
		 * allStates.add(event.getObjstate()); } }
		 */
		Collections.sort(allStates);// �����ֵ�������
		return allStates;
	}

	/*
	 * public boolean eventIsAvailable(String eventName) { List<Event> eventList
	 * = baseBean.getEvents(); for (Event e : eventList) { if
	 * (e.getName().equals(eventName)) { return true; } } return false; }
	 */

	/**
	 * ��ѯ״̬����ǰ����״̬.
	 * 
	 * @return ״̬��Ϣ
	 */
	public StateInfo queryState() {
		// ����ʵ��
		return currentState;
	}

	/**
	 * ״̬�������¼�.
	 * 
	 * @param event
	 *            �¼�����
	 * @return �������¼���״̬������״̬������
	 * @throws SMException
	 */
	public String dealEvent(Event event) throws SMException {
		// �趨event�Ĳ���
		// ����State��ӵ���¼�����������ԣ���ˣ�������Event�࣬���ǽ�����ת��
		String eveName = event.getEventName();
		StateEvent eve = stateEventsMap.get(currentState.getStateName() + "-"
				+ eveName);
		if (null != event.getArg()) {
			Object obj = event.getArg();
			if (obj instanceof E1Arg) {
				eve.setArg(((E1Arg) obj).getArg());
			} else if (obj instanceof E2Arg) {
				eve.setArg(((E2Arg) obj).getArg());
			}
		}
		if (eve != null) {
			handleFunction(eve);
		}
		return currentState.getStateName();
	}

	public void handleFunction(StateEvent event) throws SMException {
		String functionName = event.getFunction();
		if (functionName != null && !functionName.isEmpty()) {
			// ��Ҫ���õ���
			Class nextClass = eventClassMap.get(currentState.getStateName()
					+ "-" + event.getName());
			// ��Ҫ���õķ���
			Method invokedMethod = eventMethodMap.get(currentState
					.getStateName() + "-" + event.getName());
			// ����object
			Object paramObj = createInstance(eventsMap.get(event.getName())
					.getArg());

			if (paramObj instanceof E1Arg) {
				((E1Arg) paramObj).setArg(event.getArg());
			} else if (paramObj instanceof E2Arg) {
				((E2Arg) paramObj).setArg(event.getArg());
			}
			// ���ú�����ķ���ֵ
			Object returnedObj = null;
			try {
				// System.out.println("methName="+invokedMethod.getName());
				returnedObj = invokedMethod.invoke(nextClass.newInstance(),
						contextInfo, paramObj);
			} catch (Exception e) {
				throw new SMException(SMException.EVENT_HANDLE_ERROR, "�¼������쳣");
			}
			checkMethodReturnType(event, returnedObj.getClass(), returnedObj, 1);
			changeCurrentState(event);
		} else {
			String nextState = event.getObjstate();
			if (nextState != null && !nextState.equals("")) {
				changeCurrentState(event);
			}
		}
	}

	public void constructStates(BaseXmlBean baseBean) throws SMException {
		List<String> eventsNames = new ArrayList<String>();// ����Event��������б�
		List<String> stateEventNames = new ArrayList<String>();// ������state�ڵ��µ�StateEvent��������б�
		for (StateEvent event : baseBean.getEvents()) {
			eventsMap.put(event.getName(), event);
			if (!eventsNames.contains(event.getName())) {
				eventsNames.add(event.getName());
			} else {
				throw new SMException(SMException.DUP_EVENT,
						"״̬���ɽ��ܵ��¼���root/events/���ظ�");
			}
		}
		for (State state : baseBean.getStates()) {
			allStatesMap.put(state.getName(), state);
			stateEventNames.clear();// ÿ�ν������֮ǰ���������
			for (StateEvent eve : state.getEvents()) {
				int nullEvent = 0;
				// �п��ܻ��������¼��������û����ת״̬
				if (eve != null
						&& (eve.getName() != null || !eve.getName().equals(""))) {
					stateEventsMap.put(state.getName() + "-" + eve.getName(),
							eve);
					if (!stateEventNames.contains(eve.getName())) {
						stateEventNames.add(eve.getName());
					} else {
						throw new SMException(SMException.DUP_EVENT,
								"ͬһ״̬�´����¼���root/states/state/events/���ظ�");
					}
				} else {
					nullEvent++;
					if (nullEvent > 1) {
						// ���¼���Ŀ����1
						throw new SMException(SMException.DUP_EVENT,
								"ͬһ״̬�´����¼���root/states/state/events/���ظ�");
					} else {
						stateEventsMap.put(state.getName(), null);// �����ǿ��¼�ҲҪ�ӽ�ȥ
					}
				}
			}
			if (!isin(stateEventNames, eventsNames)) {
				throw new SMException(SMException.NO_SUCH_EVENT,
						"״̬�����¼�����״̬���ɽ��ܵ��¼��б���");
			}
		}

	}

	public void changeCurrentState(StateEvent event) throws SMException {
		// ��ȡ��һ����״̬
		State state = allStatesMap.get(event.getObjstate());
		if (null == state) {
			throw new SMException(SMException.NO_SUCH_STATE, "״̬������");
		}
		currentState = new StateInfo(state.getName(), contextInfo);
	}

	// ״̬�����¼���root/states/state/events/���Ƿ���״̬���ɽ��ܵ��¼���root/events/���б���
	public boolean isin(List<String> stateEventNames, List<String> eventsNames) {
		boolean flag = true;
		for (String stateEveName : stateEventNames) {
			if (!eventsNames.contains(stateEveName)) {
				flag = false;
			}
		}
		return flag;
	}

	public void constructMethods(BaseXmlBean baseBean) throws SMException {
		List<State> states = baseBean.getStates();
		for (State state : states) {
			for (StateEvent eve : state.getEvents()) {
				System.out.println(eventsMap.get(eve.getName()).getArg());
				Object eveArg = createInstance(eventsMap.get(eve.getName())
						.getArg());
				String functionName = eve.getFunction();
				// ��Ҫ�������ͺ��������ֿ���
				String className = functionName.substring(0,
						functionName.lastIndexOf("."));
				String methodName = functionName.substring(functionName
						.lastIndexOf(".") + 1);
				Class<?> nextClass = null;
				Method nextmethod = null;
				try {
					nextClass = Class.forName(className);
				} catch (Exception classE) {
					throw new SMException(SMException.LOAD_CLASS_ERROR,
							"ָ�����޷�����");
				}
				try {
					nextmethod = nextClass.getDeclaredMethod(methodName,
							ContextInfo.class, eveArg.getClass());
				} catch (Exception E) {
					throw new SMException(SMException.ERROR_FUNCTION, "ָ����������");
				}
				Method targetMethod = getStateEventMethod(eve, nextClass,
						nextmethod.getName());

				eventClassMap.put(state.getName() + "-" + eve.getName(),
						nextClass);
				eventMethodMap.put(state.getName() + "-" + eve.getName(),
						targetMethod);
			}
		}
	}

	public Method getStateEventMethod(StateEvent eve, Class<?> nextClass,
			String methodName) throws SMException {
		// ��ʼ��arg����
		Class<?> argClass = null;
		try {
			String arg = eventsMap.get(eve.getName()).getArg();
			argClass = Class.forName(arg);
		} catch (Exception classE) {
			throw new SMException(SMException.LOAD_CLASS_ERROR, "ָ�����޷�����1");
		}

		Method stateEventMethod = null;
		try {
			stateEventMethod = nextClass.getDeclaredMethod(methodName,
					ContextInfo.class, argClass);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			throw new SMException(SMException.ERROR_FUNCTION, "�����Ҳ������������");
		}

		// �ж��Ƿ�Ϊ���С���̬����
		int modifiers = stateEventMethod.getModifiers();
		if (!Modifier.isPublic(modifiers)) {
			throw new SMException(SMException.ERROR_FUNCTION,
					"<function>ָ���ķ���������Ҫ�󣺲��ǹ��з���");
		} else if (!Modifier.isStatic(modifiers)) {
			throw new SMException(SMException.ERROR_FUNCTION,
					"<function>ָ���ķ���������Ҫ�󣺲��Ǿ�̬����");
		}

		// �ж����������������Ƿ���ȷ
		/*
		 * Class<?>[] parameterTypes = stateEventMethod.getParameterTypes();
		 * String parameterName1=parameterTypes[0].getName(); String
		 * parameterName2=parameterTypes[1].getName();
		 * if(!parameterName1.equals(contextInfo.getClass().getName())){ throw
		 * new SMException(SMException.ERROR_FUNCTION,"�����쳣"); } else
		 * if(!parameterName2.equals(argClass.getClass().getName())){ throw new
		 * SMException(SMException.ERROR_FUNCTION,"�����쳣"); }
		 */

		// Type returnType = stateEventMethod.getGenericReturnType();// ��ȡ������������
		checkMethodReturnType(eve, stateEventMethod.getReturnType(), null, 2);
		return stateEventMethod;
	}

	// �����ж��ж�<function>����ʵ��ִ�еķ���ֵ�Ƿ��ڹ涨������֮��
	public void checkTypes(String rettype, String succval) throws SMException {
		String[] arrs = { "java.lang.String", "int", "boolean",
				"cn.migu.statemachineframework.IRetValue", "careless" };
		boolean flag = false;
		for (String str : arrs) {
			if (rettype != null && str.equals(rettype)) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			throw new SMException(SMException.ERROR_RETTYPE,
					"<rettype>��������ȷ��ȡֵ���ڹ涨��Χ��");
		}
		if (rettype.equals("int") || rettype.equals("java.lang.String")
				|| rettype.equals("boolean")) {
			if (succval == null || succval.equals("")) {
				throw new SMException(SMException.ERROR_RETTYPE,
						"<rettype>��������ȷ��ȡֵΪint��boolean��java.lang.Stringʱδָ��succval��");
			}
		}
	}

	// �жϵ��ú����ķ��������Ƿ���Ϲ淶
	public void checkMethodReturnType(StateEvent event, Class<?> retClass,
			Object objReturned, int flag) throws SMException {
		try {
			String succval = event.getRettype().getSuccval();
			String returnType = event.getRettype().getContent();
			// �����ж��ж�<function>����ʵ��ִ�еķ���ֵ�Ƿ��ڹ涨������֮��
			checkTypes(returnType, succval);
			String targetType = retClass.getCanonicalName();
			if (!"careless".equals(returnType)) {
				if (retClass != null) {
					// retClass�Ƿ��ǰ��ֻ�������֮һ
					if (retClass.isPrimitive()) {
						if (!returnType.equals(targetType)) {
							throw new SMException(SMException.ERROR_RETTYPE,
									"ָ������ֵ��<function>ʵ�ʷ���ֵ����");
						}
					} else {
						if (!Class.forName(returnType).isAssignableFrom(
								retClass)) {
							System.out.println("####:"
									+ retClass.getCanonicalName());
							throw new SMException(SMException.ERROR_RETTYPE,
									"ָ������ֵ��<function>ʵ�ʷ���ֵ����");
						}
					}
				}
				if (flag == 1) {
					if (objReturned instanceof IRetValue) {
						IRetValue iRetValue = (IRetValue) objReturned;
						if (!iRetValue.isSucceed()) {
							throw new SMException(SMException.ERROR_RETTYPE,
									"ָ������ֵ��<function>ʵ�ʷ���ֵ����");
						}
					} else {
						// ������ʵ�ʷ���ֵ�Ƿ����succval����ȡֵ
						String str = String.valueOf(objReturned);
						if (!str.equals(succval)) {
							throw new SMException(SMException.ERROR_RETTYPE,
									"ָ������ֵ��<function>ʵ�ʷ���ֵ����");
						}
						if (!objReturned.getClass().getCanonicalName()
								.equals(returnType)) {
							throw new SMException(SMException.ERROR_RETTYPE,
									"ָ������������<function>ʵ�ʷ������Ͳ���");
						}
						// System.out.println(objReturned.getClass().getCanonicalName());
					}
				}
			}
		} catch (Exception e) {
			throw new SMException(SMException.ERROR_RETTYPE, "<rettype>��������ȷ");
		}
	}
}
