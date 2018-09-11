package theme;

import java.util.ArrayList;

import all_interface.Observer;
import all_interface.Subject;

/*
 * 主题对象
 * 这就是被观察者
 * 他需要实现注册观察者和删除观察者的方法
 * 并且提供通知观察者数据发生变化的方法
 */
public class WeatherData implements Subject {

	private ArrayList<Observer> observers;
	private float temperature;
	private float humidity;
	private float pressure;
	
	
	public WeatherData() {
		observers = new ArrayList<>();
	}
	@Override
	public void registerObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		observers.remove(o);
	}

	@Override
	public void notifyObservers() {
		for(int i = 0; i <observers.size(); i++) {
			Observer observer = (Observer) observers.get(i);
			observer.update(temperature, humidity, pressure);
		}
	}
	
	public void measurementsChanged() {
		notifyObservers();
	}
	
	public void setMeasurements(float temperature, float humidity, float pressure) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
		measurementsChanged();
	}

}
