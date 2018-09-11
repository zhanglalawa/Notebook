package theme;

import java.util.ArrayList;

import all_interface.Observer;
import all_interface.Subject;

/*
 * �������
 * ����Ǳ��۲���
 * ����Ҫʵ��ע��۲��ߺ�ɾ���۲��ߵķ���
 * �����ṩ֪ͨ�۲������ݷ����仯�ķ���
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
