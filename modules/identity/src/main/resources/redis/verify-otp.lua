local code = redis.call('HGET', KEYS[1], 'code')
if not code then
    return 0
end

local expiresAt = tonumber(redis.call('HGET', KEYS[1], 'expiresAt'))
if not expiresAt or tonumber(ARGV[2]) >= expiresAt then
    redis.call('DEL', KEYS[1])
    return 0
end

local attempts = tonumber(redis.call('HGET', KEYS[1], 'attempts')) or 0
if attempts >= tonumber(ARGV[3]) then
    return 0
end

attempts = attempts + 1
redis.call('HSET', KEYS[1], 'attempts', attempts)

if code == ARGV[1] then
    redis.call('DEL', KEYS[1])
    return 1
end

return 0
