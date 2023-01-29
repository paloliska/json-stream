# json-stream Project

Json ChunkedStreamingMultiSubscriber bug demonstrator

## test description


Test is requesting 3 item from GreetingResource.
GreetingResource is emitting item every 2 seconds.
Test times out in 5 seconds. This means that test should receive 3 items as following:
 - 1st item should be received immediately after connecting (at time 0s)
 - 2nd item at time ~2s
 - 3rd itme at time ~4s

However, test is `receiving` items with 2 sec. delay.
After analysis items are received by client, but are not passed further until stream entity separator is received.

Example logs:
```
18:17:22,203 INFO  [GreetingResource] (executor-thread-0) Multi.IntervalMulti.0 | onItem(0)                     
        << 1st item emitted
18:17:22,237 INFO  [GreetingResource] (vert.x-eventloop-thread-0) Multi.IntervalMulti.0 | request(1)
18:17:24,203 INFO  [GreetingResource] (executor-thread-0) Multi.IntervalMulti.0 | onItem(1)                     
        << 2nd item emmitted            
18:17:24,209 INFO  [GreetingResource] (vert.x-eventloop-thread-0) Multi.IntervalMulti.0 | request(1)
18:17:24,270 INFO  [GreetingResourceTest] (vert.x-eventloop-thread-2) Client onItem                             
<< 1st item passed to test after 2 sec. when client received stream item separator and 2nd item  
18:17:26,203 INFO  [GreetingResource] (executor-thread-0) Multi.IntervalMulti.0 | onItem(2)
18:17:26,207 INFO  [GreetingResource] (vert.x-eventloop-thread-0) Multi.IntervalMulti.0 | request(1)
18:17:26,209 INFO  [GreetingResourceTest] (vert.x-eventloop-thread-2) Client onItem                             
        << 2st item passed to test after 2 sec. when client received stream item separator and 3rd item

java.lang.AssertionError: Expected 3 items.  Only 2 items have been received.
```

## Expected result

Test should pass.

Received item should be processed immediately.

## Proposed solution

ChunkedStreamingMultiSubscriber should send data with separator as suffix, not prefix.