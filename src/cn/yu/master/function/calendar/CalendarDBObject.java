package cn.yu.master.function.calendar;

public class CalendarDBObject {
	public String day;
	public String title;
	public String event;
	
	public CalendarDBObject() {
	}

	public CalendarDBObject(String day, String title, String event) {
		super();
		this.day = day;
		this.title = title;
		this.event = event;
	}
}
