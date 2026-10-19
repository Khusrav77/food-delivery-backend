redis.call('HSET', KEYS[1], 'code', ARGV[1], 'attempts', '0', 'expiresAt', ARGV[2])
redis.call('PEXPIREAT', KEYS[1], ARGV[2])
return 1
