from flask import Flask, jsonify

from Config import LAST_MEASUREMENT_KEY
from RedisUtils import get_redis_connection

APP_NAME = 'baby_sensor'
app = Flask(APP_NAME)


@app.route("/measurements/last")
def last_measurement():
    redis_conn = get_redis_connection()
    last_measurement_key = redis_conn.get(LAST_MEASUREMENT_KEY)
    measurement = redis_conn.get(last_measurement_key)
    return jsonify(measurement)


if __name__ == "__main__":
    app.run(host='localhost')
