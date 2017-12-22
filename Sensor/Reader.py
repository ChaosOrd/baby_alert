import statistics
from datetime import datetime
import time
from pi_sht1x import SHT1x
from RPi import GPIO
from RedisUtils import get_redis_connection
from Config import READINGS_INTERVAL, DATA_PIN, SCX_PIN, TIME_KEY, PREV_KEY, LAST_MEASUREMENT_KEY, \
    TEMPERATURE_KEY, HUMIDITY_KEY, LAST_MEASUREMENT_KEY_TEMPLATE, \
    MEASUREMENT_EXPIRE_HOURS, NUM_OF_READINGS
import redis


def save_readings(redis_conn: redis.Redis, temperature: float, humidity: float):
    now = datetime.utcnow()
    current_measurement = {
        TIME_KEY: now.isoformat(),
        TEMPERATURE_KEY: str(temperature),
        HUMIDITY_KEY: str(humidity)
    }
    last_measurement = redis_conn.get(LAST_MEASUREMENT_KEY)
    if last_measurement is not None:
        current_measurement[PREV_KEY] = last_measurement
    curr_measurement_key = LAST_MEASUREMENT_KEY_TEMPLATE.format(now.isoformat())
    redis_conn.hmset(curr_measurement_key, current_measurement)
    redis_conn.expire(curr_measurement_key, MEASUREMENT_EXPIRE_HOURS * 60 * 60)
    redis_conn.set(LAST_MEASUREMENT_KEY, curr_measurement_key)


def read_and_save_data():
    while True:
        with SHT1x(data_pin=DATA_PIN, sck_pin=SCX_PIN, gpio_mode=GPIO.BCM) as sensor:
            temperature_readings = []
            humidity_readings = []
            for reading_num in range(NUM_OF_READINGS):
                temperature_readings.append(sensor.read_temperature())
                humidity_readings.append(sensor.read_humidity())
                time.sleep(1)
        redis_conn = get_redis_connection()
        save_readings(redis_conn, statistics.median_grouped(temperature_readings),
                      statistics.median_grouped(humidity_readings))
        time.sleep(READINGS_INTERVAL)


if __name__ == '__main__':
    read_and_save_data()
