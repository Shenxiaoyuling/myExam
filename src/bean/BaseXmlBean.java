package bean;

import cn.migu.statemachineframework.SMException;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 */
@XStreamAlias("root")
public class BaseXmlBean {

	private String context;
	private List<State> states;
	private List<StateEvent> events;

	public List<StateEvent> getEvents() {
		return events;
	}

	public void setEvents(List<StateEvent> events) {
		this.events = events;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

	//��ȡ��ʼ��״̬
	public State getInitState() throws SMException {
		int flag=0;
		State st=new State();
		if (states!=null) {
			for (State state : states) {
				if (state.getStart() != null && state.getStart() != "") {
					flag++;
					st=state;
				}
			}
			if(flag==0){
				throw new SMException(SMException.NO_START_STATE,"δָ����ʼ״̬");
			}
			else if(flag==1){
				return st;
			}
			else if(flag>1){
				throw new SMException(SMException.TOO_MANY_START_STATE,"ָ�������ʼ״̬");
			}
		}
		return null;
	}
}
