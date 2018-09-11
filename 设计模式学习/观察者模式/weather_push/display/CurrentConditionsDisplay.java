package display;

import all_interface.DisplayElement;
import all_interface.Observer;
import all_interface.Subject;
import theme.WeatherData;

/*
 * ���ǹ۲���֮һ
 * ��ǰ�������� ���������º�ʪ��
 * �����Ĳ����Ĺ���д�����������Ǵ�ͬС���
 * ����ȫ����WeatherData�Ĺ۲��ߣ�����Ͳ�д�������ˡ�����Ҫȥ�������غܷ�
 * ����������绹����ѹ����塢Ԥ���ȵȣ������ⶼ��Ҫweatherdata���ṩ������Ϣ��
 * ֻҪ�������ݷ����仯���۲��߾ͻ����̵õ��仯�����ݣ�������Ӧ
 */
public class CurrentConditionsDisplay implements DisplayElement, Observer {
	private float temperature;
	private float humidity;
	private Subject weatherData;//ÿһ�������ƶ���һ��ָ�����⣨�������ݣ���ָ��
	
	public CurrentConditionsDisplay(Subject weatherData) {
		this.weatherData = weatherData;
		weatherData.registerObserver(this);
	}
	
	@Override
	public void update(float temp, float humidity, float pressuere) {
		this.temperature = temp;
		this.humidity = humidity;
		display();
	}

	@Override
	public void display() {
		System.out.println("Current conditions: " + temperature 
				+ "F degrees and " + humidity + "% humidity");
	}

	public static void main(String[] args) {
		WeatherData weatherData = new WeatherData();
		
		//ע������۲���
		CurrentConditionsDisplay conditionsDisplay = new CurrentConditionsDisplay(weatherData);
		
		weatherData.setMeasurements(80, 65, 30.4f);
		System.out.println();
		weatherData.setMeasurements(50, 40, 70.0f);
		System.out.println();
		
		//�ѹ۲���ɾ��֮��
		weatherData.removeObserver(conditionsDisplay);
		weatherData.setMeasurements(40, 50, 52.0f);
	}
}
