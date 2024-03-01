package com.sterling.weatherapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class WeatherApp
 */
@WebServlet("/WeatherApp")
public class WeatherApp extends HttpServlet{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeatherApp() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String apiKey = "0fbefd7653c5b38d546deed86e42c23f";
		String city = request.getParameter("city");
		String apiURL = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
		
		URL url = new URL(apiURL);
		
		HttpURLConnection connetion = (HttpURLConnection) url.openConnection();
		connetion.setRequestMethod("GET");
		
		InputStream inputStream = connetion.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputStream);
		
		StringBuilder reaponceData = new StringBuilder();
		
		Scanner sc = new Scanner(reader);
		
		while(sc.hasNext()) {
			reaponceData.append(sc.nextLine());
		}
		sc.close();
		
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(reaponceData.toString(),JsonObject.class);
		
		
		
		
		
		long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
        String date = new Date(dateTimestamp).toString();
        
		System.out.println();
		double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCelsius = (int) (temperatureKelvin - 273.15);
		
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		 String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperature", temperatureCelsius);
		request.setAttribute("WeatherCondition", weatherCondition);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("WeaterData", reaponceData.toString());
		
		String weatherCon = (String)request.getAttribute("weatherCondition");
		System.out.println(weatherCon);
		connetion.disconnect();
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
		
	} 

}
