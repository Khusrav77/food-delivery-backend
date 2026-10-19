local redisTime = redis.call('TIME')
local now = (tonumber(redisTime[1]) * 1000) + math.floor(tonumber(redisTime[2]) / 1000)
local member = ARGV[1]
local blockedFor = 0

for i = 1, 4 do
    local maxRequests = tonumber(ARGV[(i * 2)])
    local windowMillis = tonumber(ARGV[(i * 2) + 1])
    local key = KEYS[i]
    redis.call('ZREMRANGEBYSCORE', key, '-inf', now - windowMillis)

    local count = redis.call('ZCARD', key)
    if count >= maxRequests then
        local oldest = redis.call('ZRANGE', key, 0, 0, 'WITHSCORES')
        if oldest[2] then
            local retryAfter = math.ceil((tonumber(oldest[2]) + windowMillis - now) / 1000)
            if retryAfter > blockedFor then
                blockedFor = retryAfter
            end
        end
    end
end

if blockedFor > 0 then
    return blockedFor
end

for i = 1, 4 do
    local windowMillis = tonumber(ARGV[(i * 2) + 1])
    local key = KEYS[i]
    redis.call('ZADD', key, now, member .. ':' .. i)
    redis.call('PEXPIRE', key, windowMillis)
end

return 0
