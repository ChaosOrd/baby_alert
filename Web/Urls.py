APP_NAME = 'baby_sensor'

from flask import Flask

app = Flask(APP_NAME)


@app.route("/temperature")
def temperature():
    return 25