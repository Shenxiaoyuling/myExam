package bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;

/**
 */
@XStreamAlias("state")
public class State {

    @XStreamAsAttribute
    @XStreamAlias("start")
    private String start;
    private String name;
    private List<StateEvent> events;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StateEvent> getEvents() {
        return events;
    }

    public void setEvents(List<StateEvent> events) {
        this.events = events;
    }
}
