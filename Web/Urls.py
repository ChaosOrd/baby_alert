from datetime import datetime

from flask import Flask, jsonify, render_template
import dateutil.parser
from Config import LAST_MEASUREMENT_KEY, TIME_KEY, TEMPERATURE_KEY, HUMIDITY_KEY, PREV_KEY
from RedisUtils import get_redis_connection, convert_keys_and_values_to_dict

APP_NAME = 'baby_sensor'
app = Flask(APP_NAME)


@app.route("/", methods=['GET'])
def top():
    return render_template('top.html')


@app.route("/measurements/last", methods=['GET'])
def last_measurement():
    measurement = _get_last_measurement()

    return jsonify(**measurement)


@app.route("/measurements", methods=['GET'])
def measurements(from_date=None):
    if from_date is None:
        from_date = datetime.min
    else:
        from_date = dateutil.parser.parse(from_date)

    redis_conn = get_redis_connection()
    last_measurement_key = redis_conn.get(LAST_MEASUREMENT_KEY)
    keys = [TIME_KEY, TEMPERATURE_KEY, HUMIDITY_KEY, PREV_KEY]
    measurement = _get_measurement_by_key(redis_conn, last_measurement_key, keys)
    all_measurements = []
    while True:
        measurement_time = dateutil.parser.parse(measurement[TIME_KEY])
        if measurement_time <= from_date:
            break
        all_measurements.append(measurement)
        if PREV_KEY not in measurement:
            break
        measurement = _get_measurement_by_key(redis_conn, measurement[PREV_KEY], keys)

    return jsonify(measurements=all_measurements)


def _get_measurement_by_key(redis_conn, measurement_key, keys_to_retrieve):
    return convert_keys_and_values_to_dict(keys_to_retrieve, redis_conn.hmget(measurement_key))


def _get_last_measurement():
    redis_conn = get_redis_connection()
    last_measurement_key = redis_conn.get(LAST_MEASUREMENT_KEY)
    keys = [TIME_KEY, TEMPERATURE_KEY, HUMIDITY_KEY]
    return _get_measurement_by_key(redis_conn, last_measurement_key, keys)


if __name__ == "__main__":
    app.run()
