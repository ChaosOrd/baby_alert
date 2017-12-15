import redis

from Config import REDIS_HOST, REDIS_PORT


def get_redis_connection():
    pool = redis.ConnectionPool(host=REDIS_HOST, port=REDIS_PORT, db=0)
    return redis.Redis(connection_pool=pool)


def convert_keys_and_values_to_dict(keys, values):
    return {key: value for key, value in zip(keys, values)}
