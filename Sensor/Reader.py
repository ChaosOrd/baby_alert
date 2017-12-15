from datetime import datetime
import time
from pi_sht1x import SHT1x
from RPi import GPIO
from Sensor.Config import READINGS_INTERVAL, DATA_PIN, SCX_PIN, REDIS_PORT, REDIS_HOST, \
    TIME_KEY, PREV_KEY, LAST_MEASUREMENT_KEY, TEMPERATURE_KEY, HUMIDITY_KEY, LAST_MEASUREMENT_KEY_TEMPLATE, \
    MEASUREMENT_EXPIRE_HOURS
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
    with SHT1x(data_pin=DATA_PIN, sck_pin=SCX_PIN, gpio_mode=GPIO.BCM) as sensor:
        redis_conn = _get_redis_connection()
        while True:
            temperature = sensor.read_temperature()
            humidity = sensor.read_humidity()
            save_readings(redis_conn, temperature, humidity)
            time.sleep(READINGS_INTERVAL)


def _get_redis_connection():
    pool = redis.ConnectionPool(host=REDIS_HOST, port=REDIS_PORT, db=0)
    return redis.Redis(connection_pool=pool)


if __name__ == '__main__':
    read_and_save_data()
