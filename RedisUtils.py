import redis

from Config import REDIS_HOST, REDIS_PORT


def get_redis_connection():
    pool = redis.ConnectionPool(host=REDIS_HOST, port=REDIS_PORT, db=0)
    return redis.Redis(connection_pool=pool)