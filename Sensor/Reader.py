import time
from pi_sht1x import SHT1x
from RPi import GPIO
from Sensor.Config import READINGS_INTERVAL, DATA_PIN, SCX_PIN
import redis


def save_readings(temperature: float, humidity: float):
    print('Saving data: temperature: {}, Humidity: {}'.format(temperature, humidity))


def read_and_save_data():
    with SHT1x(data_pin=DATA_PIN, sck_pin=SCX_PIN, gpio_mode=GPIO.BCM) as sensor:
        while True:
            temperature = sensor.read_temperature()
            humidity = sensor.read_humidity()
            save_readings(temperature, humidity)
            time.sleep(READINGS_INTERVAL)


if __name__ == '__main__':
    read_and_save_data()