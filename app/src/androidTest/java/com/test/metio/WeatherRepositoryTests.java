package com.test.metio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.test.metio.data.WeatherRepository;
import com.test.metio.module.cookies.Cookies;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.inject.Inject;

import dagger.hilt.android.testing.CustomTestApplication;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
@RunWith(AndroidJUnit4.class)
@CustomTestApplication(MyCustomTestApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WeatherRepositoryTests {

    @Rule(order = 0)
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);
    @Inject
    WeatherRepository weatherRepository;
    Context appContext;
    @Inject
    Cookies cookies;

    @Before
    public void init() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        hiltRule.inject();
    }

    @Test
    public void test_01_getCurrentWeather() {
        assertTrue(weatherRepository.getWeatherForCities(cookies.getListOfCity()).blockingGet());
    }

    @Test
    public void test_02_getListCount() {
        assertEquals(6, weatherRepository.getListOfWeather().blockingGet().size() );
    }

    @After
    public void finish() {  }

}

