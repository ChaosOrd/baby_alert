# The interval between temperature readings in seconds
READINGS_INTERVAL = 600
DATA_PIN = 3
SCX_PIN = 2
REDIS_HOST = 'localhost'
REDIS_PORT = 6379

LAST_MEASUREMENT_KEY = 'last_measurement'
LAST_MEASUREMENT_KEY_TEMPLATE = 'measurement_{}'
TIME_KEY = 'time'
TEMPERATURE_KEY = 'temperature'
HUMIDITY_KEY = 'humidity'
PREV_KEY = 'prev'
MEASUREMENT_EXPIRE_HOURS = 48
