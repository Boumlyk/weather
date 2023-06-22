package com.test.metio.tools;

import com.test.metio.module.weather.CurrentWeatherEntity;
import com.test.metio.module.weather.CurrentWeatherResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static String getIconUrl(String codeIcon){
        return "https://openweathermap.org/img/wn/"+codeIcon+"@2x.png";
    }

    public static String formatDateAndTime(long timestamp) {
        // Create a Date object from the timestamp
        Date date = new Date(timestamp);

        // Define the desired date and time format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM", Locale.ENGLISH);
        // Format the date and time
        String formattedDate = dateFormat.format(date);
        // Concatenate the formatted date and time
        return formattedDate;
    }
    public static String formatDateString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String extractedDate="";
        try {
            Date date = dateFormat.parse(dateString);
            SimpleDateFormat newDateFormat = new SimpleDateFormat("dd-MMM HH:mm");
            extractedDate = newDateFormat.format(date);
            System.out.println(extractedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return extractedDate;
    }
    public static CurrentWeatherEntity getCurrentEntity(CurrentWeatherResponse response){
        return new CurrentWeatherEntity(response.getId(), response.getDt(), response.getDtTxt(), response.getName(),
                response.getWeather().get(0).getIcon(),response.getMain().getPressure(),response.getWeather().get(0).getDescription(),response.getMain().getTemp(),
                response.getMain().getHumidity(),response.getSys().getCountry(), response.getWind().getSpeed(),
                response.getWind().getDeg(),response.getCoord()!=null? response.getCoord().getLon():0.0,response.getCoord()!=null? response.getCoord().getLat():0.0,response.getWeather().get(0).getMain()
                ,response.getMain().getTempMin(),
                response.getMain().getTempMax());
    }

    public static List<CurrentWeatherEntity> getCurrentEntities(List<CurrentWeatherResponse> responses){
         List<CurrentWeatherEntity> list=new ArrayList<>();
         for (CurrentWeatherResponse response:responses){
             CurrentWeatherEntity entity=new CurrentWeatherEntity(response.getId(), response.getDt(), response.getDtTxt(), response.getName(),
                     response.getWeather().get(0).getIcon(),response.getMain().getPressure(),response.getWeather().get(0).getDescription(),response.getMain().getTemp(),
                     response.getMain().getHumidity(),response.getSys().getCountry(), response.getWind().getSpeed(),
                     response.getWind().getDeg(),response.getCoord()!=null? response.getCoord().getLon():0.0,response.getCoord()!=null? response.getCoord().getLat():0.0,response.getWeather().get(0).getMain()
                     ,response.getMain().getTempMin(),
                     response.getMain().getTempMax());
             list.add(entity);
         }

         return list;
    }
}
