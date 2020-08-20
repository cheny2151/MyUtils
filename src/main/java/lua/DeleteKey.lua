--
-- Created by IntelliJ IDEA.
-- User: Cheney
-- Date: 2020-08-19
-- Time: 16:32
-- redis批量删除key的lua脚本
--

local len = 1
local cursor = 0

while (true) do
    local sr = redis.call('scan', cursor, 'match', ARGV[1])
    cursor = tonumber(sr[1])
    local keys = sr[2]
    for i = 1, #keys do
        redis.call('del', keys[i])
    end
    len = #keys
    if (cursor == 0 or len == 0) then break end
end