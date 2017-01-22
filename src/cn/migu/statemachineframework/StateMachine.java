/*
 * 文件名：StateMachine.java
 * 版权：Copyright 2013-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * 描述： StateMachine.java
 * 修改时间：2014-1-9
 * 修改内容：新增
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
 * 状态机对象. 可以完善本类中的方法实现，补充必要的方法和属性，但不要修改或删除已有的函数及方法。
 * 
 * @author duohaoxue
 * @version [版本号, 2016年9月13日]
 */
public class StateMachine {
	private BaseXmlBean baseBean;// XML对象bean
	private ContextInfo contextInfo;
	private StateInfo currentState;// 当前状态

	private Map<String, State> allStatesMap = new HashMap<String, State>();// 状态集合
	private Map<String, StateEvent> stateEventsMap = new HashMap<String, StateEvent>();// 单个状态下的事件集合
	// 因XML节点中存在Events和event节点重名的原因，所有解析出来的event必须分别封装到正确的bean中
	private Map<String, StateEvent> eventsMap = new HashMap<String, StateEvent>();// 状态机可接收的事件集合
	// function信息中，方法与类连在一起，因此需要把它们分开封装
	private Map<String, Method> eventMethodMap = new HashMap<String, Method>();// //单个事件对应的方法名
	private Map<String, Class> eventClassMap = new HashMap<String, Class>();// 单个事件对于的类名

	public StateMachine() {
	}

	// 在初次构造实例时顺便设定当前状态
	public StateMachine(StateInfo currentState, BaseXmlBean baseBean)
			throws SMException {
		this.currentState = currentState;
		this.baseBean = baseBean;
		contextInfo = (ContextInfo) createInstance(baseBean.getContext());
	}

	// 用于创建实例
	public Object createInstance(String className) throws SMException {
		Object obj = null;
		try {
			obj = Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new SMException(SMException.LOAD_CLASS_ERROR, "制定类无法加载");
		}
		return obj;

	}

	// 封装BaseXmlBean对象
	public void initAll(BaseXmlBean baseBean) throws SMException {
		constructStates(baseBean);
		constructMethods(baseBean);
	}

	/**
	 * 静态查询状态机信息，假设状态机当前处于stateName指定的状态（与当前状态机的实际状态无关），<br>
	 * 返回收到一个事件后，状态机可能处于的状态列表。
	 * 
	 * @param stateName
	 *            假设状态机当前所处状态的名称
	 * @return 状态列表
	 * @throws SMException
	 */

	public List<String> queryNextState(String stateName) throws SMException {
		List<String> allStates = new ArrayList<String>();
		State state = allStatesMap.get(stateName);
		if (state == null) {
			throw new SMException(SMException.NO_SUCH_STATE, "静态查询指定状态不存在");
		}
		/*
		 * for (StateEvent event : state.getEvents()) { if
		 * (eventIsAvailable(event.getName())) {
		 * allStates.add(event.getObjstate()); } }
		 */
		Collections.sort(allStates);// 按照字典序排列
		return allStates;
	}

	/*
	 * public boolean eventIsAvailable(String eventName) { List<Event> eventList
	 * = baseBean.getEvents(); for (Event e : eventList) { if
	 * (e.getName().equals(eventName)) { return true; } } return false; }
	 */

	/**
	 * 查询状态机当前所处状态.
	 * 
	 * @return 状态信息
	 */
	public StateInfo queryState() {
		// 考生实现
		return currentState;
	}

	/**
	 * 状态机处理事件.
	 * 
	 * @param event
	 *            事件对象
	 * @return 处理完事件后状态机所处状态的名称
	 * @throws SMException
	 */
	public String dealEvent(Event event) throws SMException {
		// 设定event的参数
		// 由于State类拥有事件类的所有属性，因此，不采用Event类，而是将其做转换
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
			// 将要调用的类
			Class nextClass = eventClassMap.get(currentState.getStateName()
					+ "-" + event.getName());
			// 将要调用的方法
			Method invokedMethod = eventMethodMap.get(currentState
					.getStateName() + "-" + event.getName());
			// 参数object
			Object paramObj = createInstance(eventsMap.get(event.getName())
					.getArg());

			if (paramObj instanceof E1Arg) {
				((E1Arg) paramObj).setArg(event.getArg());
			} else if (paramObj instanceof E2Arg) {
				((E2Arg) paramObj).setArg(event.getArg());
			}
			// 调用函数后的返回值
			Object returnedObj = null;
			try {
				// System.out.println("methName="+invokedMethod.getName());
				returnedObj = invokedMethod.invoke(nextClass.newInstance(),
						contextInfo, paramObj);
			} catch (Exception e) {
				throw new SMException(SMException.EVENT_HANDLE_ERROR, "事件处理异常");
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
		List<String> eventsNames = new ArrayList<String>();// 代表Event类的名称列表
		List<String> stateEventNames = new ArrayList<String>();// 代表单个state节点下的StateEvent类的名称列表
		for (StateEvent event : baseBean.getEvents()) {
			eventsMap.put(event.getName(), event);
			if (!eventsNames.contains(event.getName())) {
				eventsNames.add(event.getName());
			} else {
				throw new SMException(SMException.DUP_EVENT,
						"状态机可接受的事件（root/events/）重复");
			}
		}
		for (State state : baseBean.getStates()) {
			allStatesMap.put(state.getName(), state);
			stateEventNames.clear();// 每次进入遍历之前先清空内容
			for (StateEvent eve : state.getEvents()) {
				int nullEvent = 0;
				// 有可能会遇到空事件的情况，没有跳转状态
				if (eve != null
						&& (eve.getName() != null || !eve.getName().equals(""))) {
					stateEventsMap.put(state.getName() + "-" + eve.getName(),
							eve);
					if (!stateEventNames.contains(eve.getName())) {
						stateEventNames.add(eve.getName());
					} else {
						throw new SMException(SMException.DUP_EVENT,
								"同一状态下处理事件（root/states/state/events/）重复");
					}
				} else {
					nullEvent++;
					if (nullEvent > 1) {
						// 空事件数目大于1
						throw new SMException(SMException.DUP_EVENT,
								"同一状态下处理事件（root/states/state/events/）重复");
					} else {
						stateEventsMap.put(state.getName(), null);// 即便是空事件也要加进去
					}
				}
			}
			if (!isin(stateEventNames, eventsNames)) {
				throw new SMException(SMException.NO_SUCH_EVENT,
						"状态接收事件不在状态机可接受的事件列表中");
			}
		}

	}

	public void changeCurrentState(StateEvent event) throws SMException {
		// 获取下一跳的状态
		State state = allStatesMap.get(event.getObjstate());
		if (null == state) {
			throw new SMException(SMException.NO_SUCH_STATE, "状态不存在");
		}
		currentState = new StateInfo(state.getName(), contextInfo);
	}

	// 状态接收事件（root/states/state/events/）是否在状态机可接受的事件（root/events/）列表中
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
				// 需要将类名和函数名区分开来
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
							"指定类无法加载");
				}
				try {
					nextmethod = nextClass.getDeclaredMethod(methodName,
							ContextInfo.class, eveArg.getClass());
				} catch (Exception E) {
					throw new SMException(SMException.ERROR_FUNCTION, "指定方法有误");
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
		// 初始化arg参数
		Class<?> argClass = null;
		try {
			String arg = eventsMap.get(eve.getName()).getArg();
			argClass = Class.forName(arg);
		} catch (Exception classE) {
			throw new SMException(SMException.LOAD_CLASS_ERROR, "指定类无法加载1");
		}

		Method stateEventMethod = null;
		try {
			stateEventMethod = nextClass.getDeclaredMethod(methodName,
					ContextInfo.class, argClass);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			throw new SMException(SMException.ERROR_FUNCTION, "方法找不到或参数不对");
		}

		// 判断是否为公有、静态方法
		int modifiers = stateEventMethod.getModifiers();
		if (!Modifier.isPublic(modifiers)) {
			throw new SMException(SMException.ERROR_FUNCTION,
					"<function>指定的方法不满足要求：不是公有方法");
		} else if (!Modifier.isStatic(modifiers)) {
			throw new SMException(SMException.ERROR_FUNCTION,
					"<function>指定的方法不满足要求：不是静态方法");
		}

		// 判断两个参数所属类是否正确
		/*
		 * Class<?>[] parameterTypes = stateEventMethod.getParameterTypes();
		 * String parameterName1=parameterTypes[0].getName(); String
		 * parameterName2=parameterTypes[1].getName();
		 * if(!parameterName1.equals(contextInfo.getClass().getName())){ throw
		 * new SMException(SMException.ERROR_FUNCTION,"参数异常"); } else
		 * if(!parameterName2.equals(argClass.getClass().getName())){ throw new
		 * SMException(SMException.ERROR_FUNCTION,"参数异常"); }
		 */

		// Type returnType = stateEventMethod.getGenericReturnType();// 获取方法返回类型
		checkMethodReturnType(eve, stateEventMethod.getReturnType(), null, 2);
		return stateEventMethod;
	}

	// 用于判断判断<function>函数实际执行的返回值是否在规定的五种之间
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
					"<rettype>描述不正确（取值不在规定范围）");
		}
		if (rettype.equals("int") || rettype.equals("java.lang.String")
				|| rettype.equals("boolean")) {
			if (succval == null || succval.equals("")) {
				throw new SMException(SMException.ERROR_RETTYPE,
						"<rettype>描述不正确（取值为int、boolean、java.lang.String时未指定succval）");
			}
		}
	}

	// 判断调用函数的返回类型是否符合规范
	public void checkMethodReturnType(StateEvent event, Class<?> retClass,
			Object objReturned, int flag) throws SMException {
		try {
			String succval = event.getRettype().getSuccval();
			String returnType = event.getRettype().getContent();
			// 用于判断判断<function>函数实际执行的返回值是否在规定的五种之间
			checkTypes(returnType, succval);
			String targetType = retClass.getCanonicalName();
			if (!"careless".equals(returnType)) {
				if (retClass != null) {
					// retClass是否是八种基本类型之一
					if (retClass.isPrimitive()) {
						if (!returnType.equals(targetType)) {
							throw new SMException(SMException.ERROR_RETTYPE,
									"指定返回值与<function>实际返回值不符");
						}
					} else {
						if (!Class.forName(returnType).isAssignableFrom(
								retClass)) {
							System.out.println("####:"
									+ retClass.getCanonicalName());
							throw new SMException(SMException.ERROR_RETTYPE,
									"指定返回值与<function>实际返回值不符");
						}
					}
				}
				if (flag == 1) {
					if (objReturned instanceof IRetValue) {
						IRetValue iRetValue = (IRetValue) objReturned;
						if (!iRetValue.isSucceed()) {
							throw new SMException(SMException.ERROR_RETTYPE,
									"指定返回值与<function>实际返回值不符");
						}
					} else {
						// 看函数实际返回值是否等于succval属性取值
						String str = String.valueOf(objReturned);
						if (!str.equals(succval)) {
							throw new SMException(SMException.ERROR_RETTYPE,
									"指定返回值与<function>实际返回值不符");
						}
						if (!objReturned.getClass().getCanonicalName()
								.equals(returnType)) {
							throw new SMException(SMException.ERROR_RETTYPE,
									"指定返回类型与<function>实际返回类型不符");
						}
						// System.out.println(objReturned.getClass().getCanonicalName());
					}
				}
			}
		} catch (Exception e) {
			throw new SMException(SMException.ERROR_RETTYPE, "<rettype>描述不正确");
		}
	}
}
