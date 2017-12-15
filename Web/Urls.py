from flask import Flask, jsonify

from Config import LAST_MEASUREMENT_KEY, TIME_KEY, TEMPERATURE_KEY, HUMIDITY_KEY
from RedisUtils import get_redis_connection, convert_keys_and_values_to_dict

APP_NAME = 'baby_sensor'
app = Flask(APP_NAME)


@app.route("/measurements/last")
def last_measurement():
    redis_conn = get_redis_connection()
    last_measurement_key = redis_conn.get(LAST_MEASUREMENT_KEY)
    keys = [TIME_KEY, TEMPERATURE_KEY, HUMIDITY_KEY]
    measurement = redis_conn.hmget(last_measurement_key, [TIME_KEY, TEMPERATURE_KEY, HUMIDITY_KEY])

    return jsonify(**convert_keys_and_values_to_dict(keys, measurement))


if __name__ == "__main__":
    app.run(host='0.0.0.0')
