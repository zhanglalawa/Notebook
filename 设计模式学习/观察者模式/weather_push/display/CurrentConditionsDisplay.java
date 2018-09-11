package display;

import all_interface.DisplayElement;
import all_interface.Observer;
import all_interface.Subject;
import theme.WeatherData;

/*
 * 这是观察者之一
 * 当前情况布告板 包括了气温和湿度
 * 其它的布告板的构造写法都和这里是大同小异的
 * 他们全部是WeatherData的观察者，这里就不写其它的了。。还要去网上下载很烦
 * 可以想象比如还有气压公告板、预告版等等，不过这都需要weatherdata类提供其它信息了
 * 只要天气数据发生变化，观察者就会立刻得到变化的数据，做出反应
 */
public class CurrentConditionsDisplay implements DisplayElement, Observer {
	private float temperature;
	private float humidity;
	private Subject weatherData;//每一个布告牌都有一个指向主题（天气数据）的指针
	
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
		
		//注册这个观察者
		CurrentConditionsDisplay conditionsDisplay = new CurrentConditionsDisplay(weatherData);
		
		weatherData.setMeasurements(80, 65, 30.4f);
		System.out.println();
		weatherData.setMeasurements(50, 40, 70.0f);
		System.out.println();
		
		//把观察者删除之后
		weatherData.removeObserver(conditionsDisplay);
		weatherData.setMeasurements(40, 50, 52.0f);
	}
}
